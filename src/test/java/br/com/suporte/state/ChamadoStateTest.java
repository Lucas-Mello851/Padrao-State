package br.com.suporte.state;

import br.com.suporte.state.model.Chamado;
import br.com.suporte.state.model.Prioridade;
import br.com.suporte.state.service.SistemaSuporte;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Padrão State — Sistema de Suporte e Chamados")
class ChamadoStateTest {

    private Chamado chamado;
    private SistemaSuporte sistema;

    @BeforeEach
    void setUp() {
        chamado = new Chamado("TKT-0001", "Erro de login",
                "Usuário não consegue autenticar no portal.",
                "João Silva", Prioridade.ALTA);
        sistema = new SistemaSuporte();
    }

    @Nested
    @DisplayName("1. Estado inicial ao criar chamado")
    class EstadoInicial {

        @Test
        @DisplayName("Chamado recém-criado deve estar no estado 'Aberto'")
        void deveCriarChamadoNoEstadoAberto() {
            assertEquals("Aberto", chamado.getEstado().getNome());
        }

        @Test
        @DisplayName("Chamado recém-criado não deve ter atendente responsável")
        void naoDeveTerAtendente() {
            assertNull(chamado.getAtendenteResponsavel());
        }

        @Test
        @DisplayName("Histórico deve conter exatamente um registro ao criar")
        void historicoPossuiUmRegistroInicial() {
            assertEquals(1, chamado.getHistorico().size());
        }
    }

    @Nested
    @DisplayName("2. Fluxo feliz completo")
    class FluxoFeliz {

        @Test
        @DisplayName("Aberto → Em Atendimento")
        void deveTransicionarParaEmAtendimento() {
            chamado.iniciarAtendimento("Maria Oliveira");
            assertEquals("Em Atendimento", chamado.getEstado().getNome());
            assertEquals("Maria Oliveira", chamado.getAtendenteResponsavel());
        }

        @Test
        @DisplayName("Em Atendimento → Aguardando Cliente")
        void deveTransicionarParaAguardandoCliente() {
            chamado.iniciarAtendimento("Maria Oliveira");
            chamado.aguardarCliente();
            assertEquals("Aguardando Cliente", chamado.getEstado().getNome());
        }

        @Test
        @DisplayName("Aguardando Cliente → Em Atendimento (cliente respondeu)")
        void clienteResponseVoltaParaEmAtendimento() {
            chamado.iniciarAtendimento("Maria Oliveira");
            chamado.aguardarCliente();
            chamado.iniciarAtendimento("Maria Oliveira");
            assertEquals("Em Atendimento", chamado.getEstado().getNome());
        }

        @Test
        @DisplayName("Em Atendimento → Resolvido")
        void deveTransicionarParaResolvido() {
            chamado.iniciarAtendimento("Pedro Santos");
            chamado.resolver();
            assertEquals("Resolvido", chamado.getEstado().getNome());
        }

        @Test
        @DisplayName("Resolvido → Fechado")
        void deveTransicionarParaFechado() {
            chamado.iniciarAtendimento("Pedro Santos");
            chamado.resolver();
            chamado.fechar();
            assertEquals("Fechado", chamado.getEstado().getNome());
        }

        @Test
        @DisplayName("Fluxo completo: Aberto → Fechado")
        void fluxoCompletoAbertoFechado() {
            chamado.iniciarAtendimento("Lucas Ferreira");
            chamado.aguardarCliente();
            chamado.iniciarAtendimento("Lucas Ferreira");
            chamado.resolver();
            chamado.fechar();
            assertEquals("Fechado", chamado.getEstado().getNome());
        }

        @Test
        @DisplayName("Aguardando Cliente → Resolvido (inatividade do cliente)")
        void aguardandoClienteParaResolvido() {
            chamado.iniciarAtendimento("Ana Lima");
            chamado.aguardarCliente();
            chamado.resolver();
            assertEquals("Resolvido", chamado.getEstado().getNome());
        }
    }

