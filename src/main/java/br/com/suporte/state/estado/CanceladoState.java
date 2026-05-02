package br.com.suporte.state.estado;

import br.com.suporte.state.model.Chamado;

public class CanceladoState implements EstadoChamado {

    @Override
    public void iniciarAtendimento(Chamado chamado) {
        throw new IllegalStateException("Chamado está cancelado. Abra um novo chamado.");
    }

    @Override
    public void aguardarCliente(Chamado chamado) {
        throw new IllegalStateException("Chamado está cancelado.");
    }

    @Override
    public void resolver(Chamado chamado) {
        throw new IllegalStateException("Chamado está cancelado.");
    }

    @Override
    public void fechar(Chamado chamado) {
        throw new IllegalStateException("Chamado está cancelado.");
    }

    @Override
    public void cancelar(Chamado chamado) {
        throw new IllegalStateException("Chamado já está cancelado.");
    }

    @Override
    public String getNome() {
        return "Cancelado";
    }
}
