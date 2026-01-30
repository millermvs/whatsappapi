package br.com.automica.api.whatsapp.modules.conversa.domain.entities;

import java.time.Instant;

import br.com.automica.api.whatsapp.modules.conversa.domain.enums.DirecaoMensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.enums.StatusMensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.enums.TipoMensagem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mensagem")
public class Mensagem {

    @Id
    private String idMensagem;

    @Enumerated(EnumType.STRING)
    @Column
    private TipoMensagem tipoMensagem;

    @Column(columnDefinition = "text")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversa_id", referencedColumnName = "idConversa")
    private Conversa conversa;

    @Enumerated(EnumType.STRING)
    @Column
    private DirecaoMensagem direcaoMensagem; // ENTRADA ou SAIDA

    @Enumerated(EnumType.STRING)
    @Column
    private StatusMensagem status; // ENVIADA, RECEBIDA, PROCESSADA

    @Column
    private Instant recebidaEm; // timestamp de quando a mensagem foi recebida pelo sistema

    @Column
    private Boolean processada; // indica se a mensagem foi processada pelo sistema

    @Column
    private Instant processadaEm; // timestamp de quando a mensagem foi processada pelo sistema

    @Column
    private Instant enviadaEm; // timestamp de quando a mensagem foi enviada ao WhatsApp

    @Column
    private Instant entregueEm; // timestamp de quando a mensagem foi entregue ao destinatário

    @Column
    private Instant visualizadaEm; // timestamp de quando a mensagem foi visualizada pelo destinatário

}
