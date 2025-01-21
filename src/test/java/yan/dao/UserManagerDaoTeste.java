package yan.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dao.BancoDados;
import dao.UserManagerDao;
import entities.Administrador;
import entities.Participante;
import entities.User;
import entities.UsuarioTipo;

class UserManagerDaoTeste {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        BancoDados.conectar();
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    	BancoDados.desconectar();
    }

    @Test
    void testCadastroEExclusaoDeAdministrador() throws Exception {
        // Criação do administrador
        Administrador admin = new Administrador(
            0, // O ID será gerado automaticamente
            "Admin Teste",
            "senha123",
            "adminteste@example.com",
            "Gerente",
            new Date(System.currentTimeMillis())
        );

        // Testa o cadastro
        boolean cadastrado = UserManagerDao.cadastrar(admin);
        assertTrue(cadastrado, "Administrador não foi cadastrado corretamente");

        // Verifica se o administrador foi cadastrado
        User userBuscado = UserManagerDao.getUserPorEmail("adminteste@example.com");
        assertNotNull(userBuscado, "Administrador não foi encontrado no banco de dados");
        assertEquals("Admin Teste", userBuscado.getNome(), "Nome do administrador não corresponde");
        assertEquals(UsuarioTipo.ADMIN, userBuscado.getTipo(), "Tipo do usuário não é ADMIN");

        // Testa a exclusão
        boolean excluido = UserManagerDao.apagarUsuarioPorEmail("adminteste@example.com");
        assertTrue(excluido, "Administrador não foi excluído corretamente");
    }

    @Test
    void testCadastroEExclusaoDeParticipante() throws Exception {
        // Criação do participante
        Participante participante = new Participante(
            0, // O ID será gerado automaticamente
            "Participante Teste",
            "senha123",
            "participante@example.com",
            "104.559.349-40",
            new Date(System.currentTimeMillis())
        );

        // Testa o cadastro
        boolean cadastrado = UserManagerDao.cadastrar(participante);
        assertTrue(cadastrado, "Participante não foi cadastrado corretamente");

        // Verifica se o participante foi cadastrado
        User userBuscado = UserManagerDao.getUserPorEmail("participante@example.com");
        assertNotNull(userBuscado, "Participante não foi encontrado no banco de dados");
        assertEquals("Participante Teste", userBuscado.getNome(), "Nome do participante não corresponde");
        assertEquals(UsuarioTipo.DEFAULT, userBuscado.getTipo(), "Tipo do usuário não é DEFAULT");

        // Testa a exclusão
        boolean excluido = UserManagerDao.apagarUsuarioPorEmail("participante@example.com");
        assertTrue(excluido, "Participante não foi excluído corretamente");
    }

    @Test
    void testUsuariosMaiorQue() throws Exception {
        // Quantidade inicial de usuários
        boolean resultado = UserManagerDao.UsuariosMaiorQue(0);
        assertTrue(resultado, "A verificação de usuários não foi realizada corretamente");
    }

    @Test
    void testParticipanteCPFInvalido() {
        // Tentativa de criação de um participante com CPF inválido
        Exception exception = assertThrows(Exception.class, () -> {
            new Participante(
                0, // ID automático
                "Participante Teste",
                "senha123",
                "participante@example.com",
                "105.559.349-40", // CPF inválido
                new Date(System.currentTimeMillis())
            );
        });

        // Verifica a mensagem de erro da exceção
        assertEquals("CPF INVÁLIDO", exception.getMessage(), "A mensagem de exceção não corresponde.");
    }

    
}
