package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entities.Administrador;
import entities.Participante;
import entities.User;
import entities.UsuarioTipo;

public final class UserManagerDao {
	private static Connection conexaoBD;
	
	private UserManagerDao() {}
	
	static void conectarBD(Connection conn) { // Só pode ser acessado dentro da package
		conexaoBD = conn;
	}
	
	public static boolean cadastrar(User user) throws Exception {
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
				if (statement == null) {
					System.out.println("Não foi possível criar o participante.");
					throw new SQLException("Não foi possível criar o administrador");
				}
					
				
		        statement.executeUpdate();
			}else{
				statement = cadastroDefault((Participante) user,userId);
				if (statement == null) {
					System.out.println("Não foi possível criar o participante.");
					throw new SQLException("Não foi possível criar o participante");
				}
					
				
				statement.executeUpdate();	
			}
			BancoDados.finalizarStatement(statement);
			
			conexaoBD.commit(); //Realizar o commit
			return true;
		} catch(SQLException erro) {
			
			throw new Exception("Erro com o banco de dados [ "+erro+" ]");
		} finally {
			BancoDados.finalizarStatement(statement);
		}
	}
	
	public static boolean apagarUsuarioPorEmail(String email) throws SQLException {
	    PreparedStatement statement = null;
	    ResultSet resultado = null;

	    try {
	        conexaoBD.setAutoCommit(false);

	        statement = conexaoBD.prepareStatement("SELECT id FROM usuario WHERE email = ?");
	        statement.setString(1, email);
	        resultado = statement.executeQuery();

	        if (!resultado.next()) {
	            System.out.println("Usuário com esse email não encontrado.");
	            return false;
	        }

	        int userId = resultado.getInt("id");

	        BancoDados.finalizarResultSet(resultado);
	        BancoDados.finalizarStatement(statement);

	        statement = conexaoBD.prepareStatement("DELETE FROM administrador WHERE id = ?");
	        statement.setInt(1, userId);
	        statement.executeUpdate();

	        BancoDados.finalizarStatement(statement);

	        statement = conexaoBD.prepareStatement("DELETE FROM participante WHERE id = ?");
	        statement.setInt(1, userId);
	        statement.executeUpdate();

	        BancoDados.finalizarStatement(statement);

	        statement = conexaoBD.prepareStatement("DELETE FROM usuario WHERE id = ?");
	        statement.setInt(1, userId);
	        int rowsAffected = statement.executeUpdate();

	        // Commit da transação
	        conexaoBD.commit();

	        return rowsAffected > 0;
	    } catch (SQLException erro) {
	        try {
	            conexaoBD.rollback();
	        } catch (SQLException rollbackErro) {
	            System.err.println("Erro ao realizar rollback: " + rollbackErro.getMessage());
	        }
	        System.err.println("Erro ao apagar o usuário [ " + erro + " ]");
	        return false;
	    } finally {
	        BancoDados.finalizarResultSet(resultado);
	        BancoDados.finalizarStatement(statement);
	        conexaoBD.setAutoCommit(true);
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

    User user = null;

    try {
        // Consulta na tabela usuario
        statement = conexaoBD.prepareStatement("SELECT id, nome_completo, senha, tipo FROM usuario WHERE email = ?");
        statement.setString(1, email);
        resultado = statement.executeQuery();

        if (resultado.next()) {
            int id = resultado.getInt("id");
            String nome = resultado.getString("nome_completo");
            String senha = resultado.getString("senha");
            String tipo = resultado.getString("tipo");

            // Determina o tipo de usuário
            if (UsuarioTipo.getFromString(tipo) == UsuarioTipo.ADMIN) {
                BancoDados.finalizarResultSet(resultado);
                BancoDados.finalizarStatement(statement);

                // Consulta adicional para administrador
                statement = conexaoBD.prepareStatement("SELECT data_contratado, cargo FROM administrador WHERE id = ?");
                statement.setInt(1, id);
                resultado = statement.executeQuery();

                if (resultado.next()) {
                    String cargo = resultado.getString("cargo");
                    Date dataContratado = resultado.getDate("data_contratado");

                    user = new Administrador(id, nome, senha, email, cargo, dataContratado);
                }
            } else {
                BancoDados.finalizarResultSet(resultado);
                BancoDados.finalizarStatement(statement);

                // Consulta adicional para participante
                statement = conexaoBD.prepareStatement("SELECT data_nascimento, cpf FROM participante WHERE id = ?");
                statement.setInt(1, id);
                resultado = statement.executeQuery();

                if (resultado.next()) {
                    String cpf = resultado.getString("cpf");
                    Date dataNascimento = resultado.getDate("data_nascimento");

                    user = new Participante(id, nome, senha, email, cpf, dataNascimento);
                }
            }
        } else {
            System.out.println("Usuário com esse email não encontrado!");
        }
    } catch (SQLException erro) {
        throw new Exception("Erro ao buscar usuário por email: " + erro.getMessage(), erro);
    } finally {
        BancoDados.finalizarResultSet(resultado);
        BancoDados.finalizarStatement(statement);
    }

    return user;
}
	
	public static User getUserPorId(int id) throws Exception {
		PreparedStatement statement = null;
		ResultSet resultado = null;
		
		String email;
		String nome;
		String senha;
		String tipo;
		
		User user = null;
		
		try {
			statement = conexaoBD.prepareStatement("SELECT email, nome_completo, senha, tipo FROM usuario WHERE email LIKE ?");
			statement.setInt(1, id);
			resultado = statement.executeQuery();
			
			if (resultado.next()) {
				
				email 	= resultado.getString("email");
				nome 	= resultado.getString("nome_completo");
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
					
					user = (Administrador) new Administrador(id, nome, senha, email, cargo, dataContratado);
					
				} else {
					statement = conexaoBD.prepareStatement("SELECT data_nascimento,cpf FROM participante WHERE participante.id = ?");
					statement.setInt(1, id);
					resultado = statement.executeQuery();
					
					Date dataNascimento;
					String cpf;
					
					dataNascimento = resultado.getDate("data_nascimento");
					cpf = resultado.getString("cpf");
					
					user = (Participante) new Participante(id, nome, senha, email, cpf, dataNascimento);
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
	
	public static boolean UsuariosMaiorQue(int quantidade) throws Exception {
	    PreparedStatement statement = null;
	    ResultSet resultado = null;

	    int comparado = 0;

	    try {
	        // Corrigida a consulta SQL com COUNT(*)
	        statement = conexaoBD.prepareStatement("SELECT COUNT(*) AS total FROM usuario");
	        resultado = statement.executeQuery();

	        if (resultado.next()) {
	            comparado = resultado.getInt("total");
	        }

	    } catch (SQLException erro) {
	        throw new Exception("ERRO ao verificar a quantidade de usuários [ " + erro + " ]");
	    } finally {
	        BancoDados.finalizarResultSet(resultado);
	        BancoDados.finalizarStatement(statement);
	    }
	    
	    
	    System.out.println("A QUANTIDADE: " +quantidade+ " COMPARADO A: "+comparado+ "É: "+ (quantidade > comparado));
	    
	    return quantidade > comparado;
	}

		
}
