package service.user;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dao.UserManagerDao;

public final class UserManager {
	
	// Criar requisitos mínimos para senha
	
	private UserManager() {}
	
	// Retorna boolean para a GUI saber como reagir em caso de cadastro ou falha
	public static boolean cadastrarUsuario(User user) {
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
	
	private static String encriptarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha);
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
		
		return null; // Nome de usuário ou senha incorretos.
	}
	
	public static User getUsuarioByEmail(String email) {
		try {
			return UserManagerDao.getUserPorEmail(email);
		} catch (SQLException erro) {
			System.out.println("Não foi possível adquirir o usuário ["+erro+"]");
			return null;
		}
	}
	
	public static ArrayList<User> getUsuarios(UsuarioTipo tipo) {
		
		
		return null;
	}
	
}
 