package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class BancoDados {
	
	private static Connection conexaoBD;
	
	private BancoDados() {}
	
	public static Connection conectar() throws SQLException, IOException{
		if (conexaoBD != null)
			return null;
		
		Properties propriedades = carregarPropriedades();
		String url = propriedades.getProperty("dburl");
		
		conexaoBD = DriverManager.getConnection(url,propriedades);
		
		UserManagerDao.conectarBD(conexaoBD); // Ele conecta automaticamente o UserManagerDao ao BD, não tem porque não automatizar isso.
		return conexaoBD;
	}
	
	public static Connection desconectar() throws SQLException{
		if (conexaoBD == null)
			return conexaoBD;
		
		conexaoBD.close();
		conexaoBD = null;
		return conexaoBD;
	}
	
	public static Properties carregarPropriedades() throws IOException {
		FileInputStream propriedadeBase = new FileInputStream("database.properties");
		Properties propriedades = new Properties();
		propriedades.load(propriedadeBase);
		return propriedades;
	}
	
	public static void finalizarStatement(Statement statement) throws SQLException {
		if (statement == null)
			return;
		
		statement.close();
	}
	
	public static void finalizarResultSet(ResultSet resultSet) throws SQLException {
		if (resultSet == null)
			return;
		
		resultSet.close();
	}
	
}
