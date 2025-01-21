package yan.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dao.BancoDados;

class BancoDadosTeste {

    private Connection conexao;

    @BeforeEach
    void setUp() throws Exception {
        conexao = null;
    }

    @AfterEach
    void tearDown() throws Exception {
        if (conexao != null) {
            BancoDados.desconectar();
        }
    }

    @Test
    void testConectar() throws SQLException, IOException {
        conexao = BancoDados.conectar();
        assertNotNull(conexao, "A conexão com o banco de dados não deve ser nula");
        assertFalse(conexao.isClosed(), "A conexão deve estar aberta");
    }

    @Test
    void testDesconectar() throws SQLException, IOException {
        conexao = BancoDados.conectar();
        assertNotNull(conexao, "A conexão com o banco de dados não deve ser nula antes de desconectar");

        Connection desconexao = BancoDados.desconectar();
        assertNull(desconexao, "A conexão deve ser nula após chamar o método desconectar");
    }

    @Test
    void testCarregarPropriedades() throws IOException {
        Properties propriedades = BancoDados.carregarPropriedades();
        assertNotNull(propriedades, "As propriedades não devem ser nulas");
        assertTrue(propriedades.containsKey("dburl"), "As propriedades devem conter a chave 'dburl'");
    }

    @Test
    void testFinalizarStatement() throws SQLException, IOException {
        Statement statement = null;
        try {
            conexao = BancoDados.conectar();
            statement = conexao.createStatement();
            BancoDados.finalizarStatement(statement);
            assertTrue(statement.isClosed(), "O Statement deve ser fechado corretamente");
        } finally {
            BancoDados.finalizarStatement(statement);
        }
    }

    @Test
    void testFinalizarResultSet() throws SQLException, IOException {
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            conexao = BancoDados.conectar();
            statement = conexao.createStatement();
            resultSet = statement.executeQuery("SELECT 1");

            BancoDados.finalizarResultSet(resultSet);
            assertTrue(resultSet.isClosed(), "O ResultSet deve ser fechado corretamente");
        } finally {
            BancoDados.finalizarResultSet(resultSet);
            BancoDados.finalizarStatement(statement);
        }
    }
    
}
