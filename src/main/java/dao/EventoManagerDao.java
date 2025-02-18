package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entities.Administrador;
import entities.Evento;
import entities.EventoCategoria;
import entities.EventoStatus;
import entities.Participante;
import entities.User;

//@SuppressWarnings("unused")
public final class EventoManagerDao {
	// ==========================||     VARIÁVEIS     ||========================== //
	private static Connection conexaoBD;
	private EventoManagerDao() {}
	// ==========================||      FUNÇÕES      ||========================== //
	
	static void conectarBD(Connection conn) { // Só pode ser acessado dentro da package
		conexaoBD = conn;
	}
	
	public static boolean adicionar(Evento evento) throws Exception {
	    PreparedStatement statement = null;
	    try {
	        conexaoBD.setAutoCommit(false); // Inicia transação

	        // Valida se o evento já tem ID (não deve ter para ser novo)
	        if (evento.getId() != null) {
	            return false;
	        }
	        
	        System.out.println("A tabela está vazia: "+ evento.getOrganizadores().isEmpty());
	        
	        if (evento.getOrganizadores().isEmpty()) { // <--- VALIDAÇÃO NOVA
	            throw new SQLException("Evento deve ter pelo menos 1 organizador.");
	        }
	        
	        // Insere o evento na tabela principal
	        statement = conexaoBD.prepareStatement(
	            "INSERT INTO evento (titulo, descricao, categoria, hora, data, duracao, status, preco, capacidade_maxima, local, is_link) " +
	            "VALUES (?,?,?,?,?,?,?,?,?,?,?)", 
	            PreparedStatement.RETURN_GENERATED_KEYS
	        );

	        // Formata a duração para o padrão ISO 8601 (ex: P2DT3H30M)
	        Duration duracao = evento.getDuracaoEvento();
	        String intervalo = String.format("P%dDT%dH%dM", 
	            duracao.toDaysPart(),
	            duracao.toHoursPart(),
	            duracao.toMinutesPart()
	        );
	        
	        // Preenche os parâmetros do evento
	        statement.setString(1, evento.getTitulo());
	        statement.setString(2, evento.getDescricao());
	        statement.setString(3, evento.getCategoria().getSQL());
	        statement.setTime(4, evento.getHoraEvento());
	        statement.setDate(5, evento.getDataEvento());
	        statement.setString(6, intervalo);
	        statement.setString(7, evento.getStatus().getSQL());
	        statement.setDouble(8, evento.getPreco());
	        statement.setInt(9, evento.getCapacidadeMaxima());
	        statement.setString(10, evento.getLocal());
	        statement.setBoolean(11, evento.isLink());
	        
	        // Executa a inserção do evento
	        int rowsAffected = statement.executeUpdate();
	        System.out.println("Rows afetadas: " + rowsAffected);
	        if (rowsAffected == 0) {
	            throw new SQLException("Falha ao criar evento");
	        }

	        // Recupera o ID gerado para o novo evento
	        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                int novoEventoId = generatedKeys.getInt(1);
	                
	                evento.setId(novoEventoId);
	                
	                // Adiciona administradores associados ao evento

	                for (Administrador admin : evento.getOrganizadores().values()) {
	                    if (!adicionarAdministradorEvento(admin.getId(), novoEventoId)) {
	                        throw new SQLException("Falha ao adicionar administrador " + admin.getId());
	                    }
	                }

	                for (Participante participante : evento.getParticipantes().values()) {
	                    if (!adicionarParticipanteEvento(participante.getId(), novoEventoId)) {
	                        throw new SQLException("Falha ao adicionar participante " + participante.getId());
	                    }
	                }

	            } else {
	                throw new Exception("Nenhum ID gerado para o evento");
	            }
	        }