    @Nested
    @DisplayName("3. Fluxo de reprocessamento")
    class Reprocessamento {

        @Test
        @DisplayName("Cliente contesta resolução: Resolvido → Em Atendimento")
        void clienteContestaResolucao() {
            chamado.iniciarAtendimento("Carlos Neves");
            chamado.resolver();
            chamado.iniciarAtendimento("Carlos Neves"); // contestação
            assertEquals("Em Atendimento", chamado.getEstado().getNome());
        }

        @Test
        @DisplayName("Fluxo com dupla resolução após contestação")
        void duplaResolucaoAposContestar() {
            chamado.iniciarAtendimento("Carlos Neves");
            chamado.resolver();
            chamado.iniciarAtendimento("Carlos Neves");
            chamado.resolver();
            chamado.fechar();
            assertEquals("Fechado", chamado.getEstado().getNome());
        }
    }

    @Nested
    @DisplayName("4. Cancelamento em estados permitidos")
    class Cancelamento {

        @Test
        @DisplayName("Cancelar no estado Aberto")
        void cancelarEstadoAberto() {
            chamado.cancelar();
            assertEquals("Cancelado", chamado.getEstado().getNome());
        }

        @Test
        @DisplayName("Cancelar no estado Em Atendimento")
        void cancelarEmAtendimento() {
            chamado.iniciarAtendimento("Bia Costa");
            chamado.cancelar();
            assertEquals("Cancelado", chamado.getEstado().getNome());
        }

        @Test
        @DisplayName("Cancelar no estado Aguardando Cliente")
        void cancelarAguardandoCliente() {
            chamado.iniciarAtendimento("Bia Costa");
            chamado.aguardarCliente();
            chamado.cancelar();
            assertEquals("Cancelado", chamado.getEstado().getNome());
        }
    }

    @Nested
    @DisplayName("5. Transições inválidas no estado Aberto")
    class TransicoesInvalidasAberto {

        @Test
        @DisplayName("Não deve aguardar cliente no estado Aberto")
        void naoDeveAguardarClienteAberto() {
            assertThrows(IllegalStateException.class, chamado::aguardarCliente);
        }

        @Test
        @DisplayName("Não deve resolver no estado Aberto")
        void naoDeveResolverAberto() {
            assertThrows(IllegalStateException.class, chamado::resolver);
        }

        @Test
        @DisplayName("Não deve fechar no estado Aberto")
        void naoDeveFecharAberto() {
            assertThrows(IllegalStateException.class, chamado::fechar);
        }
    }

    @Nested
    @DisplayName("6. Estado Fechado — imutável")
    class EstadoFechadoImutavel {

        @BeforeEach
        void fecharChamado() {
            chamado.iniciarAtendimento("Rafa Pinto");
            chamado.resolver();
            chamado.fechar();
        }

        @Test
        @DisplayName("Não deve iniciar atendimento num chamado fechado")
        void naoDeveIniciarAtendimento() {
            assertThrows(IllegalStateException.class,
                    () -> chamado.iniciarAtendimento("Alguém"));
        }

        @Test
        @DisplayName("Não deve cancelar um chamado fechado")
        void naoDeveCancelar() {
            assertThrows(IllegalStateException.class, chamado::cancelar);
        }

        @Test
        @DisplayName("Não deve resolver um chamado fechado")
        void naoDeveResolver() {
            assertThrows(IllegalStateException.class, chamado::resolver);
        }

        @Test
        @DisplayName("Não deve fechar um chamado já fechado")
        void naoDeveFecharNovamente() {
            assertThrows(IllegalStateException.class, chamado::fechar);
        }
    }

    @Nested
    @DisplayName("7. Estado Cancelado — imutável")
    class EstadoCanceladoImutavel {

        @BeforeEach
        void cancelarChamado() {
            chamado.cancelar();
        }

