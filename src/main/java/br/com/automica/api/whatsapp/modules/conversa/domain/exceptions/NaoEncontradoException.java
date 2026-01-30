package br.com.automica.api.whatsapp.modules.conversa.domain.exceptions;

public class NaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NaoEncontradoException() {
        super("Não encontrado.");
    }

    public NaoEncontradoException(String mensagem) {
        super(mensagem);
    }

}
