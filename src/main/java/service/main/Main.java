package service.main;

import java.sql.SQLException;

import dao.BancoDados;
import service.AtualizadorStatusEvento;
import service.LoginManager;

public class Main {
	
	public static void main(String[] argumentos) throws Exception {
		
		int tentativas = 0;
		boolean sucesso = false;

		while (sucesso == false && tentativas < 3) {
			
			try {
				BancoDados.conectar();
				AtualizadorStatusEvento.iniciarAtualizacao();
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
			System.out.println("Aplicação falhou em iniciar, não foi possível se conectar ao banco de dados.");
			return;
		}
		
		try {
			LoginManager.login("yan2005leal@gmail.com", "senhalegal123@");
			//LoginManager.logOff();
		} catch (Exception erro) {
			System.out.println("Não foi possível cadastrar o usuário [ " + erro + " ]");
			throw erro;
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
		    AtualizadorStatusEvento.pararAtualizacao();
		    try {
				BancoDados.desconectar();
			} catch (SQLException erro) {
				erro.printStackTrace();
			}
		}));
		
	}
}


