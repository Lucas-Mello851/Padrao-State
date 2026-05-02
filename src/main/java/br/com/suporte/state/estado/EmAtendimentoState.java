package br.com.suporte.state.estado;

import br.com.suporte.state.model.Chamado;

public class EmAtendimentoState implements EstadoChamado {

    @Override
    public void iniciarAtendimento(Chamado chamado) {
        throw new IllegalStateException(
                "Chamado já está em atendimento por: " + chamado.getAtendenteResponsavel() + ".");
    }

    @Override
    public void aguardarCliente(Chamado chamado) {
        System.out.println("[Chamado #" + chamado.getId() + "]  Resposta enviada ao cliente. Aguardando retorno.");
        chamado.setEstado(new AguardandoClienteState());
    }

    @Override
    public void resolver(Chamado chamado) {
        System.out.println("[Chamado #" + chamado.getId() + "]  Chamado marcado como resolvido pelo agente.");
        chamado.setEstado(new ResolvidoState());
    }

    @Override
    public void fechar(Chamado chamado) {
        throw new IllegalStateException(
                "Não é possível fechar diretamente: o chamado precisa ser resolvido antes.");
    }

    @Override
    public void cancelar(Chamado chamado) {
        System.out.println("[Chamado #" + chamado.getId() + "]  Chamado cancelado durante o atendimento.");
        chamado.setEstado(new CanceladoState());
    }

    @Override
    public String getNome() {
        return "Em Atendimento";
    }
}
