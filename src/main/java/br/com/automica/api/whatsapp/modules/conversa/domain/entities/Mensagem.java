package br.com.automica.api.whatsapp.modules.conversa.domain.entities;

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


}
