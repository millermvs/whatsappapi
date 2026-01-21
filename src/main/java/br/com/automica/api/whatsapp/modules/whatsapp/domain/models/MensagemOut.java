package br.com.automica.api.whatsapp.modules.whatsapp.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MensagemOut {

    private String destinatario;

	private String tipo;

	private String conteudo;

	private Boolean previewUrl;
}