	        conexaoBD.commit();
	        return true;
 
	    } catch (SQLException erro) {
	    	conexaoBD.rollback();
	        System.out.println("Erro ao adicionar evento [" + erro.getMessage() + "]");
	        return false;
	    } finally {
	    	conexaoBD.setAutoCommit(true);
	        BancoDados.finalizarStatement(statement);
	    }
	}
	
    public static boolean adicionarParticipanteEvento(int participanteId, int eventoId) throws Exception {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            // 1. Obter a capacidade máxima do evento e o número atual de participantes
            String sqlCapacidade = "SELECT capacidade_maxima, (SELECT COUNT(*) FROM participante_evento WHERE evento_id = ?) as participantes_atual FROM evento WHERE id = ?";
            statement = conexaoBD.prepareStatement(sqlCapacidade);
            statement.setInt(1, eventoId);
            statement.setInt(2, eventoId);
            resultSet = statement.executeQuery();

            int capacidadeMaxima = 0;
            int participantesAtual = 0;
            if (resultSet.next()) {
                capacidadeMaxima = resultSet.getInt("capacidade_maxima");
                participantesAtual = resultSet.getInt("participantes_atual");
            }

            if (participantesAtual >= capacidadeMaxima) {
                throw new Exception("Capacidade máxima do evento excedida.");
            }

            // 2. Inserir o participante se a capacidade não for excedida
            String sqlInsert = "INSERT INTO participante_evento (participante_id, evento_id, confirmou_presenca) VALUES (?, ?, ?)";
            BancoDados.finalizarStatement(statement); // Fechar o statement anterior
            statement = conexaoBD.prepareStatement(sqlInsert);
            statement.setInt(1, participanteId);
            statement.setInt(2, eventoId);
            statement.setBoolean(3, false);

            return statement.executeUpdate() > 0; // Não gerencia transação!

        } catch (SQLIntegrityConstraintViolationException erro) {
            return false; // Retorna false em caso de violação de integridade (participante já inscrito)
        } finally {
            BancoDados.finalizarResultSet(resultSet);
            BancoDados.finalizarStatement(statement);
        }
    }

	public static boolean adicionarAdministradorEvento(int adminId, int eventoId) throws SQLException {
	    PreparedStatement statement = null;
	    try {
	        String sql = "INSERT INTO administrador_evento (admin_id, evento_id) VALUES (?, ?)";
	        statement = conexaoBD.prepareStatement(sql);
	        statement.setInt(1, adminId);
	        statement.setInt(2, eventoId);
	        return statement.executeUpdate() > 0; // Não gerencia transação!
	        
	    } finally {
	        BancoDados.finalizarStatement(statement);
	    }
	}
	
	public static boolean editar(Evento evento) throws SQLException {
	    PreparedStatement updateEventoStmt = null;
	    try {
	        conexaoBD.setAutoCommit(false);

	        // Validações
	        if (evento.getId() == null) {
	            throw new SQLException("Evento não possui ID válido para edição.");
	        }
	        if (evento.getStatus() == EventoStatus.ENCERRADO || evento.getStatus() == EventoStatus.CANCELADO) {
	            throw new SQLException("Evento não pode ser editado no status atual.");
	        }

	        // Query de atualização do evento
	        String updateEventoSql = 
	            "UPDATE evento SET " +
	            "titulo = ?, descricao = ?, categoria = ?, hora = ?, data = ?, " +
	            "duracao = ?, status = ?, preco = ?, capacidade_maxima = ?, local = ? " +
	            "WHERE id = ?";
	        
	        updateEventoStmt = conexaoBD.prepareStatement(updateEventoSql);
	        
	        // Formatação da duração (mesmo padrão do método adicionar)
	        Duration duracao = evento.getDuracaoEvento();
	        String intervalo = String.format("P%dDT%dH%dM", 
	            duracao.toDaysPart(),
	            duracao.toHoursPart(),
	            duracao.toMinutesPart()
	        );

	        // Preenche os parâmetros
	        updateEventoStmt.setString(1, evento.getTitulo());
	        updateEventoStmt.setString(2, evento.getDescricao());
	        updateEventoStmt.setString(3, evento.getCategoria().getSQL());
	        updateEventoStmt.setTime(4, evento.getHoraEvento());
	        updateEventoStmt.setDate(5, evento.getDataEvento());
	        updateEventoStmt.setString(6, intervalo);
	        updateEventoStmt.setString(7, evento.getStatus().getSQL());
	        updateEventoStmt.setDouble(8, evento.getPreco());
	        updateEventoStmt.setInt(9, evento.getCapacidadeMaxima());
	        updateEventoStmt.setString(10, evento.getLocal());
	        updateEventoStmt.setInt(11, evento.getId()); // WHERE id = ?

	        int rowsUpdated = updateEventoStmt.executeUpdate();
	        if (rowsUpdated == 0) {
	            throw new SQLException("Evento não encontrado ou nenhum dado alterado.");
	        }

	        conexaoBD.commit(); // Confirma a transação
	        return true;

	    } catch (SQLException erro) {
	    	conexaoBD.rollback();
	        System.out.println("Erro ao editar evento [" + erro.getMessage() + "]");
	        return false;
	    } finally {
	        BancoDados.finalizarStatement(updateEventoStmt);
	        conexaoBD.setAutoCommit(true);
	    }
	}
		
	public static boolean excluir(int id) throws SQLException {
	    PreparedStatement statementAdmins = null;
	    PreparedStatement statementParticipantes = null;
	    PreparedStatement statementEvento = null;

	    try {
	        conexaoBD.setAutoCommit(false); // Inicia transação

	        // 1. Remove administradores associados ao evento
	        String deleteAdminsSql = "DELETE FROM administrador_evento WHERE evento_id = ?";
	        statementAdmins = conexaoBD.prepareStatement(deleteAdminsSql);
	        statementAdmins.setInt(1, id);
	        statementAdmins.executeUpdate();

	        // 2. Remove participantes associados ao evento
	        String deleteParticipantesSql = "DELETE FROM participante_evento WHERE evento_id = ?";
	        statementParticipantes = conexaoBD.prepareStatement(deleteParticipantesSql);
	        statementParticipantes.setInt(1, id);
	        statementParticipantes.executeUpdate();

	        // 3. Remove o evento da tabela principal
	        String deleteEventoSql = "DELETE FROM evento WHERE id = ?";
	        statementEvento = conexaoBD.prepareStatement(deleteEventoSql);
	        statementEvento.setInt(1, id);
	        int linhasAfetadas = statementEvento.executeUpdate();

	        if (linhasAfetadas > 0) {
	            conexaoBD.commit(); // Confirma as exclusões
	            System.out.println("Evento e relações excluídos com sucesso.");
	            return true;
	        } else {
	            System.out.println("Evento não encontrado.");
	            return false;
	        }

	    } catch (SQLException erro) {
	        conexaoBD.rollback(); // Reverte todas as operações em caso de erro
	        System.err.println("Erro ao excluir evento: " + erro.getMessage());
	        throw erro; // Propaga a exceção para tratamento externo
	    } finally {
	        // Restaura o auto-commit e fecha os recursos
	        conexaoBD.setAutoCommit(true); 
	        BancoDados.finalizarStatement(statementAdmins);
	        BancoDados.finalizarStatement(statementParticipantes);
	        BancoDados.finalizarStatement(statementEvento);
	    }
	}

	public static boolean usuarioEstaNoEvento(int userId, int eventoId) throws SQLException {
	    PreparedStatement statement = null;
	    ResultSet resultado = null;

	    try {
	        statement = conexaoBD.prepareStatement(
	            "SELECT 1 FROM participante_evento WHERE participante_id = ? AND evento_id = ?"
	        );
	        statement.setInt(1, userId); // Usa o ID recebido como parâmetro
	        statement.setInt(2, eventoId);

	        resultado = statement.executeQuery();
	        return resultado.next(); // True se encontrou registro
	    } finally {
	        BancoDados.finalizarResultSet(resultado);
	        BancoDados.finalizarStatement(statement);
	    }
	}
	
	public static boolean confirmarPresencaEvento(int userId, int eventoId) throws Exception {
	    PreparedStatement statement = null;
	    try {
	        conexaoBD.setAutoCommit(false); // Inicia transação

	        // 1. Busca o evento
	        Evento evento = EventoManagerDao.getEventoPorId(eventoId);
	        if (evento == null) {
	            System.out.println("Evento não encontrado.");
	            return false;
	        }

	        // 2. Valida status do evento
	        if (evento.getStatus() != EventoStatus.ABERTO) {
	            System.out.println("Confirmação só é permitida em eventos ABERTOS.");
	            return false;
	        }

	        // 3. Valida horário
	        LocalDateTime dataHoraEvento = LocalDateTime.of(
	            evento.getDataEvento().toLocalDate(),
	            evento.getHoraEvento().toLocalTime()
	        );
	        LocalDateTime agora = LocalDateTime.now();
	        
	        if (agora.isAfter(dataHoraEvento.minusHours(1))) {
	            System.out.println("Confirmação permitida apenas até 1h antes do evento.");
	            return false;
	        }

	        // 4. Verifica se o usuário está inscrito
	        if (!usuarioEstaNoEvento(userId, eventoId)) {
	            System.out.println("Usuário não está registrado no evento.");
	            return false;
	        }

	        // 5. Atualiza a confirmação
	        statement = conexaoBD.prepareStatement(
	            "UPDATE participante_evento SET confirmou_presenca = true " +
	            "WHERE participante_id = ? AND evento_id = ?"
	        );
	        statement.setInt(1, userId);
	        statement.setInt(2, eventoId);

	        int rowsUpdated = statement.executeUpdate();
	        if (rowsUpdated == 0) {
	            throw new SQLException("Falha na confirmação de presença.");
	        }

	        conexaoBD.commit(); // Confirma transação
	        return true;

	    } catch (SQLException erro) {
	        conexaoBD.rollback();
	        System.err.println("Erro ao confirmar presença: " + erro.getMessage());
	        return false;
	    } finally {
	        BancoDados.finalizarStatement(statement);
	        conexaoBD.setAutoCommit(true); // Restaura auto-commit
	    }
	}
	
	// ==========================|| GETTERS & SETTERS ||========================== //
	
	public static boolean sairParticipanteEvento(int userId, int eventoId) throws Exception {
	    PreparedStatement statement = null;
	    try {
	        conexaoBD.setAutoCommit(false); // Inicia transação

	        // 1. Verifica se o evento está ABERTO
	        Evento evento = getEventoPorId(eventoId);
	        if (evento == null) {
	            throw new SQLException("Evento não encontrado.");
	        }
	        if (evento.getStatus() != EventoStatus.ABERTO) {
	            throw new SQLException("Só é permitido sair de eventos ABERTOS.");
	        }

	        // 2. Verifica se o usuário está no evento
	        if (!usuarioEstaNoEvento(userId, eventoId)) {
	            System.out.println("Usuário não está registrado no evento.");
	            return false;
	        }

	        // 3. Remove o participante
	        statement = conexaoBD.prepareStatement(
	            "DELETE FROM participante_evento WHERE participante_id = ? AND evento_id = ?"
	        );
	        statement.setInt(1, userId);
	        statement.setInt(2, eventoId);

	        int rowsDeleted = statement.executeUpdate();
	        if (rowsDeleted == 0) {
	            throw new SQLException("Falha ao remover participante.");
	        }

	        conexaoBD.commit(); // Confirma a transação
	        return true;

	    } catch (SQLException erro) {
	        conexaoBD.rollback(); // Reverte em caso de erro
	        System.err.println("Erro ao sair do evento: " + erro.getMessage());
	        return false;
	    } finally {
	        BancoDados.finalizarStatement(statement);
	        conexaoBD.setAutoCommit(true); // Restaura auto-commit
	    }
	}

	public static boolean sairAdministradorEvento(int adminId, int eventoId) throws Exception {
	    PreparedStatement countAdminsStmt = null;
	    PreparedStatement deleteAdminStmt = null;
	    ResultSet rs = null;
	    
	    try {
	    	
	        Evento evento = getEventoPorId(eventoId);
	        if (evento == null) {
	            throw new SQLException("Evento não encontrado.");
	        }
	    	
	        if (evento.getStatus() != EventoStatus.ABERTO) {
	            throw new SQLException("Só é permitido sair de eventos ABERTOS.");
	        }
	    	
	        conexaoBD.setAutoCommit(false); // Inicia transação

	        // 1. Verifica quantos administradores restam no evento
	        String countSql = "SELECT COUNT(*) AS total FROM administrador_evento WHERE evento_id = ?";
	        countAdminsStmt = conexaoBD.prepareStatement(countSql);
	        countAdminsStmt.setInt(1, eventoId);
	        rs = countAdminsStmt.executeQuery();

	        int totalAdmins = 0;
	        if (rs.next()) {
	            totalAdmins = rs.getInt("total");
	        }

	        // 2. Valida se é o último administrador
	        if (totalAdmins <= 1) {
	            throw new SQLException("Não é possível remover o último administrador do evento.");
	        }

	        // 3. Remove o administrador
	        String deleteSql = "DELETE FROM administrador_evento WHERE admin_id = ? AND evento_id = ?";
	        deleteAdminStmt = conexaoBD.prepareStatement(deleteSql);
	        deleteAdminStmt.setInt(1, adminId);
	        deleteAdminStmt.setInt(2, eventoId);

	        int rowsDeleted = deleteAdminStmt.executeUpdate();
	        if (rowsDeleted == 0) {
	            throw new SQLException("Administrador não está associado a este evento.");
	        }

	        conexaoBD.commit(); // Confirma as alterações
	        return true;

	    } catch (SQLException erro) {
	        conexaoBD.rollback(); // Reverte em caso de erro
	        System.err.println("Erro ao remover administrador: " + erro.getMessage());
	        return false;
	    } finally {
	        BancoDados.finalizarResultSet(rs);
	        BancoDados.finalizarStatement(countAdminsStmt);
	        BancoDados.finalizarStatement(deleteAdminStmt);
	        conexaoBD.setAutoCommit(true); // Restaura auto-commit
	    }
	}
	
	public static HashMap<Integer, Participante> getParticipantesEvento(int eventoId) throws Exception {
	    HashMap<Integer, Participante> participantes = new HashMap<>();
	    PreparedStatement statement = null;
	    ResultSet resultado = null;
	    
	    try {
	        // Query corrigida: seleciona participante_id vinculado ao evento
	        statement = conexaoBD.prepareStatement(
	            "SELECT participante_id FROM participante_evento WHERE evento_id = ?"
	        );
	        statement.setInt(1, eventoId); // Define o parâmetro do evento
	        
	        resultado = statement.executeQuery();
	        
	        while (resultado.next()) {
	            int idParticipante = resultado.getInt("participante_id");
	            User user = UserManagerDao.getUserPorId(idParticipante); // Busca o usuário
	            
	            if (user instanceof Participante) { // Verifica se é um Participante
	                participantes.put(idParticipante, (Participante) user);
	            }
	        }
	        
	    } catch (SQLException erro) {
	        System.err.println("Erro ao buscar participantes: " + erro.getMessage());
	        throw erro; // Propaga a exceção para tratamento externo
	    } finally {
	        BancoDados.finalizarResultSet(resultado);
	        BancoDados.finalizarStatement(statement);
	    }
	    
	    return participantes;
	}
	
	public static HashMap<Integer, Administrador> getAdministradoresEvento(int eventoId) throws Exception {
	    HashMap<Integer, Administrador> administradores = new HashMap<>();
	    PreparedStatement statement = null;
	    ResultSet resultado = null;
	    
	    try {
	        // Query para buscar admins vinculados ao evento
	        statement = conexaoBD.prepareStatement(
	            "SELECT admin_id FROM administrador_evento WHERE evento_id = ?"
	        );
	        statement.setInt(1, eventoId); // Define o parâmetro do evento
	        
	        resultado = statement.executeQuery();
	        
	        while (resultado.next()) {
	            int idAdmin = resultado.getInt("admin_id");
	            User user = UserManagerDao.getUserPorId(idAdmin); // Busca o usuário
	            
	            if (user instanceof Administrador) { // Verifica se é um Administrador
	                administradores.put(idAdmin, (Administrador) user);
	            }
	        }
	        
	    } catch (SQLException erro) {
	        System.err.println("Erro ao buscar administradores: " + erro.getMessage());
	        throw erro; // Propaga a exceção
	    } finally {
	        BancoDados.finalizarResultSet(resultado);
	        BancoDados.finalizarStatement(statement);
	    }
	    
	    return administradores;
	}
	
	public static Evento getEventoPorId(int id) throws Exception {
	    PreparedStatement statement = null;
	    ResultSet resultado = null;

	    try {
	        // Query inclui o campo "id" para compatibilidade com mapearEventoDoResultSet
	        statement = conexaoBD.prepareStatement(
	            "SELECT * FROM evento WHERE id = ?"
	        );
	        statement.setInt(1, id);

	        resultado = statement.executeQuery();
	        
	        if (resultado.next()) {
	            return mapearEventoDoResultSet(resultado); // Toda a lógica de mapeamento aqui
	        } else {
	            System.out.println("Nenhum evento encontrado com ID: " + id);
	            return null;
	        }
	    } finally {
	        BancoDados.finalizarResultSet(resultado);
	        BancoDados.finalizarStatement(statement);
	    }
	}
	
	public static boolean getEventoConfirmado(int userId, int eventoId) throws SQLException {
	    PreparedStatement statement = null;
	    ResultSet resultado = null;

	    try {
	        // Query inclui o campo "id" para compatibilidade com mapearEventoDoResultSet
	        statement = conexaoBD.prepareStatement(
	            "SELECT confirmou_presenca FROM participante_evento WHERE participante_id = ? AND evento_id = ?"
	        );
	        statement.setInt(1, userId);
	        statement.setInt(2, eventoId);
	        
	        resultado = statement.executeQuery();
	        
	        if (resultado.next()) {
	        	return resultado.getBoolean("confirmou_presenca");
	        } else {
	            System.out.println("Nenhum evento encontrado com ID: " + eventoId);
	            return false;
	        }
	    } catch (SQLException erro) {
	    	System.out.println("Não foi possível determinar a confirmação do usuário, erro: "+ erro);
	        return false;
	    } finally {
	        BancoDados.finalizarResultSet(resultado);
	        BancoDados.finalizarStatement(statement);
	    }
	}
	
	// ==========================|| ================= ||========================== //
	
	public static List<Evento> buscarEventosPorNome(String nome) throws Exception {
	    List<Evento> eventos = new ArrayList<>();
	    PreparedStatement statement = null;
	    ResultSet resultado = null;

	    try {
	        String sql = "SELECT * FROM evento WHERE titulo LIKE ?";
	        statement = conexaoBD.prepareStatement(sql);
	        statement.setString(1, "%" + nome + "%"); // Busca parcial (ex: "Works" encontra "Workshop")

	        resultado = statement.executeQuery();
	        while (resultado.next()) {
	            Evento evento = mapearEventoDoResultSet(resultado);
	            eventos.add(evento);
	        }

	        return eventos;
	    } finally {
	        BancoDados.finalizarResultSet(resultado);
	        BancoDados.finalizarStatement(statement);
	    }
	}
	
	public static List<Evento> buscarEventosPorCategoria(EventoCategoria categoria) throws Exception {
	    List<Evento> eventos = new ArrayList<>();
	    PreparedStatement statement = null;
	    ResultSet resultado = null;

	    try {
	        String sql = "SELECT * FROM evento WHERE categoria = ?";
	        statement = conexaoBD.prepareStatement(sql);
	        statement.setString(1, categoria.getSQL()); // Usa o valor do Enum no formato SQL

	        resultado = statement.executeQuery();
	        while (resultado.next()) {
	            Evento evento = mapearEventoDoResultSet(resultado);
	            eventos.add(evento);
	        }

	        return eventos;
	    } finally {
	        BancoDados.finalizarResultSet(resultado);
	        BancoDados.finalizarStatement(statement);
	    }
	}
	
	public static List<Evento> listarTodosEventos() throws Exception {
	    List<Evento> eventos = new ArrayList<>();
	    PreparedStatement statement = null;
	    ResultSet resultado = null;

	    try {
	        String sql = "SELECT * FROM evento";
	        statement = conexaoBD.prepareStatement(sql);

	        resultado = statement.executeQuery();
	        while (resultado.next()) {
	            Evento evento = mapearEventoDoResultSet(resultado);
	            eventos.add(evento);
	        }

	        return eventos;
	    } finally {
	        BancoDados.finalizarResultSet(resultado);
	        BancoDados.finalizarStatement(statement);
	    }
	}
	
	private static Evento mapearEventoDoResultSet(ResultSet resultado) throws Exception {
	    int id = resultado.getInt("id");
	    String titulo = resultado.getString("titulo");
	    String descricao = resultado.getString("descricao");
	    Date dataEvento = resultado.getDate("data");
	    Time horaEvento = resultado.getTime("hora");
	    Duration duracao = Duration.parse(resultado.getString("duracao"));
	    String local = resultado.getString("local");
	    boolean isLink = resultado.getBoolean("is_link");
	    int capacidadeMaxima = resultado.getInt("capacidade_maxima");
	    EventoCategoria categoria = EventoCategoria.getFromString(resultado.getString("categoria"));
	    EventoStatus status = EventoStatus.getFromString(resultado.getString("status"));
	    double preco = resultado.getDouble("preco");

	    // Busca relações
	    HashMap<Integer, Administrador> organizadores = getAdministradoresEvento(id);
	    HashMap<Integer, Participante> participantes = getParticipantesEvento(id);

	    return new Evento(
	        id,
	        titulo,
	        descricao,
	        dataEvento,
	        horaEvento,
	        duracao,
	        local,
	        isLink,
	        capacidadeMaxima,
	        categoria,
	        status,
	        preco,
	        organizadores,
	        participantes
	    );
	}
	
	public static List<Evento> getEventosCadastrados(int userId) throws Exception {
	        List<Evento> eventos = new ArrayList<>();
	        PreparedStatement statement = null;
	        ResultSet resultado = null;

	        try {
	            String sql = "(SELECT e.* FROM evento e " +
	                         "INNER JOIN administrador_evento ae ON e.id = ae.evento_id " +
	                         "WHERE ae.admin_id = ? ) " +
	                         "UNION " +
	                         "(SELECT e.* FROM evento e " +
	                         "INNER JOIN participante_evento pe ON e.id = pe.evento_id " +
	                         "WHERE pe.participante_id = ?)";


	            statement = conexaoBD.prepareStatement(sql);
	            statement.setInt(1, userId);
	            statement.setInt(2, userId);


	            resultado = statement.executeQuery();

	            // Mapeia os resultados para objetos Evento
	            while (resultado.next()) {
	                Evento evento = mapearEventoDoResultSet(resultado);
	                eventos.add(evento);
	            }

	            return eventos;
	        } finally {
	            BancoDados.finalizarResultSet(resultado);
	            BancoDados.finalizarStatement(statement);
	        }
	    }
	
	// ==========================|| ================= ||========================== //
	
}
