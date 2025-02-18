package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeParseException;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entities.Evento;
import service.EventoManager;
import service.LoginManager;

public final class AdministradorWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel painelAtual; 
	
	public AdministradorWindow() {
		
		//===================================================================//
		
		this.setSize(680, 425);
		this.setLocationRelativeTo(null);
		
		criarComponentes();
	}
	
	private void criarComponentes() {
		setTitle("Menu do Administrador");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Usuário");
		menuBar.add(mnNewMenu);
		
		JMenuItem logOffButton = new JMenuItem("LogOff");
		mnNewMenu.add(logOffButton);
		getContentPane().setLayout(null);
		
		JList<Object> list = new JList<Object>();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"Buscar por nome", "Vizualizar um evento", "Eventos participando", "Gerar relatório XLS", "Criar um evento", "Editar um evento", "Excluir um evento", "Cadastrar um admin"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 11, 125, 342);
		getContentPane().add(scrollPane_1);
		
		scrollPane_1.setViewportView(list);
		
		logOffButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				logOff();
			}
		});
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evento) {
				if (painelAtual != null) {
					painelAtual.setVisible(false);
				}
				
				try {
					int id;
					Evento eventoPorId;
					
					switch (list.getSelectedIndex()){
						case 0:
							painelAtual = new PesquisaPorNome();
		                    painelAtual.setLayout(null);
		                    
		                    getContentPane().add(painelAtual);
		                    getContentPane().revalidate();
		                    getContentPane().repaint();
							break;
						case 1:
							id = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o ID do evento:", "Id do Evento", JOptionPane.QUESTION_MESSAGE));
							
							eventoPorId = EventoManager.getEventoPorId(id);
							
							if (eventoPorId == null) {
								JOptionPane.showMessageDialog(null, "Não foi encontrado um evento com esse ID.","Oops", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
							
							painelAtual = new VisualizarEvento(eventoPorId);
							painelAtual.setLayout(null);
		                    getContentPane().add(painelAtual);
		                    getContentPane().revalidate();
		                    getContentPane().repaint();
							break;
						case 2:
							painelAtual = new EventosCadastrados();
							
							painelAtual.setLayout(null);
		                    getContentPane().add(painelAtual);
		                    getContentPane().revalidate();
		                    getContentPane().repaint();
							break;
						case 3:
							new RelatorioWindow();
							break;
						case 4:
							painelAtual = new CriarEvento();
							
							painelAtual.setLayout(null);
		                    getContentPane().add(painelAtual);
		                    getContentPane().revalidate();
		                    getContentPane().repaint();
		                    break;
						case 5:
							id = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o ID do evento:", "Id do Evento", JOptionPane.QUESTION_MESSAGE));
							
							eventoPorId = EventoManager.getEventoPorId(id);
							
							if (eventoPorId == null) {
								JOptionPane.showMessageDialog(null, "Não foi encontrado um evento com esse ID.","Oops", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
							
							painelAtual = new EditarEvento(eventoPorId);
							
							painelAtual.setLayout(null);
		                    getContentPane().add(painelAtual);
		                    getContentPane().revalidate();
		                    getContentPane().repaint();
							break;
						case 6:
							id = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o ID do evento a ser excluído:", "Id do Evento", JOptionPane.QUESTION_MESSAGE));

							if (JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o evento com ID " + id + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
								if (EventoManager.excluirEvento(id)) {
									JOptionPane.showMessageDialog(null, "Evento com ID " + id + " excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
									
								} else {
									JOptionPane.showMessageDialog(null, "Falha ao excluir o evento com ID " + id + ".\nVerifique se o ID está correto.", "Erro", JOptionPane.ERROR_MESSAGE);
								}
							} else {
								JOptionPane.showMessageDialog(null, "Exclusão de evento cancelada pelo usuário.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
							}
							break;
						case 7:
							painelAtual = new CriarAdministrador();
							
							painelAtual.setLayout(null);
		                    getContentPane().add(painelAtual);
		                    getContentPane().revalidate();
		                    getContentPane().repaint();
		                    break;
						default:
							System.out.println("Algo muito inesperado aconteceu...");
					}
				} catch (NumberFormatException erro) {
					return;
				} catch (IllegalArgumentException | DateTimeParseException erro) {
					JOptionPane.showMessageDialog(null, "Tente novamente, dados em formatos incorretos.","Erro", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception erro) {
					System.out.println(erro);
					
					JOptionPane.showMessageDialog(null, "Ocorreu um erro com sua conexão,\ntente novamente mais tarde ou verifique sua estabilidade com o banco de dados.","Erro Grave", JOptionPane.ERROR_MESSAGE);
					finalizar();
				}
				
				
			}
		});
		
		this.setVisible(true);
	}
	
	private void logOff() {
		LoginManager.logOff();
		this.dispose();
		new LoginWindow();
	}
	
	private void finalizar() {
		this.dispose();
	}
}
