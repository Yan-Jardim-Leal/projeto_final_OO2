package service.user;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dao.UserManagerDao;

public final class UserManager {
	
	private User usuarioLogado;
	
	private UserManager(User usuario) {
		this.usuarioLogado = usuario;
	}
	
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
	
	private static User getUsuarioByEmail(String email) {
		try {
			return UserManagerDao.getUserPorEmail(email);
		} catch (SQLException erro) {
			System.out.println("Não foi possível adquirir o usuário ["+erro+"]");
			return null;
		}
	}
	

	// Criar requisitos mínimos para senha
	// Retorna boolean para a GUI saber como reagir em caso de cadastro ou falha

	public User getUser() {
		return usuarioLogado;
	}
		
	private static String encriptarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha);
	}
	
	
	public static UserManager login(String email, String senha) {
		User user;
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		try {
			user = UserManagerDao.getUserPorEmail(email);
		} catch (Exception erro) {
			user = null;
		}
		
		//comparar a senha encriptada com a normal
		if (user != null && encoder.matches(senha, user.getSenha())) {
			return new UserManager(user);
		}
		return null; // Nome de usuário ou senha incorretos.
	}
	
	public User getUsuarioLogado() {
		return this.usuarioLogado;
	}
	

}

// DOCUMENTAÇÃO DA CLASSE:
/*
 * 	Essa classe funciona assim:
 * 	- Ela contém métodos estáticos, que não precisam de nenhum usuário logado para realizar
 * 	- Ela contém métodos de classe, que precisam de um login para poderem funcionar, um exemplo:
 * 		um usuário participante só poderia usar funções que dizem respeito aos participantes.
 * 		um usuário admin só poderia usar funções que dizem respeito aos administradores.
 * 		uma função geral que funciona idependente de logado ou não.
 */


 