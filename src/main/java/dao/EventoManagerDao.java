package dao;

import java.sql.Connection;

@SuppressWarnings("unused")
public final class EventoManagerDao {	
	private static Connection conexaoBD;
	
	private EventoManagerDao() {}
	
	static void conectarBD(Connection conn) { // Só pode ser acessado dentro da package
		conexaoBD = conn;
	}
	
	public static void 
}
