package service;

import java.sql.SQLException;

import dao.EventoManagerDao;
import entities.Evento;
import entities.User;
import exceptions.LoginException;

public final class EventoManager { //Não ter o public significa que ela só é acessível dentro do Package Service
	
	private EventoManager() {}
	
	// ==========================||      USUÁRIOS     ||========================== //
	public static boolean participarEvento(User user, Evento evento) throws LoginException, SQLException {
		LoginManager.verParticipante();
		return EventoManagerDao.adicionarParticipanteEvento(user, evento.getId());
	}
	
	public static boolean confirmarPresenca(User user, Evento evento) throws LoginException, SQLException {
		LoginManager.verParticipante();
		return EventoManagerDao.confirmarPresencaEvento(user, evento.getId());
	}
	
	public static boolean sairEvento(User user, Evento evento) throws LoginException, SQLException {
		LoginManager.verParticipante();
		return EventoManagerDao.sairParticipanteEvento(user, evento.getId());
	}
	
	// ==========================||  ADMINISTRADORES  ||========================== //
	public static boolean criarEvento(Evento evento) throws SQLException, LoginException {
		LoginManager.verAdmin();
		return EventoManagerDao.adicionar(evento);
	}
	
/*
 * 	public static boolean editarEvento(Evento evento,VariavelEvento vEvento) { // Como e o que eu devo poder editar?
		LoginManager.verAdmin();
		return EventoManagerDao.editar(evento,vEvento,aEvento);
	}
 */
	
	public static boolean excluirEvento(int id) throws SQLException,  LoginException {
		LoginManager.verAdmin();
		return EventoManagerDao.excluir(id);
	}
	// ==========================|| ================= ||========================== //
}
