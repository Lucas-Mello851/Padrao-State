package br.com.suporte.state.estado;

import br.com.suporte.state.model.Chamado;

public class AguardandoClienteState implements EstadoChamado {

    @Override
    public void iniciarAtendimento(Chamado chamado) {
        System.out.println("[Chamado #" + chamado.getId() + "]  Cliente respondeu. Retomando atendimento.");
        chamado.setEstado(new EmAtendimentoState());
    }

    @Override
    public void aguardarCliente(Chamado chamado) {
        throw new IllegalStateException(
                "Chamado já está aguardando resposta do cliente.");
    }

    @Override
    public void resolver(Chamado chamado) {
        System.out.println("[Chamado #" + chamado.getId()
                + "]  Chamado resolvido administrativamente por inatividade do cliente.");
        chamado.setEstado(new ResolvidoState());
    }

    @Override
    public void fechar(Chamado chamado) {
        throw new IllegalStateException(
                "Não é possível fechar: aguardando retorno do cliente. Resolva antes.");
    }

    @Override
    public void cancelar(Chamado chamado) {
        System.out.println("[Chamado #" + chamado.getId() + "]  Chamado cancelado enquanto aguardava o cliente.");
        chamado.setEstado(new CanceladoState());
    }

    @Override
    public String getNome() {
        return "Aguardando Cliente";
    }
}
