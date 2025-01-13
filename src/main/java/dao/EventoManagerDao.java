package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;

import service.evento.Evento;
import service.evento.EventoCategoria;
import service.evento.EventoStatus;
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
	
	public static boolean editar(int id, VariavelEvento variavel, Undefined valor) throws SQLException {
		Evento evento = getEventoPorId(id);
		
		
	}
	
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
	
}
