package br.com.automica.api.whatsapp.modules.whatsapp.domain.models;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MensagemIn {

    /**
     * ID único da mensagem na Meta (wamid...)
     * Usado para idempotência (evitar duplicação)
     */
    @Id
    private String idMensagemMeta;

    /**
     * Número de quem enviou a mensagem
     */
    @Column
    private String numeroRemetente;

    /**
     * Número do WhatsApp Business que recebeu a mensagem
     */
    @Column
    private String numeroDestino;

    /**
     * Tipo da mensagem (text, image, audio, etc.)
     */
    @Column
    private String tipoMensagem;

    /**
     * Conteúdo textual da mensagem (quando tipo = text)
     */
    @Column
    private String conteudoTexto;

    /**
     * Momento em que a mensagem foi enviada (timestamp da Meta)
     */
    @Column
    private Instant dataHoraMensagem;
}

