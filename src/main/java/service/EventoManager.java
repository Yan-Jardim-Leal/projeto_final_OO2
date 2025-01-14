package service;

import java.sql.SQLException;

import dao.EventoManagerDao;
import entities.Evento;
import entities.User;
import entities.VariavelEvento;

public final class EventoManager { //Não ter o public significa que ela só é acessível dentro do Package Service
	
	private EventoManager() {}
	
	// ==========================||      USUÁRIOS     ||========================== //
	public static boolean participarEvento(User user, Evento evento) {
		
		return false;
	}
	
	public static boolean confirmarPresenca(User user, Evento evento) {
		
		return false;
	}
	
	public static boolean sairEvento(User user, Evento evento) {
		
		
		return false;
	}
	
	// ==========================||  ADMINISTRADORES  ||========================== //
	public static boolean criarEvento(Evento evento) {
		try {
			return EventoManagerDao.adicionar(evento);
		} catch (SQLException erro) {
			System.out.println("Não foi possível finalizar a criação do evento");
			return false;
		}
		
	}
	
	public static boolean editarEvento(Evento evento,VariavelEvento vEvento) { // Como e o que eu devo poder editar?
		
		return false;
	}
	
	public static boolean excluirEvento(Evento evento) {
		
		return false;
	}
	// ==========================|| ================= ||========================== //
}
