
## 📌 Visão Geral

Projeto backend desenvolvido em **Java + Spring Boot (starter-web)** para integração com a **API oficial do WhatsApp Cloud (Meta)**.

O sistema é responsável por:

- 📩 Receber mensagens e eventos via **Webhook**
- 📤 Enviar mensagens via **Graph API**
- 🔐 Centralizar tokensss, segurança e regras de negócio no **backend**
- 🔄 Servir como base para aplicações **SaaS** (ex.: clínicas, empresas)

> ⚠️ O frontend **nunca** se comunica diretamente com a Meta.  
> Toda integração externa é responsabilidade do backend.

---

## 🧱 Arquitetura (Visão Geral)

modules/whatsapp
  ├─ application
  │   └─ controllers
  ├─ domain
  │   ├─ services
  │   ├─ models
  │   └─ gateways
  └─ infrastructure
      └─ meta
          ├─ client
          ├─ adapter
          ├─ dto
          └─ mapper

Visão geral do desenho

modules/: cada subpasta é um módulo (bounded context). Um módulo não deve vazar detalhes internos para o outro.

application/: camada de entrada (HTTP). Aqui só orquestra request/response.

domain/: regras e linguagem do negócio. Aqui é o “coração”.

infrastructure/: detalhes técnicos (HTTP client, banco, configs, integrações).

shared/: coisas realmente compartilháveis (exceções base, value objects, config comum), com parcimônia.

Módulo whatsapp
modules/whatsapp/application/controllers
EnviarMensagemController

Responsabilidade

Expor endpoints para envio de mensagens (ex.: POST /whatsapp/mensagens).

Receber DTO de request (payload do seu sistema).

Validar formato (Bean Validation) e delegar a execução para o domínio (via MensagemService).

Retornar DTO de response.

O que pode

Validar request (@Valid, campos obrigatórios, formatos).

Traduzir erro para HTTP (ou deixar para um handler global).

O que não pode

Não chama Meta diretamente.

Não conhece MetaWhatsAppClient.

Não monta JSON de integração.

Não tem regra de negócio (“pode enviar? pode template? limitações?”).

WebhookController

Responsabilidade

Expor endpoints de webhook (recebimento de eventos: mensagens/status).

Fazer validação do handshake (se aplicável) e validação mínima da assinatura/token.

Transformar a entrada em um modelo interno e delegar ao domínio.

O que pode

Tratar diferenças de protocolo (GET de verificação / POST de eventos).

Enfileirar/processar async (se você evoluir depois), mas mantendo a decisão no domínio.

O que não pode

Não faz regra de negócio (“o que responder”, “o que salvar”, “como correlacionar status”).

Não conhece detalhes de persistência.

modules/whatsapp/domain
services/MensagemService

Responsabilidade

Ser o orquestrador de regras de negócio do WhatsApp.

Garantir invariantes do seu negócio: validações de fluxo, seleção de tipo de envio, autorização, limites, fallback, auditoria etc.

Chamar portas (interfaces) para o mundo externo: aqui entra o WhatsAppGateway.

O que entra

Models do domínio (ex.: Mensagem) ou DTOs internos de caso de uso.

O que sai

Resultado de negócio (ex.: MensagemEnviadaResponse, status interno, ids, etc).

O que não pode

Não constrói payload da Meta.

Não faz HTTP (sem WebClient aqui).

Não depende de classes de infrastructure.

Regra do time: Service do domínio só fala com domain.models, domain.gateways e shared.domain (exceções/VO).

dtos/request e dtos/response

Responsabilidade

Modelos que representam o contrato da sua API, não o contrato da Meta.

Request: “o que o seu cliente manda”.

Response: “o que o seu cliente recebe”.

O que pode

Ter validações de formato (@NotBlank, etc).

Serem simples e estáveis.

O que não pode

Não conter anotações e campos específicos de integração (Meta).

Não conter regras (métodos com lógica).

Não vazar sua entity/banco.

Se você prefere, esses DTOs poderiam ficar em application/dto, mas manter em domain/dtos é aceitável se o time estiver alinhado e não misturar com infra.

gateways/WhatsAppGateway

Responsabilidade

É a porta (interface) que o domínio usa para “falar com WhatsApp”.

Define operações de alto nível do ponto de vista do domínio, por exemplo:

enviarMensagemTexto(Mensagem msg)

enviarTemplate(...)

marcarComoLida(...) etc.

O que pode

Ser orientado ao domínio e estável.

O que não pode

Não tem dependência de Meta, WebClient, DTO externo.

Não é “client HTTP disfarçado”.

Regra do time: O domínio depende do gateway; a infra implementa o gateway.

models/Mensagem

Responsabilidade

Representar o conceito de Mensagem no seu sistema (não “o JSON da Meta”).

Carregar significado: destinatário, tipo, conteúdo, metadados relevantes.

Idealmente, garantir consistência básica do objeto (ex.: não permitir texto vazio quando type = text).

O que pode

Ter métodos que expressam comportamento de domínio (“éTexto?”, “validarConteudo()”, etc).

