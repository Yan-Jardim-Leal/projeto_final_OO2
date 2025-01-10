package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import service.user.Administrador;
import service.user.Participante;
import service.user.User;
import service.user.UsuarioTipo;

public final class UserManagerDao {
	private static Connection conexaoBD;
	
	private UserManagerDao() {}
	
	static void conectarBD(Connection conn) { // Só pode ser acessado dentro da package
		conexaoBD = conn;
	}
	
	public static boolean cadastrar(User user) throws SQLException {
		PreparedStatement statement = null;
		int userId;
		
		try {
			conexaoBD.setAutoCommit(false);
			
			statement = conexaoBD.prepareStatement("INSERT INTO usuario (nome_completo,email,senha,tipo) VALUES (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, user.getNome());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getSenha());
			statement.setString(4, user.getTipo().getSQLValue());
			
			statement.executeUpdate();
			
			ResultSet userIds = statement.getGeneratedKeys();
			
			if (userIds.next())
				userId = userIds.getInt(1);
			else
				throw new SQLException("Falha ao obter ID gerado para usuário.");
			
			BancoDados.finalizarStatement(statement);
			if (user.getTipo() == UsuarioTipo.ADMIN) {
				statement = cadastroAdmin((Administrador) user,userId);
				if (statement == null)
					throw new SQLException("Não foi possível criar o administrador");
				
		        statement.executeUpdate();
			}else{
				statement = cadastroDefault((Participante) user,userId);
				if (statement == null)
					throw new SQLException("Não foi possível criar o participante");
				
				statement.executeUpdate();	
			}
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
	
	private static PreparedStatement cadastroAdmin(Administrador admin, int id) { 
		PreparedStatement statement = null;
        String sql = "INSERT INTO administrador (id, cargo, data_contratado) VALUES (?, ?, ?)";
        try {
			statement = conexaoBD.prepareStatement(sql);
	        statement.setInt(1, id);
	        statement.setString(2, admin.getCargo());
	        statement.setDate(3, admin.getDataContratado());
		} catch (SQLException erro) {
	        if (conexaoBD != null) {
	            try {
					conexaoBD.rollback();
				} catch (SQLException rollbackError) {
					System.out.println("Não foi possível dar RollBack ["+rollbackError+"]");
				}
	        }
			
			System.out.println("Erro ["+erro+"]");
		}

		return statement;
	}
	
	private static PreparedStatement cadastroDefault(Participante participante, int id) { 
		PreparedStatement statement = null;
        String sql = "INSERT INTO participante (id, cpf, data_nascimento) VALUES (?, ?, ?)";
        try {
			statement = conexaoBD.prepareStatement(sql);
	        statement.setInt(1, id);
	        statement.setString(2, participante.getCpf());
	        statement.setDate(3, participante.getDataNascimento());
		} catch (SQLException erro) {
			System.out.println("Erro ["+erro+"]");
		}

		
		return statement;
	}
	
	public static User getUserPorEmail(String email) throws Exception {
		PreparedStatement statement = null;
		ResultSet resultado = null;
		
		int id;
		String nome;
		String senha;
		String tipo;
		
		User user = null;
		
		try {
			statement = conexaoBD.prepareStatement("SELECT id,nome,senha FROM usuario WHERE email LIKE ?");
			statement.setString(1, email);
			resultado = statement.executeQuery();
			
			if (resultado.next()) {
				
				id 		= resultado.getInt("id");
				nome 	= resultado.getString("nome");
				senha 	= resultado.getString("senha");
				tipo 	= resultado.getString("tipo");
				
				BancoDados.finalizarResultSet(resultado);
				BancoDados.finalizarStatement(statement);
				
				if (UsuarioTipo.getFromString(tipo) == UsuarioTipo.ADMIN) {
					statement = conexaoBD.prepareStatement("SELECT data_contratado,cargo FROM admin WHERE admin.id = ?");
					statement.setInt(1, id);
					resultado = statement.executeQuery();
					
					Date dataContratado;
					String cargo;
					
					dataContratado  = resultado.getDate("data_contratado");
					cargo = resultado.getString("cargo");
					
					user = (Administrador) new Administrador(nome, senha, email, cargo, dataContratado);
					
				} else {
					statement = conexaoBD.prepareStatement("SELECT data_nascimento,cpf FROM participante WHERE participante.id = ?");
					statement.setInt(1, id);
					resultado = statement.executeQuery();
					
					Date dataNascimento;
					String cpf;
					
					dataNascimento = resultado.getDate("data_nascimento");
					cpf = resultado.getString("cpf");
					
					user = (Participante) new Participante(nome, senha, email, cpf, dataNascimento);
				}
				
				BancoDados.finalizarResultSet(resultado);
				BancoDados.finalizarStatement(statement);
				
			}else {
				System.out.println("Usuário com esse Email não encontrado!");
			}
		}finally {
			BancoDados.finalizarResultSet(resultado);
			BancoDados.finalizarStatement(statement);
		}
		
		return user;
	}
	
}
