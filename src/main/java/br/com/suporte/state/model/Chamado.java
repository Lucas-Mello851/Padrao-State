package br.com.suporte.state.model;

import br.com.suporte.state.estado.AbertoState;
import br.com.suporte.state.estado.EstadoChamado;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chamado {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final String id;
    private final String titulo;
    private final String descricao;
    private final String solicitante;
    private final Prioridade prioridade;
    private final LocalDateTime dataAbertura;

    private String atendenteResponsavel;
    private EstadoChamado estado;
    private final List<String> historico;

    public Chamado(String id, String titulo, String descricao,
                   String solicitante, Prioridade prioridade) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.solicitante = solicitante;
        this.prioridade = prioridade;
        this.dataAbertura = LocalDateTime.now();
        this.estado = new AbertoState();
        this.historico = new ArrayList<>();
        registrarHistorico("Chamado aberto por " + solicitante + " | Prioridade: " + prioridade);
    }

    public void iniciarAtendimento(String atendente) {
        this.atendenteResponsavel = atendente;
        estado.iniciarAtendimento(this);
        registrarHistorico("Atendimento iniciado por: " + atendente);
    }

    public void aguardarCliente() {
        estado.aguardarCliente(this);
        registrarHistorico("Aguardando resposta do cliente.");
    }

    public void resolver() {
        estado.resolver(this);
        registrarHistorico("Chamado marcado como resolvido.");
    }

    public void fechar() {
        estado.fechar(this);
        registrarHistorico("Chamado fechado.");
    }

    public void cancelar() {
        estado.cancelar(this);
        registrarHistorico("Chamado cancelado.");
    }

    private void registrarHistorico(String evento) {
        historico.add("[" + LocalDateTime.now().format(FORMATTER) + "] " + evento);
    }

    public void exibirResumo() {
        System.out.println("=".repeat(60));
        System.out.println("  CHAMADO #" + id);
        System.out.println("=".repeat(60));
        System.out.println("  Título     : " + titulo);
        System.out.println("  Solicitante: " + solicitante);
        System.out.println("  Prioridade : " + prioridade);
        System.out.println("  Abertura   : " + dataAbertura.format(FORMATTER));
        System.out.println("  Atendente  : " + (atendenteResponsavel != null ? atendenteResponsavel : "—"));
        System.out.println("  Estado     : " + estado.getNome());
        System.out.println("  Histórico  :");
        historico.forEach(h -> System.out.println("    " + h));
        System.out.println("=".repeat(60));
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getSolicitante() { return solicitante; }
    public Prioridade getPrioridade() { return prioridade; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public String getAtendenteResponsavel() { return atendenteResponsavel; }
    public EstadoChamado getEstado() { return estado; }
    public List<String> getHistorico() { return Collections.unmodifiableList(historico); }

    public void setEstado(EstadoChamado novoEstado) {
        this.estado = novoEstado;
    }
}
