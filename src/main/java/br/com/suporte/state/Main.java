package br.com.suporte.state;

import br.com.suporte.state.model.Chamado;
import br.com.suporte.state.model.Prioridade;
import br.com.suporte.state.service.SistemaSuporte;

public class Main {

    public static void main(String[] args) {

        SistemaSuporte sistema = new SistemaSuporte();

        System.out.println("\n");
        System.out.println("   PADRÃO STATE — SISTEMA DE SUPORTE E CHAMADOS      ");
        System.out.println("\n");

        System.out.println(" Cenário 1: Fluxo Completo ");
        Chamado chamado1 = sistema.abrirChamado(
                "Sistema fora do ar",
                "O sistema de NF-e não responde desde as 08h.",
                "Carlos Mendes",
                Prioridade.CRITICA
        );
        chamado1.iniciarAtendimento("Ana Souza");
        chamado1.aguardarCliente();
        chamado1.iniciarAtendimento("Ana Souza");
        chamado1.resolver();
        chamado1.fechar();
        chamado1.exibirResumo();

        System.out.println("\n Cenário 2: Contestação da Resolução ");
        Chamado chamado2 = sistema.abrirChamado(
                "Erro ao emitir relatório",
                "O relatório mensal retorna erro 500 ao exportar.",
                "Fernanda Lima",
                Prioridade.ALTA
        );
        chamado2.iniciarAtendimento("Bruno Costa");
        chamado2.resolver();
        chamado2.iniciarAtendimento("Bruno Costa");
        chamado2.resolver();
        chamado2.fechar();
        chamado2.exibirResumo();

        System.out.println("\n Cenário 3: Cancelamento Durante Atendimento ");
        Chamado chamado3 = sistema.abrirChamado(
                "Dúvida sobre faturamento",
                "Preciso entender a cobrança da última fatura.",
                "Roberto Alves",
                Prioridade.BAIXA
        );
        chamado3.iniciarAtendimento("Paula Martins");
        chamado3.cancelar();
        chamado3.exibirResumo();

        System.out.println("\n Cenário 4: Transição Inválida ");
        Chamado chamado4 = sistema.abrirChamado(
                "Lentidão no sistema",
                "Páginas demoram mais de 30s para carregar.",
                "Juliana Ramos",
                Prioridade.MEDIA
        );
        try {
            chamado4.resolver();
        } catch (IllegalStateException e) {
            System.out.println("  Transição bloqueada: " + e.getMessage());
        }

        System.out.println("\n Resumo Geral dos Chamados ");
        sistema.listarTodos().forEach(c ->
            System.out.printf("  %-12s | %-30s | Estado: %s%n",
                    c.getId(), c.getTitulo(), c.getEstado().getNome())
        );
    }
}
