package service;

import java.sql.SQLException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dao.UserManagerDao;
import entities.User;

public final class UserManager {
	
	public static boolean cadastrarUsuario(User user) throws Exception {
		User usuarioExistente = getUsuarioByEmail(user.getEmail());
		
		if (usuarioExistente != null)
			return false;
		try {
			user.setSenha(encriptarSenha(user.getSenha()));
			
			UserManagerDao.cadastrar(user);
		} catch (Exception erro) {
			System.out.println("Não foi possível cadastrar o usuário ["+erro+"]");
			return false;
		}
		
		return true;
	}
	
	private static User getUsuarioByEmail(String email) throws Exception {
		try {
			return UserManagerDao.getUserPorEmail(email);
		} catch (SQLException erro) {
			System.out.println("Não foi possível adquirir o usuário ["+erro+"]");
			return null;
		}
	}
	
	// Criar requisitos mínimos para senha
		
	private static String encriptarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha);
	}
	
	public static User login(String email, String senha) throws Exception {
		User user;
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		try {
			user = UserManagerDao.getUserPorEmail(email);
		} catch (Exception erro) {
			System.out.println("Não foi possível obter o usuário por email");
			user = null;
		}
		
		//comparar a senha encriptada com a normal
		if (user != null && encoder.matches(senha, user.getSenha()))
			return user;
		
		throw new Exception("Senha ou Email incorretos."); // Nome de usuário ou senha incorretos.
	}
	
	// ==========================||     14.01.2025    ||========================== //
	public static boolean maiorQue(int quantidade) throws SQLException {
		return UserManagerDao.maiorQue(quantidade); 
	}
	
	public static boolean apagarUsuario(User user) throws SQLException {
		return UserManagerDao.apagarUsuarioPorEmail(user.getEmail());
	}
	// ==========================|| ================= ||========================== //
}

/*
 * DOCUMENTAÇÃO:
 * 	-Todas as funções aqui serão utilizadas e unificadas pelo sistema ServiceManager.
 * 
 */


 