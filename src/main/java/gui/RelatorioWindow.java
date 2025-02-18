package gui;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import entities.Evento;
import entities.UsuarioTipo;
import service.EventoManager;
import service.LoginManager;
import service.XLSGenerator;

public class RelatorioWindow {
	
	private int id;
	private String caminhoDiretorio;
	
	public RelatorioWindow() throws Exception {
		
		inicializar();
	}
	
	private void inicializar() throws Exception {	
		id = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o ID do evento:", "Id do Evento", JOptionPane.QUESTION_MESSAGE));

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Selecione o Diretório para Salvar o Relatório");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);

		int resultado = fileChooser.showDialog(null, "Selecionar Diretório");

		if (resultado == JFileChooser.APPROVE_OPTION) {
			File diretorioSelecionado = fileChooser.getSelectedFile();
			caminhoDiretorio = diretorioSelecionado.getAbsolutePath();

			try {
				Evento eventoRelatorio = EventoManager.getEventoPorId(id);
				if (eventoRelatorio != null) {
					String nomeArquivo = "relatorio_evento_" + id + ".xls"; // Nome do arquivo padrão
					String caminhoCompletoArquivo = caminhoDiretorio + "/" + nomeArquivo; // Caminho completo

					if(LoginManager.getUsuario().getTipo() == UsuarioTipo.ADMIN) {
						XLSGenerator.gerarRelatorioAdmin(eventoRelatorio, caminhoCompletoArquivo);
					} else {
						// Construir o caminho completo para o relatório do participante também
						String nomeArquivoParticipante = "relatorio_participante_evento_" + id + "_usuario_" + LoginManager.getUsuario().getId() + ".xls"; // Nome de arquivo diferente para participante
						String caminhoCompletoArquivoParticipante = caminhoDiretorio + "/" + nomeArquivoParticipante;
						XLSGenerator.gerarRelatorioParticipante(eventoRelatorio, LoginManager.getUsuario().getId(), caminhoCompletoArquivoParticipante);
						caminhoCompletoArquivo = caminhoCompletoArquivoParticipante; // Atualizar para a mensagem de sucesso exibir o caminho correto
					}


					JOptionPane.showMessageDialog(null, "Relatório XLS gerado com sucesso em:\n" + caminhoCompletoArquivo, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Evento com ID " + id + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
				}

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao gerar relatório XLS: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro de SQL ao gerar relatório: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
		} else {
			JOptionPane.showMessageDialog(null, "Geração de relatório cancelada pelo usuário.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