Usar shared.domain.valueobjects (ex.: PhoneNumber, MessageId).

O que não pode

Não ter anotações de JPA (a menos que você decida que Mensagem também é entidade persistida, mas aí vira outro desenho).

Não conhecer Meta.

modules/whatsapp/infrastructure/meta
client/MetaWhatsAppClient

Responsabilidade

Implementar a comunicação HTTP com a Meta usando WebClient.

Saber endpoints, headers, token, retries, timeouts, serialização, status codes.

Receber DTOs externos (Meta request) e devolver DTOs externos (Meta response).

O que pode

Ter tratamento técnico: 429 retry, 5xx, timeouts, logging, correlation-id.

Ser bem testado com mocks.

O que não pode

Não deve receber Mensagem do domínio diretamente (isso acopla).

Não decide “o que enviar”, só “como enviar”.

adapter/WhatsAppGatewayMetaAdapter

Responsabilidade

Ser o adaptador que implementa WhatsAppGateway usando a Meta.

Traduz do mundo do domínio para o mundo da integração:

Domain model → mapper → DTO Meta request

chama MetaWhatsAppClient

DTO Meta response → mapper (se necessário) → retorno para o domínio

O que pode

Chamar MetaMensagemMapper.

Converter exceções técnicas em exceções de domínio (ex.: IntegracaoIndisponivelException).

O que não pode

Não deve conter regra de negócio (isso é do MensagemService).

Não deve “inventar” validações de conteúdo além do contrato externo.

Regra do time: Adapter é cola. Sem inteligência.

dto/request (Meta)

Responsabilidade

Representar exatamente o payload que a Meta exige.

Fiel ao contrato externo. Se a Meta usa messaging_product, o DTO reflete isso.

O que pode

Ser verboso.

Ter classes aninhadas conforme o JSON.

O que não pode

Não misturar com DTO da sua API.

Não usar nomes “bonitos” se isso atrapalhar aderência ao contrato.

mapper/MetaMensagemMapper

Responsabilidade

Converter entre seus modelos e os modelos da Meta:

Mensagem / MensagemTextoParaEnviar → MensagemDeTextoProntaParaEnviar

(e o inverso, se necessário para resposta)

O que pode

Definir constantes de contrato (ex.: messaging_product=whatsapp, recipient_type=individual) desde que isso seja parte do contrato externo.

Fazer conversões simples (enum ↔ string, etc).

O que não pode

Não acessar banco.

Não chamar service.

Não decidir fluxo (ex.: “se falhar tenta template”). Isso é do domínio.

modules/whatsapp/infrastructure/config
MetaWebClientConfig

Responsabilidade

Centralizar configuração técnica do WebClient:

baseUrl

timeouts

default headers

filtros (log, retry, tracing)

bean do WebClient (ou WebClient.Builder)

O que pode

Ler properties (application.yml).

Criar beans.

O que não pode

Não conhecer regras do domínio.

Não conhecer controllers.

Módulo chat (visão por alto, mantendo o mesmo padrão)
modules/chat/application/controllers

Responsabilidade

Endpoints da sua área de chat (interno), ex.: conversas, mensagens armazenadas, etc.

Entrada/saída via DTOs do chat.

modules/chat/domain/services

Responsabilidade

Regras de negócio do chat (armazenamento, conversa, status, leitura, etc).

modules/chat/domain/models

Responsabilidade

Modelos do domínio de chat (Conversa, MensagemChat, Participante, etc).

modules/chat/domain/repositories (interfaces)

Responsabilidade

Portas de persistência: MensagemRepository, ConversaRepository.

Interface orientada ao domínio, sem JPA.

modules/chat/infrastructure/persistence/repositories (JPA)

Responsabilidade

Implementação concreta usando Spring Data JPA.

Mapeamento entity↔tabela, queries, paginação.

Regra do time: Domain conhece interface de repository; infra implementa. Domain não importa Spring Data.

shared
shared/domain/exceptions

Responsabilidade

Exceções base e padronizadas que podem ser usadas em todos os módulos:

BusinessException

NotFoundException

ValidationException

IntegrationException

Regra

Exceções específicas de módulo podem existir no próprio módulo, mas herdam daqui.

shared/domain/valueobjects

Responsabilidade

Tipos que dão segurança e significado:

PhoneNumber, Cpf, Email, Periodo, Money, etc.

Validação e invariantes dentro do próprio VO.

shared/infrastructure/configurations

Responsabilidade

Configurações transversais: Jackson, timezone, OpenAPI, interceptors comuns, tracing, etc.

Contrato de trabalho da equipe (regras que eu cobraria em PR)

Controller não chama infra. Controller chama domínio (service/use case).

Domínio não depende de infrastructure. Nem por import.

Adapter implementa gateway e faz a cola com client + mapper.

Mapper só converte (sem regra, sem IO).

DTO da Meta nunca “vaza” para fora do módulo infrastructure/meta.

Shared é mínimo: só o que for realmente comum e estável.