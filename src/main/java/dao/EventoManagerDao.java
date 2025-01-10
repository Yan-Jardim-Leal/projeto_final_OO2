package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import service.evento.Evento;
import service.evento.VariavelEvento;
import service.user.Participante;

@SuppressWarnings("unused")
public final class EventoManagerDao {	
	private static Connection conexaoBD;
	
	private EventoManagerDao() {}
	
	static void conectarBD(Connection conn) { // Só pode ser acessado dentro da package
		conexaoBD = conn;
	}
	
	public static boolean adicionar(Evento evento) throws SQLException {
		PreparedStatement statement = null;
		try {
			conexaoBD.setAutoCommit(false);
			
			if (evento.getId() != null)
				throw new Exception("Para adicionar um evento ele não pode ter id");
			
			statement = conexaoBD.prepareStatement("INSERT INTO usuario (nome_completo,email,senha,tipo) VALUES (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, user.getNome());
			
			BancoDados.finalizarStatement(statement);
			
			conexaoBD.commit(); //Realizar o commit
			return true;
		}catch(SQLException erro) {
			System.out.println("Erro com o banco de dados ["+erro+"]");
			return false;
		}finally {
			BancoDados.finalizarStatement(statement);
		}
	}
	
	public static boolean editar(int id, VariavelEvento variavel, Generic valor) {
		
	}
	
	public static Evento getEventoPorId(int id) throws Exception {
		PreparedStatement statement = null;
		Evento evento = null;
		
		try {
			conexaoBD.setAutoCommit(false);
			statement = conexaoBD.prepareStatement("SELECT  FROM evento WHERE id = ?");
			statement.setInt(1, id);
			
		} finally {
			
			
		}
		
		return evento;
	}
	
	public static ArrayList<Participante> getParticipantesEvento(int id) {
		ArrayList<Participante> participantes = null;
		
		
		return participantes;
	}
	
}
