package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.EventoManagerDao;
import entities.Evento;
import entities.EventoCategoria;
import entities.EventoStatus;
import entities.User;
import exceptions.LoginException;

public final class EventoManager { //Não ter o public significa que ela só é acessível dentro do Package Service
	
	private EventoManager() {}
	
	// ==========================||      USUÁRIOS     ||========================== //
	public static boolean participarEvento(User user, Evento evento) throws LoginException, SQLException {
		LoginManager.verParticipante();
		return EventoManagerDao.adicionarParticipanteEvento(user.getId(), evento.getId());
	}
	
	public static boolean confirmarPresenca(User user, Evento evento) throws Exception {
		LoginManager.verParticipante();
		return EventoManagerDao.confirmarPresencaEvento(user.getId(), evento.getId());
	}
	
	public static boolean sairEvento(User user, Evento evento) throws Exception {
		LoginManager.verParticipante();
		return EventoManagerDao.sairParticipanteEvento(user.getId(), evento.getId());
	}
	
	public static List<Evento> buscarEventosPorNomeCliente(String nome) throws Exception {
		LoginManager.verParticipante();
	    List<Evento> eventos = EventoManagerDao.buscarEventosPorNome(nome);
	    
	    return filtrarEventosCliente(eventos);
	}
	
	public static List<Evento> buscarEventosPorCategoriaCliente(EventoCategoria categoria) throws Exception {
		LoginManager.verParticipante();
	    List<Evento> eventos = EventoManagerDao.buscarEventosPorCategoria(categoria);
	    
	    return filtrarEventosCliente(eventos);
	}
	
	public static List<Evento> getEventosCadastradosCliente() throws Exception{
		LoginManager.verParticipante();		
		return filtrarEventosCliente(EventoManagerDao.getEventosCadastrados(LoginManager.getUsuario().getId()));
	}
	
	private static List<Evento> filtrarEventosCliente(List<Evento> listaEvento){
		List<Evento> listaFiltrada = new ArrayList<Evento>();
		
		for (Evento evento : listaEvento) {
			if (evento.getStatus() == EventoStatus.ABERTO)
				listaFiltrada.add(evento);
		}
		
		return listaFiltrada;
	}
	// ==========================||  ADMINISTRADORES  ||========================== //
	public static boolean criarEvento(Evento evento) throws SQLException, LoginException {
		LoginManager.verAdmin();
		return EventoManagerDao.adicionar(evento);
	}
	
	public static boolean editarEvento(Evento evento) throws SQLException, LoginException {
	    LoginManager.verAdmin();
	    return EventoManagerDao.editar(evento);
	}
	
	public static boolean excluirEvento(int id) throws SQLException,  LoginException {
		LoginManager.verAdmin();
		return EventoManagerDao.excluir(id);
	}
	
	public static List<Evento> buscarEventosPorNome(String nome) throws Exception {
	    LoginManager.verAdmin();
	    return EventoManagerDao.buscarEventosPorNome(nome);
	}

	public static List<Evento> buscarEventosPorCategoria(EventoCategoria categoria) throws Exception {
	    LoginManager.verAdmin();
	    return EventoManagerDao.buscarEventosPorCategoria(categoria);
	}
	
	public static void gerarRelatorioAdmin(Evento evento, String caminhoArquivo) throws Exception {
	    LoginManager.verAdmin();
	    // Um relatório do administrador deve conter:
	    // Todas as informações do evento
	    // Todos os organizadores do evento
	    // Todos os participantes do evento
	    
	    //XLSGenerator.gerarRelatorioEventos(eventos, caminhoArquivo);
	}
	
	public static void gerarRelatorioParticipante(Evento evento, String caminhoArquivo) throws Exception {
	    LoginManager.verParticipante();
	    // Um relatório do administrador deve conter:
	    // Todas as informações do evento
	    // Todos os organizadores do evento
	    // Presença confirmada (relação entre usuário e evento)
	    
	    //XLSGenerator.gerarRelatorioEventos(eventos, caminhoArquivo);
	}
	
	public static List<Evento> getEventosCadastradosAdministrador() throws Exception{
		LoginManager.verAdmin();		
		return EventoManagerDao.getEventosCadastrados(LoginManager.getUsuario().getId());
	}
	
	// ==========================|| ================= ||========================== //
}
