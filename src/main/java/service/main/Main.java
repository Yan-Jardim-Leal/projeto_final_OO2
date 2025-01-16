package service.main;

import java.sql.Date;

import dao.BancoDados;
import entities.Administrador;
import service.LoginManager;
import service.UserManager;

public class Main {
	
	public static void main(String[] argumentos) throws Exception {
		
		int tentativas = 0;
		boolean sucesso = false;
		
		while (sucesso == false && tentativas < 3) {
			
			try {
				BancoDados.conectar();
				System.out.println("Banco de dados conectado com sucesso!");
				sucesso = true;
			} catch (Exception erro) {
				// Tentar novamente
				System.out.println("Ocorreu um erro ao acessar o banco de dados, tentando novamente, erro [LOG][ "+erro+" ][/LOG]\n");
				tentativas++;
				//Adicionar delay de alguns ms entre cada tentativa
			}
		}
		
		if (tentativas >= 3) {
			System.err.println("Aplicação falhou em iniciar, não foi possível se conectar ao banco de dados.");
			return;
		}
		
		try {									 //Integer id, String nome, String senha, String email, String cargo, Date dataContratado
			LoginManager.login("yan2005leal@gmail.com", "senhalegal123@");
			LoginManager.logOff();
			//System.out.println("Login realizado com sucesso");
		} catch (Exception erro) {
			System.err.println("Não foi possível cadastrar o usuário [ " + erro + " ]");
			//System.err.println("Não foi possível fazer login com o usuário [ " + erro + " ]");
			throw erro;
		}
		
	}
}
