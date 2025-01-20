package yan;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.sql.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dao.BancoDados;
import entities.Administrador;
import entities.Participante;
import entities.User;
import service.LoginManager;
import service.UserManager;

class UserAndLoginManagerTeste {
	
	private static User user1;
	private static User user2;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		BancoDados.conectar();
		
		user1 = new Administrador(null, "Silvano", "Senha123", "Silvado@utfpr.edu.br", "Organizador geral", Date.valueOf("2024-09-09"));
		user2 = new Participante(null, "Silvano_Usuario", "Senha12345", "Silvado@cliente.utfpr.edu.br", "104.559.349-40", Date.valueOf("2024-09-09"));
		
		UserManager.cadastrarUsuario(user1);
		LoginManager.logOff();
		
		UserManager.registrarUsuario(user2);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		LoginManager.logOff();
		
		LoginManager.login("Silvado@utfpr.edu.br", "Senha123");
		UserManager.apagarUsuario();
		
		LoginManager.login("Silvado@cliente.utfpr.edu.br", "Senha12345");
		UserManager.apagarUsuario();
		
		BancoDados.desconectar();
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void falharAoRegistrarAdmin() {
		
		assertFalse(false);
	}
	
	@Test
	void test() {
		assertFalse(false);
	}

}
