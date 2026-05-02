package br.com.suporte.state.estado;

import br.com.suporte.state.model.Chamado;

public interface EstadoChamado {

    void iniciarAtendimento(Chamado chamado);

    void aguardarCliente(Chamado chamado);

    void resolver(Chamado chamado);

    void fechar(Chamado chamado);

    void cancelar(Chamado chamado);

    String getNome();
}
