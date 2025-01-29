package service.main;

import java.sql.SQLException;

import dao.BancoDados;
import service.AtualizadorStatusEvento;
import service.EventoManager;
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
		
		LoginManager.login("yan2005leal@gmail.com", "senhalegal123@");
		
		EventoManager.gerarRelatorioAdmin(54, "C:\\Users\\Yan\\Desktop\\Relatorio\\relatorioAdminCorreto.xls");
		
		System.out.println("Tudo certo!");
		
		LoginManager.logOff();
		//LoginManager.login("participante2.evento@teste.com", "senha456");
		
		//EventoManager.participarEvento(1);
		//System.out.println("Participando do evento!");
		
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
		    AtualizadorStatusEvento.pararAtualizacao();
		    try {
				BancoDados.desconectar();
			} catch (SQLException erro) {
				erro.printStackTrace();
			}
		}));
		
	}
	
	// "yan2005leal@gmail.com", "senhalegal123@"
    // "participante1.evento@teste.com", "senha123"
    // "participante2.evento@teste.com", "senha456"
    // "participante3.evento@teste.com", "senha789"
}


