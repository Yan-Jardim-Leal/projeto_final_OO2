package service.main;

import dao.BancoDados;

public class Main {
	
	public static void main(String[] argumentos) {
		
		int tentativas = 0;
		boolean sucesso = false;
		
		while (sucesso == false && tentativas < 3) {
			
			try {
				BancoDados.conectar();
				System.out.println("Banco de dados conectado com sucesso!");
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
		
		

		
		
		
		
	}
}
