package br.com.suporte.state.estado;

import br.com.suporte.state.model.Chamado;

public class AbertoState implements EstadoChamado {

    @Override
    public void iniciarAtendimento(Chamado chamado) {
        System.out.println("[Chamado #" + chamado.getId() + "]  Atendimento iniciado pelo agente: "
                + chamado.getAtendenteResponsavel() + ".");
        chamado.setEstado(new EmAtendimentoState());
    }

    @Override
    public void aguardarCliente(Chamado chamado) {
        throw new IllegalStateException(
                "Não é possível aguardar resposta do cliente: chamado ainda não foi atendido.");
    }

    @Override
    public void resolver(Chamado chamado) {
        throw new IllegalStateException(
                "Não é possível resolver: chamado ainda não foi atendido.");
    }

    @Override
    public void fechar(Chamado chamado) {
        throw new IllegalStateException(
                "Não é possível fechar: chamado ainda não foi atendido.");
    }

    @Override
    public void cancelar(Chamado chamado) {
        System.out.println("[Chamado #" + chamado.getId() + "]  Chamado cancelado pelo cliente antes do atendimento.");
        chamado.setEstado(new CanceladoState());
    }

    @Override
    public String getNome() {
        return "Aberto";
    }
}
