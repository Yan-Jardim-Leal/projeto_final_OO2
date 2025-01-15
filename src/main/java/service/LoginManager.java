package service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dao.UserManagerDao;
import entities.User;
import entities.UsuarioTipo;
import exceptions.CredenciaisInvalidasException;
import exceptions.LoginException;
import exceptions.UsuarioJaLogadoException;
import exceptions.UsuarioNaoAdminException;
import exceptions.UsuarioNaoEncontradoException;
import exceptions.UsuarioNaoParticipanteException;

public final class LoginManager {
	
	private static User usuarioLogado;
	private LoginManager() {}
	
	public static void login(String email, String senha) throws LoginException {
		if (usuarioLogado != null) {
			throw new UsuarioJaLogadoException(); // Usuário já está logado.
		}
		
		User user;
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		try {
			user = UserManagerDao.getUserPorEmail(email);
		} catch (Exception erro) {
			throw new UsuarioNaoEncontradoException(); // Email não foi encontrado
		}
		
		//comparar a senha encriptada com a normal
		if (user != null && encoder.matches(senha, user.getSenha()))
			usuarioLogado = user;
		
		throw new CredenciaisInvalidasException(); // Nome de usuário ou senha incorretos.
	}
	
	public static void logOff() {
		usuarioLogado = null;
	}
	
	public static boolean isLogado() {
		if (usuarioLogado != null)
			return true;
		return false;
	}
	
	public static void verAdmin() throws LoginException {
		if (usuarioLogado.getTipo() != UsuarioTipo.ADMIN)
			throw new UsuarioNaoAdminException();
	}
	
	public static void verParticipante() throws LoginException {
		if (usuarioLogado.getTipo() != UsuarioTipo.DEFAULT)
			throw new UsuarioNaoParticipanteException();
	}
	
	
	public static User getUsuario() {
		return usuarioLogado;
	}
	
}
