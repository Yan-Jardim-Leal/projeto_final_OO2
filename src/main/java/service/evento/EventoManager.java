package service.evento;

import service.user.Administrador;
import service.user.Participante;

public final class EventoManager {
	
	// ==========================||      USUÁRIOS     ||========================== //
	public boolean participarEvento(Participante participante) {
		
		return false;
	}
	
	public boolean confirmarPresenca(Participante participante) {
		
		return false;
	}
	
	public boolean sairEvento(Participante participante) {
		
		
		return false;
	}
	
	// ==========================||  ADMINISTRADORES  ||========================== //
	public boolean criarEvento(Administrador admin) {
		
		return false;
	}
	
	public boolean editarEvento(Administrador admin, VariavelEvento vEvento) { // Como e o que eu devo poder editar?
		
		return false;
	}
	
	public boolean excluirEvento(Administrador admin) {
		
		return false;
	}
	// ==========================|| ================= ||========================== //
}
