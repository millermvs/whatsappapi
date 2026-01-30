package br.com.automica.api.whatsapp.modules.conversa.domain.gateways;

import tools.jackson.databind.JsonNode;

public interface PayloadGateway {
    String filtrarPayload(JsonNode payload);
}
