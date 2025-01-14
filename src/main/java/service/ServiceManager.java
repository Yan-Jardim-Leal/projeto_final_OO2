package service;

import java.sql.SQLException;
import java.util.ArrayList;

import entities.Administrador;
import entities.Evento;
import entities.Participante;
import entities.User;
import entities.UsuarioTipo;
import entities.VariavelEvento;

//@SuppressWarnings("unused")
public final class ServiceManager {
	// ==========================||     VARIÁVEIS     ||========================== //
	private User user;
	// ==========================||    CONSTRUTOR     ||========================== //
	private ServiceManager(User user) {
		this.user = user;
	}
	// ==========================||    ESTÁTICOS      ||========================== //
	private static boolean registrarAdmin(Administrador user) throws Exception {
		if (user.getTipo() == UsuarioTipo.ADMIN)
			return UserManager.cadastrarUsuario(user);
		return false;
	}
	
	private static boolean registrarParticipante(Participante user) throws Exception {
		if (user.getTipo() == UsuarioTipo.DEFAULT)
			return UserManager.cadastrarUsuario(user);
		return false;
	}
	 
	public static boolean verificarPrimeiroRegistro() throws Exception {
		// Verifica se há algum registro de usuário
		try {
			if (UserManager.maiorQue(0)){
				return false;
			}
		} catch (SQLException erro) {
			System.out.println("Ocorreu um erro ao verificar o primeiro registro");
			throw new Exception("Não foi possível se é o primeiro registro. [ "+erro+" ]");
		}
		
		return true;
	}
	
	public static boolean verficiarRegistroMaiorQueUm() throws Exception {
		try {
			if (UserManager.maiorQue(1)){
				return true;
			}
		} catch (SQLException erro) {
			System.out.println("Ocorreu um erro ao verificar o primeiro registro");
			throw new Exception("Não foi possível se o registro tem mais de um usuário.");
		}
		
		return false;
	}
	
	// ==========================|| REGISTRO E LOGIN  ||========================== //
	public static ServiceManager login(String email, String senha) throws Exception {
		return new ServiceManager(UserManager.login(email, senha)); 
	}
	
	public static boolean registrarUser(User user) throws Exception {
		if (!verificarPrimeiroRegistro()) // Isso implica que não existe ninguém registrado
			return registrarAdmin((Administrador) user);
		else
			return registrarParticipante((Participante) user);
	}
	
	// ==========================||   VERIFICADORES   ||========================== //
	
	private void verificarAdmin() throws Exception{
		if (this.user.getTipo() != UsuarioTipo.ADMIN)
			throw new Exception("Usuário não é um administrador.");
	}
	
	private void verificarDefault() throws Exception{
		if (this.user.getTipo() != UsuarioTipo.DEFAULT)
			throw new Exception("Usuário não é um participante.");
	}
	
	private void verificarLogin() throws Exception{
		if (this.user != null)
			throw new Exception("Usuário já está logado.");
	}
	
	public boolean existemAdministradores() throws Exception {
		verificarLogin();
		
		return false;
	}
	
	public boolean precisaConfirmarPresencaEvento(Evento evento) {
		
		return false;
	}
	
	// ==========================||   ADMINISTRADOR   ||========================== //
	
	public boolean registrarAdminViaAdmin(Administrador user) throws Exception {
		verificarAdmin();
		
		return registrarAdmin(user);
	}
	
	public void apagarUsuario(User user) throws Exception {
		verificarAdmin();
		
		
	}
	
	public ArrayList<User> getUsers(UsuarioTipo tipo) throws Exception {
		verificarAdmin();
		
		return null;
	}
	
	public ArrayList<User> getUsersPorNome(String nome) throws Exception {
		verificarAdmin();
		
		return null;
	}
	
	public void gerarRelatorioXLSAdmin() throws Exception {
		verificarAdmin();
		
	}
	
	public void criarEvento(Evento evento) throws Exception {
		verificarAdmin();
		if (!EventoManager.criarEvento(evento))
			throw new Exception("Ocorreu um erro ao criar o evento");
		
	}
	
	public void editarEvento(Evento evento, VariavelEvento variavel) throws Exception {
		verificarAdmin();
		if (!EventoManager.editarEvento(evento,variavel))
			throw new Exception("Ocorreu um erro ao editar o evento");
		
	}
	
	public void excluirEvento() throws Exception {
		verificarAdmin();
		if (!EventoManager.excluirEvento(null))
			throw new Exception("Ocorreu um erro ao excluir o evento");
		
	}
	
	// ==========================||    PARTICIPANTE   ||========================== //
	public void gerarRelatorioXLSParticipante() throws Exception {
		verificarDefault();
		
	}
	
	public void participarEvento(Evento evento) throws Exception {
		verificarDefault();
		if (!EventoManager.participarEvento(user,evento))
			throw new Exception("Ocorreu um erro ao participar do evento");
		
	}
	
	public void sairEvento(Evento evento) throws Exception {
		verificarDefault();
		if (!EventoManager.sairEvento(user,evento))
			throw new Exception("Ocorreu um erro ao sair do evento");
		
	}
	
	public void confirmarPresenca(Evento evento) throws Exception {
		verificarDefault();
		if (!EventoManager.confirmarPresenca(user,evento))
			throw new Exception("Ocorreu um erro ao confirmar sua presença no evento");
			
	}
	
	public void registrarParticipante(User user) throws Exception {
		verificarLogin();
		
		
	}
	
	// ==========================|| ================= ||========================== //
	
	public ArrayList<Evento> getEventoPorNome(String nome) throws Exception {
		
		return null;
	}
	
}
