package br.com.suporte.state.service;

import br.com.suporte.state.model.Chamado;
import br.com.suporte.state.model.Prioridade;

import java.util.*;
import java.util.stream.Collectors;

public class SistemaSuporte {

    private final Map<String, Chamado> chamados = new LinkedHashMap<>();
    private int contadorId = 1;

    public Chamado abrirChamado(String titulo, String descricao,
                                String solicitante, Prioridade prioridade) {
        String id = String.format("TKT-%04d", contadorId++);
        Chamado chamado = new Chamado(id, titulo, descricao, solicitante, prioridade);
        chamados.put(id, chamado);
        System.out.println(" Novo chamado registrado: #" + id + " — " + titulo);
        return chamado;
    }

    public Optional<Chamado> buscarPorId(String id) {
        return Optional.ofNullable(chamados.get(id));
    }

    public List<Chamado> listarPorEstado(String nomeEstado) {
        return chamados.values().stream()
                .filter(c -> c.getEstado().getNome().equalsIgnoreCase(nomeEstado))
                .collect(Collectors.toList());
    }

    public Collection<Chamado> listarTodos() {
        return Collections.unmodifiableCollection(chamados.values());
    }

    public void exibirTodos() {
        if (chamados.isEmpty()) {
            System.out.println("Nenhum chamado registrado.");
            return;
        }
        chamados.values().forEach(Chamado::exibirResumo);
    }
}
