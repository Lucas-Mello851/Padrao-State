package br.com.suporte.state.estado;

import br.com.suporte.state.model.Chamado;

public class FechadoState implements EstadoChamado {

    @Override
    public void iniciarAtendimento(Chamado chamado) {
        throw new IllegalStateException("Chamado já está fechado. Abra um novo chamado se necessário.");
    }

    @Override
    public void aguardarCliente(Chamado chamado) {
        throw new IllegalStateException("Chamado já está fechado.");
    }

    @Override
    public void resolver(Chamado chamado) {
        throw new IllegalStateException("Chamado já está fechado.");
    }

    @Override
    public void fechar(Chamado chamado) {
        throw new IllegalStateException("Chamado já está fechado.");
    }

    @Override
    public void cancelar(Chamado chamado) {
        throw new IllegalStateException("Não é possível cancelar um chamado já fechado.");
    }

    @Override
    public String getNome() {
        return "Fechado";
    }
}
