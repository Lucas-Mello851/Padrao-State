package br.com.suporte.state.estado;

import br.com.suporte.state.model.Chamado;

public class ResolvidoState implements EstadoChamado {

    @Override
    public void iniciarAtendimento(Chamado chamado) {
        System.out.println("[Chamado #" + chamado.getId()
                + "]  Cliente contestou a resolução. Chamado reaberto para novo atendimento.");
        chamado.setEstado(new EmAtendimentoState());
    }

    @Override
    public void aguardarCliente(Chamado chamado) {
        throw new IllegalStateException(
                "Chamado já foi resolvido. Aguardando apenas o fechamento.");
    }

    @Override
    public void resolver(Chamado chamado) {
        throw new IllegalStateException(
                "Chamado já está marcado como resolvido.");
    }

    @Override
    public void fechar(Chamado chamado) {
        System.out.println("[Chamado #" + chamado.getId()
                + "]  Chamado fechado com sucesso. Obrigado pelo contato!");
        chamado.setEstado(new FechadoState());
    }

    @Override
    public void cancelar(Chamado chamado) {
        throw new IllegalStateException(
                "Não é possível cancelar um chamado já resolvido. Use fechar() para encerrá-lo.");
    }

    @Override
    public String getNome() {
        return "Resolvido";
    }
}
