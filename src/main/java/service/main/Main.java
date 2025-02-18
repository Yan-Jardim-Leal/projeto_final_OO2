package service.main;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import dao.BancoDados;
import gui.LoginWindow;
import service.AtualizadorStatusEvento;

public class Main {

	public static void main(String[] argumentos) throws Exception {
		int tentativas = 0;
		boolean sucesso = false;
		
        // Define o handler global para exceções não tratadas
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            // Garante que a exibição da GUI ocorra na thread de eventos do Swing
            SwingUtilities.invokeLater(() -> {
            	JOptionPane.showMessageDialog(null, "Ocorreu um erro grave ao se conectar com o Banco de Dados,\nverifique sua conexão com o servidor e tente novamente mais tarde.","Erro Grave", JOptionPane.ERROR_MESSAGE);
            	System.exit(0);
            });
        });
        
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
			throw new RuntimeException("Falha na conexão com o banco de dados após 3 tentativas.");
		}
		
		LoginWindow starterFrame = new LoginWindow();
		starterFrame.setVisible(true);
		

		
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

//LoginManager.login("yan2005leal@gmail.com", "senhalegal123@");
//EventoManager.gerarRelatorioAdmin(54, "C:\\Users\\Yan\\Desktop\\Relatorio\\relatorioAdminCorreto.xls");
//System.out.println("Tudo certo!");
//LoginManager.logOff();
//LoginManager.login("participante2.evento@teste.com", "senha456");
//EventoManager.participarEvento(1);
//System.out.println("Participando do evento!");
// 
// "Rafael@gmail.com", "123"
// "Clarisse@leni.com", "123"
// "yan2005leal@gmail.com", "senhalegal123@"
// "participante1.evento@teste.com", "senha123"
// "participante2.evento@teste.com", "senha456"
// "participante3.evento@teste.com", "senha789"

