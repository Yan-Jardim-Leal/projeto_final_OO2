package service.user;

import java.sql.SQLException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dao.UserManagerDao;

public final class UserManager {
	
	// Criar requisitos mínimos para senha
	
	private UserManager() {}
	
	// Retorna boolean para a GUI saber como reagir em caso de cadastro ou falha
	public static boolean cadastrarUsuario(User user) {
		User usuarioExistente = encontrarUsuario(user);
		
		if (usuarioExistente != null)
			return false;
		
		try {
			UserManagerDao.cadastrar(user);
		} catch (SQLException erro) {
			System.out.println("Não foi possível cadastrar o usuário ["+erro+"]");
			return false;
		}
		
		return true;
	}
	
	public static User encontrarUsuario(User user) {
		try {
			return UserManagerDao.getUserPorEmail(user.getEmail());
		} catch (SQLException erro) {
			System.out.println("Não foi possível adquirir o usuário ["+erro+"]");
			return null;
		}
	}
	
	public static User login(String email, String senha) {
		User user;
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		try {
			user = UserManagerDao.getUserPorEmail(email);
		} catch (Exception erro) {
			user = null;
		}
		
		//comparar a senha encriptada com a normal
		if (user != null && encoder.matches(senha, user.getSenha()))
			return user;
		
		return null; // Nome ou senha incorretos
	}
	
}
 