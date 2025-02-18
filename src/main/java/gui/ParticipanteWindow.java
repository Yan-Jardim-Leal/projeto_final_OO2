package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public final class ParticipanteWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel painelAtual; 
	
	public ParticipanteWindow() {
		//===================================================================//
		
		this.setSize(680, 425);
		this.setLocationRelativeTo(null);
				
		criarComponentes();
	}
	
	private void criarComponentes() {
		setTitle("Menu do Participante");
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
			String[] values = new String[] {"Buscar por nome", "Vizualizar um evento", "Eventos participando", "Confirmar Presença", "Gerar relatório XLS"};
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
				
				if (evento.getValueIsAdjusting()) {
					return;
				}

				if (painelAtual != null) {
					painelAtual.setVisible(false);
				}

				try {
					int id;

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

							Evento eventoPorId = EventoManager.getEventoPorId(id);

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
							id = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o ID do evento:", "Id do Evento", JOptionPane.QUESTION_MESSAGE));

							if (EventoManager.confirmarPresenca(id)) {
								JOptionPane.showMessageDialog(null, "Presença confirmada!","Info", JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(null, "Presença não foi confirmada.\nEvento não encontrado ou\ntalvez ainda seja muito cedo!","Info", JOptionPane.INFORMATION_MESSAGE);
							}

							break;
						case 4:
							new RelatorioWindow();
							break;
						default:
					}
				} catch (NumberFormatException erro) {
					return;
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
		System.exit(0);
	}
}
