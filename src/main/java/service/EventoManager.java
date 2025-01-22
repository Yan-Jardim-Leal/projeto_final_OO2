package service;

import java.sql.SQLException;
import java.util.List;

import dao.EventoManagerDao;
import entities.Evento;
import entities.EventoCategoria;
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
	
	public static boolean editarEvento(Evento evento) throws SQLException, LoginException {
	    LoginManager.verAdmin();
	    return EventoManagerDao.editarEvento(evento);
	}
	
	public static boolean excluirEvento(int id) throws SQLException,  LoginException {
		LoginManager.verAdmin();
		return EventoManagerDao.excluir(id);
	}
	
	public static List<Evento> buscarEventosPorNome(String nome) throws SQLException, LoginException {
	    LoginManager.verAdmin();
	    return EventoManagerDao.buscarEventosPorNome(nome);
	}

	public static List<Evento> buscarEventosPorCategoria(EventoCategoria categoria) throws SQLException, LoginException {
	    LoginManager.verAdmin();
	    return EventoManagerDao.buscarEventosPorCategoria(categoria);
	}
	
	public static void gerarRelatorioAdmin(String caminhoArquivo) throws SQLException, LoginException {
	    LoginManager.verAdmin();
	    List<Evento> eventos = EventoManagerDao.listarTodosEventos();
	    // Implemente a geração do XLS usando uma biblioteca como Apache POI
	    XLSGenerator.gerarRelatorioEventos(eventos, caminhoArquivo);
	}
	
	// ==========================|| ================= ||========================== //
}
