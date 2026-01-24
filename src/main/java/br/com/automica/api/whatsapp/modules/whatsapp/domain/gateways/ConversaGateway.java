package br.com.automica.api.whatsapp.modules.whatsapp.domain.gateways;

import tools.jackson.databind.JsonNode;

public interface ConversaGateway {
    void filtrar(JsonNode payload); // faz o filtro do payload recebido do webhook
}
