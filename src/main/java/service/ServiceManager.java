package service;

import java.util.ArrayList;

import service.evento.Evento;
import service.evento.EventoManager;
import service.evento.VariavelEvento;
import service.user.User;
import service.user.UsuarioTipo;

//@SuppressWarnings("unused")
public final class ServiceManager {
	// ==========================||     VARIÁVEIS     ||========================== //
	private User user;
	// ==========================||    CONSTRUTOR     ||========================== //
	public ServiceManager(User user) {
		this.user = user;
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
	// ==========================||   ADMINISTRADOR   ||========================== //
	public ArrayList<User> getUsers(UsuarioTipo tipo) throws Exception {
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
	// ==========================|| ================= ||========================== //
	
}
