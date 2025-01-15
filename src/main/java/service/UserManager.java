package service;

import java.sql.SQLException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dao.UserManagerDao;
import entities.Administrador;
import entities.User;
import entities.UsuarioTipo;
import exceptions.UsuarioNaoLogadoException;

public final class UserManager {
	// ==========================|| FUNÇÕES PÚBLICAS  ||========================== //
	
	public static void cadastrarUsuario(User user) throws Exception {
		if (LoginManager.isLogado() && LoginManager.getUsuario().getTipo() != UsuarioTipo.ADMIN)
			throw new Exception("Um usuário precisa estar deslogado para registrar outro.");
			
		User usuarioExistente = getUsuarioByEmail(user.getEmail());
		
		if (usuarioExistente != null)
			throw new Exception("Este Email já está sendo ultilizado.");
		
		if (!usuariosMaiorQue(0)) // Define o primeiro usuário como administrador obrigatóriamente
			user = (Administrador) user;
		
		try {
			// Criar requisitos mínimos para senha depois
			user.setSenha(encriptarSenha(user.getSenha()));
			UserManagerDao.cadastrar(user);
		} catch (Exception erro) {
			throw new Exception("Não foi possível cadastrar o usuário ["+erro+"]");
		}
		
	}
		
	public static void apagarUsuario() throws SQLException, UsuarioNaoLogadoException {
		if (!LoginManager.isLogado()) 
			throw new UsuarioNaoLogadoException();
		
		UserManagerDao.apagarUsuarioPorEmail(LoginManager.getUsuario().getEmail());
		LoginManager.logOff();
	}
	
	public static boolean usuariosMaiorQue(int quantidade) throws SQLException {
		return UserManagerDao.UsuariosMaiorQue(quantidade); 
	}
	
	// ==========================|| FUNÇÕES PRIVADAS  ||========================== //
	
	private static String encriptarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha);
	}
		
	// ==========================|| GETTERS E SETTERS ||========================== //
	private static User getUsuarioByEmail(String email) throws Exception {
		return UserManagerDao.getUserPorEmail(email);
	}
	
	// ==========================|| ================= ||========================== //
}

/*
 * DOCUMENTAÇÃO:
 * 	
 * 
 */


 