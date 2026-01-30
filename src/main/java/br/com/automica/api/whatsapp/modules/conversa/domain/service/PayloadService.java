package br.com.automica.api.whatsapp.modules.conversa.domain.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.conversa.ConversaRequestDto;
import br.com.automica.api.whatsapp.modules.conversa.domain.enums.StatusMensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.gateways.PayloadGateway;
import br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories.ConversaRepository;
import br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories.MensagemRepository;
import tools.jackson.databind.JsonNode;

@Service
public class PayloadService implements PayloadGateway {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private ConversaService conversaService;

    @Autowired
    private ConversaRepository conversaRepository;

    @Override
    public String filtrarPayload(JsonNode payload) {

        // entry[]
        for (JsonNode entry : payload.path("entry")) {

            String wabaId = entry.path("id").stringValue(null);

            // changes[]
            for (JsonNode change : entry.path("changes")) {

                JsonNode value = change.path("value");

                JsonNode metadata = value.path("metadata");
                String phoneNumberId = metadata.path("phone_number_id").stringValue(null);
                String displayPhoneNumber = metadata.path("display_phone_number").stringValue(null);

                // =========================
                // 1) MENSAGENS RECEBIDAS
                // =========================
                if (value.has("messages")) {

                    // contacts[0] (quando existir)
                    JsonNode contact0 = value.path("contacts").path(0);
                    String waId = contact0.path("wa_id").stringValue(null);

                    // messages[]
                    for (JsonNode message : value.path("messages")) {

                        String messageId = message.path("id").stringValue(null);
                        String typeMessage = message.path("type").stringValue(null);

                        String bodyText = null;
                        if ("text".equals(typeMessage)) {
                            bodyText = message.path("text").path("body").stringValue(null);
                        }

                        String timestampStr = message.path("timestamp").stringValue(null);
                        Instant messageAt = null;
                        if (timestampStr != null) {
                            messageAt = Instant.ofEpochSecond(Long.parseLong(timestampStr));
                        }

                        var conversaRequestDto = new ConversaRequestDto();
                        conversaRequestDto.setWaId(waId);
                        conversaRequestDto.setPhoneNumberId(phoneNumberId);
                        conversaRequestDto.setDisplayPhoneNumber(displayPhoneNumber);
                        conversaRequestDto.setLastMessageAt(messageAt);
                        conversaRequestDto.setLastMessageId(messageId);
                        conversaRequestDto.setWabaId(wabaId);
                        conversaRequestDto.setTypeMessage(typeMessage);
                        conversaRequestDto.setBodyText(bodyText);
                        conversaRequestDto.setIdMensagem(messageId);

                        conversaService.criarConversa(conversaRequestDto);
                    }
                }

                // =========================
                // 2) STATUS DE MENSAGENS ENVIADAS
                // =========================
                if (value.has("statuses")) {

                    // statuses[]
                    for (JsonNode statusNode : value.path("statuses")) {

                        String wamid = statusNode.path("id").stringValue(null);
                        String status = statusNode.path("status").stringValue(null);
                        String timestampStr = statusNode.path("timestamp").stringValue(null);

                        if (wamid == null || status == null || timestampStr == null) {
                            continue;
                        }

                        Instant when = Instant.ofEpochSecond(Long.parseLong(timestampStr));

                        var mensagemOpt = mensagemRepository.findById(wamid);

                        if (mensagemOpt.isEmpty()) {
                            // webhook pode chegar antes do update de retorno da Meta, ou mensagem pode não
                            // ter sido persistida ainda
                            continue;
                        }

                        var mensagem = mensagemOpt.get();

                        var conversaOpt = conversaRepository.findById(mensagem.getConversa().getIdConversa());
                        var conversa = conversaOpt.get();

                        switch (status) {
                            case "sent":
                                mensagem.setStatus(StatusMensagem.ENVIADA);
                                mensagem.setEnviadaEm(when);
                                mensagemRepository.save(mensagem);
                                break;

                            case "delivered":
                                mensagem.setStatus(StatusMensagem.ENTREGUE);
                                mensagem.setEntregueEm(when);
                                mensagemRepository.save(mensagem);
                                conversa.setLastMessageAt(when);
                                conversa.setLastMessageId(wamid);
                                conversaRepository.save(conversa);
                                break;

                            case "read":
                                mensagem.setStatus(StatusMensagem.VISUALIZADA);
                                mensagem.setVisualizadaEm(when);
                                mensagemRepository.save(mensagem);
                                break;

                            default:
                                // outros status possíveis (failed, deleted, etc) — tratar depois
                                break;
                        }
                    }
                }
            }
        }

        return "Filtrado com sucesso!";
    }

}
