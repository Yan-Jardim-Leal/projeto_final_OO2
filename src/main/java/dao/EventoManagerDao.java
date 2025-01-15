package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;

import entities.Evento;
import entities.EventoCategoria;
import entities.EventoStatus;
import entities.Participante;
import entities.User;
import entities.VariavelEvento;

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
				return false; //Para adicionar um evento ele não pode ter id
			
			statement = conexaoBD.prepareStatement("INSERT INTO evento (titulo, descricao, categoria, hora, data, duracao, status, preco, capacidade_maxima, local) VALUES (?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			
			Duration duracao = evento.getDuracaoEvento();
			String intervalo = String.format("P%dDT%dH%dM", 
		            duracao.toDaysPart(),
		            duracao.toHoursPart(),
		            duracao.toMinutesPart()
			);
			
			statement.setString(1, evento.getTitulo());
			statement.setString(2, evento.getDescricao());
			statement.setString(3, evento.getCategoria().getSQL()); // Converter para comando SQL
			statement.setTime(4, evento.getHoraEvento());
			statement.setDate(5, evento.getDataEvento());
			statement.setString(6, intervalo);
			statement.setString(7, evento.getStatus().getSQL()); // Converter para comando SQL
			statement.setDouble(8, evento.getPreco());
			statement.setInt(9, evento.getCapacidadeMaxima());
			statement.setString(10, evento.getLocal());
			
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
	
	/*
	public static boolean editar(int id, VariavelEvento variavel, Undefined valor) throws SQLException {
		Evento evento = getEventoPorId(id);
	}
	*/
	
	public static Evento getEventoPorId(int id) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultado = null;
		
		Evento evento = null;
		
		String titulo;
		String descricao;
		
		Date dataEvento;
		Time horaEvento;
		Duration duracaoEvento;
		
		boolean isLink;
		
		String local;
		int capacidadeMaxima;
		
		EventoCategoria categoria;
		EventoStatus status;
		
		double preco;
		
		try {
			statement = conexaoBD.prepareStatement("SELECT titulo, descricao, categoria, hora, data, duracao, status, preco, capacidade_maxima, is_link, local FROM evento WHERE id = ?");
			statement.setInt(1, id);
			
			resultado = statement.executeQuery();
			
			if (resultado.next()) {
				
				titulo 				= resultado.getString("titulo");
				descricao 			= resultado.getString("descricao");
				local				= resultado.getString("local");
				
				horaEvento 			= resultado.getTime("hora");
				dataEvento 			= resultado.getDate("data");
				
				isLink 				= resultado.getBoolean("is_link");
				
				preco 				= resultado.getDouble("preco");
				capacidadeMaxima	= resultado.getInt("capacidade_maxima");
				
				duracaoEvento 		= Duration.parse(resultado.getString("duracao"));
				
				status 				= EventoStatus.valueOf(resultado.getString("status"));
				categoria 			= EventoCategoria.valueOf(resultado.getString("categoria"));
				
				return new Evento(
						id,
						titulo, 
						descricao,
						
						dataEvento, 
						horaEvento, 
						duracaoEvento,
						
						local, // Caso seja link, especificar
						isLink,
						
						capacidadeMaxima,
						categoria,
						status,
						preco
					);
				
			} else {
				System.out.println("Não há nenhum evento cadastrado com esse id.");
				return null;
			}
			
		} finally {
			
			BancoDados.finalizarResultSet(resultado);
			BancoDados.finalizarStatement(statement);
			
		}
	}
	
	public static ArrayList<Participante> getParticipantesEvento(int id) throws SQLException {
		ArrayList<Participante> participantes = new ArrayList<Participante>();
		
		PreparedStatement statement = null;
		ResultSet resultado = null;
		
		try {
			statement = conexaoBD.prepareStatement("SELECT participante_evento_id FROM participante_evento WHERE id = ?");
			resultado = statement.executeQuery();
			
			while (resultado.next()) {
				
				int idParticipante = resultado.getInt("participante_evento_id");
				
				participantes.add((Participante) UserManagerDao.getUserPorId(idParticipante));
				
			}
			
		} catch (SQLException erro) {
			
			erro.printStackTrace();
		} catch (Exception erro) {
			
			erro.printStackTrace();
		} finally {
			
			BancoDados.finalizarResultSet(resultado);
			BancoDados.finalizarStatement(statement);
		}
		
		return participantes;
	}
	
	public static boolean excluir(int id) throws SQLException {
	    PreparedStatement statementOrganizadores = null;
	    PreparedStatement statementParticipanteEvento = null;
	    PreparedStatement statementEvento = null;

	    try {
	        conexaoBD.setAutoCommit(false);

	        Evento evento = getEventoPorId(id);
	        if (evento == null) {
	            System.out.println("Evento não encontrado com o ID: " + id);
	            return false;
	        }

	        statementOrganizadores = conexaoBD.prepareStatement("DELETE FROM organizadores WHERE evento_id = ?");
	        statementOrganizadores.setInt(1, id);
	        statementOrganizadores.executeUpdate();

	        statementParticipanteEvento = conexaoBD.prepareStatement("DELETE FROM participante_evento WHERE evento_id = ?");
	        statementParticipanteEvento.setInt(1, id);
	        statementParticipanteEvento.executeUpdate();

	        statementEvento = conexaoBD.prepareStatement("DELETE FROM evento WHERE id = ?");
	        statementEvento.setInt(1, id);
	        int linhasAfetadas = statementEvento.executeUpdate();

	        if (linhasAfetadas > 0) {
	            conexaoBD.commit();
	            System.out.println("Evento com ID " + id + " e seus relacionamentos foram excluídos com sucesso.");
	            return true;
	        } else {
	            System.out.println("Nenhum evento foi excluído. ID inexistente.");
	            return false;
	        }
	    } catch (SQLException erro) {
	        conexaoBD.rollback();
	        System.err.println("Erro ao excluir evento: " + erro.getMessage());
	        throw erro;
	    } finally {
	        conexaoBD.setAutoCommit(true);
	        BancoDados.finalizarStatement(statementOrganizadores);
	        BancoDados.finalizarStatement(statementParticipanteEvento);
	        BancoDados.finalizarStatement(statementEvento);
	    }
	}
	
	public static boolean usuarioEstaNoEvento(User user, int eventoId) throws SQLException {
	    PreparedStatement statement = null;
	    ResultSet resultado = null;

	    try {
	        statement = conexaoBD.prepareStatement("SELECT 1 FROM participante_evento WHERE participante_id = ? AND evento_id = ?");
	        statement.setInt(1, user.getId());
	        statement.setInt(2, eventoId);

	        resultado = statement.executeQuery();

	        return resultado.next(); // Retorna true se encontrar um registro
	    } finally {
	        BancoDados.finalizarResultSet(resultado);
	        BancoDados.finalizarStatement(statement);
	    }
	}
	
	public static boolean adicionarParticipanteEvento(User user, int eventoId) throws SQLException {
	    PreparedStatement statement = null;

	    try {
	        if (usuarioEstaNoEvento(user, eventoId)) {
	            System.out.println("Usuário já está no evento.");
	            return false;
	        }

	        statement = conexaoBD.prepareStatement(
	            "INSERT INTO participante_evento (confirmou_presenca, participante_id, evento_id) VALUES (?, ?, ?)"
	        );
	        statement.setBoolean(1, false); // Presença não confirmada inicialmente
	        statement.setInt(2, user.getId());
	        statement.setInt(3, eventoId);

	        statement.executeUpdate();
	        System.out.println("Usuário adicionado ao evento com sucesso.");
	        return true;
	    } finally {
	        BancoDados.finalizarStatement(statement);
	    }
	}
	
	public static boolean confirmarPresencaEvento(User user, int eventoId) throws SQLException {
	    PreparedStatement statement = null;

	    try {
	        if (!usuarioEstaNoEvento(user, eventoId)) {
	            System.out.println("Usuário não está registrado no evento.");
	            return false;
	        }

	        statement = conexaoBD.prepareStatement(
	            "UPDATE participante_evento SET confirmou_presenca = ? WHERE participante_id = ? AND evento_id = ?"
	        );
	        statement.setBoolean(1, true);
	        statement.setInt(2, user.getId());
	        statement.setInt(3, eventoId);

	        int rowsUpdated = statement.executeUpdate();
	        if (rowsUpdated > 0) {
	            return true;
	        } else {
	            return false;
	        }
	    } finally {
	        BancoDados.finalizarStatement(statement);
	    }
	}
	
	public static boolean sairParticipanteEvento(User user, int eventoId) throws SQLException {
	    PreparedStatement statement = null;

	    try {
	        if (!usuarioEstaNoEvento(user, eventoId)) {
	            System.out.println("Usuário não está registrado no evento.");
	            return false;
	        }

	        statement = conexaoBD.prepareStatement(
	            "DELETE FROM participante_evento WHERE participante_id = ? AND evento_id = ?"
	        );
	        statement.setInt(1, user.getId());
	        statement.setInt(2, eventoId);

	        int rowsDeleted = statement.executeUpdate();
	        if (rowsDeleted > 0) {
	            System.out.println("Usuário removido do evento com sucesso.");
	            return true;
	        } else {
	            System.out.println("Erro ao remover usuário do evento. Nenhuma linha foi excluída.");
	            return false;
	        }
	    } finally {
	        BancoDados.finalizarStatement(statement);
	    }
	}
}
