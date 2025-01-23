package yan.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import dao.BancoDados;
import dao.EventoManagerDao;
import dao.UserManagerDao;
import entities.Administrador;
import entities.Evento;
import entities.EventoCategoria;
import entities.EventoStatus;
import entities.Participante;

class EventoManagerDaoTeste {

    private static Administrador adminTeste;
    private static Participante participanteTeste;
    private static Evento eventoTeste;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        BancoDados.conectar();
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        BancoDados.desconectar();
    }

    @BeforeEach
    void setUp() throws Exception {
    	criarUsuariosTeste();
        criarEventoTeste();
    }

    @AfterEach
    void tearDown() throws Exception {
        limparDadosTeste();
    }

    // ==========================|| TESTES ||========================== //
    @Test
    void testAdicionarEventoValido() throws Exception {
        boolean resultado = EventoManagerDao.adicionar(eventoTeste);
        
        assertTrue(resultado, "Evento válido deveria ser adicionado");
        
        Evento eventoRecuperado = EventoManagerDao.getEventoPorId(eventoTeste.getId());
        
        assertNotNull(eventoRecuperado, "Evento não foi encontrado após inserção");
    }

    @Disabled
    void testAdicionarEventoSemOrganizadores() {
        eventoTeste.getOrganizadores().clear();
        eventoTeste.removerOrganizador(adminTeste.getId());
        
        assertThrows(SQLException.class, () -> {
            EventoManagerDao.adicionar(eventoTeste);
        }, "Deveria falhar ao adicionar evento sem organizadores");
        
        eventoTeste.adicionarOrganizador(adminTeste);
    }
    
    @Test
    void testExcluirEvento() throws Exception {
        // Adiciona o evento (o ID é definido automaticamente)
        boolean porra = EventoManagerDao.adicionar(eventoTeste);
        System.out.println("CARAIO: "+ porra);
        // Recupera o ID atualizado
        int eventoId = eventoTeste.getId();
        
        // Exclui o evento
        boolean resultado = EventoManagerDao.excluir(eventoId);
        assertTrue(resultado, "Exclusão deveria ser bem-sucedida");
        
        // Verifica se o evento foi removido
        Evento eventoExcluido = EventoManagerDao.getEventoPorId(eventoId);
        assertNull(eventoExcluido, "Evento deveria ser removido");
    }
    
    @Test
    void testEditarEvento() throws Exception {
        EventoManagerDao.adicionar(eventoTeste);
        
        eventoTeste.setTitulo("Novo Título Editado");
        boolean resultado = EventoManagerDao.editar(eventoTeste);
        
        assertTrue(resultado, "Edição deveria ser bem-sucedida");
        Evento eventoEditado = EventoManagerDao.getEventoPorId(eventoTeste.getId());
        assertEquals("Novo Título Editado", eventoEditado.getTitulo());
    }

    // ==========================|| MÉTODOS AUXILIARES ||========================== //
    private static void criarUsuariosTeste() throws Exception {
        // Cria administrador de teste
        adminTeste = new Administrador(
            null, 
            "Admin Evento Teste",
            "senha123",
            "admin.evento@teste.com",
            "Cargo Teste",
            new Date(System.currentTimeMillis())
        );
        UserManagerDao.cadastrar(adminTeste);

        // Cria participante de teste
        participanteTeste = new Participante(
            null,
            "Participante Evento Teste",
            "senha456",
            "participante.evento@teste.com",
            "104.559.349-40", // CPF válido
            new Date(System.currentTimeMillis())
        );
        UserManagerDao.cadastrar(participanteTeste);
    }

    private void criarEventoTeste() {
        HashMap<Integer, Administrador> organizadores = new HashMap<>();
        organizadores.put(adminTeste.getId(), adminTeste);

        eventoTeste = new Evento(
            null,
            "Evento de Teste",
            "Descrição do Evento de Teste",
            Date.valueOf(LocalDate.now().plusDays(7)),
            Time.valueOf(LocalTime.of(14, 0)),
            Duration.ofHours(2),
            "Auditório Principal",
            false,
            100,
            EventoCategoria.CONFERENCIA,
            EventoStatus.ABERTO,
            0.0,
            organizadores,
            new HashMap<>()
        );
    }

    private void limparDadosTeste() throws SQLException {
        try {
            // 1. Exclui o evento de teste (se existir)
            if (eventoTeste.getId() != null) {
                EventoManagerDao.excluir(eventoTeste.getId());
            }

            // 2. Exclui os usuários de teste usando UserManagerDao
            UserManagerDao.apagarUsuarioPorEmail("admin.evento@teste.com");
            UserManagerDao.apagarUsuarioPorEmail("participante.evento@teste.com");

        } catch (SQLException e) {
            System.err.println("Erro na limpeza de dados: " + e.getMessage());
            throw e; // Propaga para falhar o teste se houver erro crítico
        }
    }
    
}