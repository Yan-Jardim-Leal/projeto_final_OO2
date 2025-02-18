package service;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.EventoManagerDao;
import entities.Evento;
import entities.EventoCategoria;
import entities.EventoStatus;
import entities.User;
import entities.UsuarioTipo;
import exceptions.LoginException;

public final class EventoManager { //Não ter o public significa que ela só é acessível dentro do Package Service
	
	private EventoManager() {}
	
	// ==========================||      PUBLICAS     ||========================== //
	
	public static Evento getEventoPorId(int id) throws Exception {
		return EventoManagerDao.getEventoPorId(id);
	}
	
	public static boolean participarEvento(int eventoId) throws Exception {
		User user = LoginManager.getUsuario();
		
		if (user.getTipo() == UsuarioTipo.ADMIN)
			return EventoManagerDao.adicionarAdministradorEvento(user.getId(), eventoId);
		else
			return EventoManagerDao.adicionarParticipanteEvento(user.getId(), eventoId);
	}
	
	public static boolean sairEvento(int eventoId) throws Exception {
		User user = LoginManager.getUsuario();
		
		if (user.getTipo() == UsuarioTipo.ADMIN)
			return EventoManagerDao.sairAdministradorEvento(user.getId(), eventoId);
		else
			return EventoManagerDao.sairParticipanteEvento(user.getId(), eventoId);
		
	}
	
	// ==========================||      USUÁRIOS     ||========================== //
	
	public static boolean getPresenca(int eventoId) throws SQLException, LoginException {
		LoginManager.verParticipante();
		return EventoManagerDao.getEventoConfirmado(LoginManager.getUsuario().getId(), eventoId);
	}
	
	public static boolean confirmarPresenca(int eventoId) throws Exception {		LoginManager.verParticipante();
		User user = LoginManager.getUsuario();

		Evento evento = getEventoPorId(eventoId);
		if (evento == null) {
			return false;
		}

		LocalDateTime dataHoraEvento = LocalDateTime.of(evento.getDataEvento().toLocalDate(), evento.getHoraEvento().toLocalTime());
		LocalDateTime now = LocalDateTime.now();
		Duration timeUntilEvent = Duration.between(now, dataHoraEvento);

		if (timeUntilEvent.isNegative() || timeUntilEvent.toHours() <= 8) {
			return EventoManagerDao.confirmarPresencaEvento(user.getId(), eventoId);
		} else {
			return false;
		}
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
			//System.out.println("EVENTO QUE RECEBI:"+evento.getId());
			if (evento.getStatus() == EventoStatus.ABERTO)
				listaFiltrada.add(evento);
		}
		
		return listaFiltrada;
	}
	// ==========================||  ADMINISTRADORES  ||========================== //
	
	public static boolean criarEvento(Evento evento) throws Exception {
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
	
	public static void gerarRelatorioAdmin(int eventoId, String caminhoArquivo) throws Exception {
	    LoginManager.verAdmin();
	    // Um relatório do administrador deve conter:
	    // Todas as informações do evento
	    // Todos os organizadores do evento
	    // Todos os participantes do evento
	    
	    Evento evento = EventoManagerDao.getEventoPorId(eventoId);
	    XLSGenerator.gerarRelatorioAdmin(evento, caminhoArquivo);
	}
	
	public static void gerarRelatorioParticipante(int eventoId, String caminhoArquivo) throws Exception {
	    LoginManager.verParticipante();
	    // Um relatório do administrador deve conter:
	    // Todas as informações do evento
	    // Todos os organizadores do evento
	    // Presença confirmada (relação entre usuário e evento)
	    
	    Evento evento = EventoManagerDao.getEventoPorId(eventoId);
	    XLSGenerator.gerarRelatorioParticipante(evento, LoginManager.getUsuario().getId(),caminhoArquivo);
	}
	
	public static List<Evento> getEventosCadastradosAdministrador() throws Exception{
		LoginManager.verAdmin();		
		return EventoManagerDao.getEventosCadastrados(LoginManager.getUsuario().getId());
	}
	
	// ==========================|| ================= ||========================== //
}