        @Test
        @DisplayName("Não deve iniciar atendimento num chamado cancelado")
        void naoDeveIniciarAtendimento() {
            assertThrows(IllegalStateException.class,
                    () -> chamado.iniciarAtendimento("Alguém"));
        }

        @Test
        @DisplayName("Não deve cancelar novamente")
        void naoDeveCancelarNovamente() {
            assertThrows(IllegalStateException.class, chamado::cancelar);
        }

        @Test
        @DisplayName("Não deve resolver um chamado cancelado")
        void naoDeveResolver() {
            assertThrows(IllegalStateException.class, chamado::resolver);
        }
    }

    @Nested
    @DisplayName("8. Estado Resolvido — restrições")
    class EstadoResolvido {

        @BeforeEach
        void resolverChamado() {
            chamado.iniciarAtendimento("Teste");
            chamado.resolver();
        }

        @Test
        @DisplayName("Não deve aguardar cliente no estado Resolvido")
        void naoDeveAguardarCliente() {
            assertThrows(IllegalStateException.class, chamado::aguardarCliente);
        }

        @Test
        @DisplayName("Não deve cancelar no estado Resolvido")
        void naoDeveCancelar() {
            assertThrows(IllegalStateException.class, chamado::cancelar);
        }

        @Test
        @DisplayName("Não deve resolver novamente")
        void naoDeveResolverNovamente() {
            assertThrows(IllegalStateException.class, chamado::resolver);
        }
    }

    @Nested
    @DisplayName("9. SistemaSuporte — abertura e consulta")
    class SistemaSuporteTest {

        @Test
        @DisplayName("Deve abrir um chamado e retorná-lo com ID gerado")
        void deveAbrirChamado() {
            Chamado c = sistema.abrirChamado("Falha na impressão",
                    "Impressora offline.", "Tânia", Prioridade.MEDIA);
            assertNotNull(c.getId());
            assertTrue(c.getId().startsWith("TKT-"));
            assertEquals("Aberto", c.getEstado().getNome());
        }

        @Test
        @DisplayName("Deve encontrar chamado por ID existente")
        void deveBuscarPorIdExistente() {
            Chamado c = sistema.abrirChamado("Teste", "Desc", "User", Prioridade.BAIXA);
            Optional<Chamado> resultado = sistema.buscarPorId(c.getId());
            assertTrue(resultado.isPresent());
            assertEquals(c.getId(), resultado.get().getId());
        }

        @Test
        @DisplayName("Deve retornar Optional vazio para ID inexistente")
        void deveRetornarVazioParaIdInexistente() {
            Optional<Chamado> resultado = sistema.buscarPorId("TKT-9999");
            assertFalse(resultado.isPresent());
        }

        @Test
        @DisplayName("Deve filtrar chamados por estado")
        void deveFiltrarPorEstado() {
            sistema.abrirChamado("A", "desc", "user1", Prioridade.BAIXA);
            Chamado c2 = sistema.abrirChamado("B", "desc", "user2", Prioridade.MEDIA);
            c2.iniciarAtendimento("Agente");

            List<Chamado> abertos = sistema.listarPorEstado("Aberto");
            List<Chamado> emAtendimento = sistema.listarPorEstado("Em Atendimento");

            assertEquals(1, abertos.size());
            assertEquals(1, emAtendimento.size());
        }

        @ParameterizedTest
        @ValueSource(strings = {"CRITICA", "ALTA", "MEDIA", "BAIXA"})
        @DisplayName("Deve abrir chamados com todas as prioridades")
        void deveAbrirComDiferentesPrioridades(String prioridadeStr) {
            Prioridade p = Prioridade.valueOf(prioridadeStr);
            Chamado c = sistema.abrirChamado("Título", "Desc", "User", p);
            assertEquals(p, c.getPrioridade());
            assertEquals("Aberto", c.getEstado().getNome());
        }
    }
}
