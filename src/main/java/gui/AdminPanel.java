package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Button;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class AdminPanel extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminPanel frame = new AdminPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AdminPanel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Sistema de Gerenciamento de Eventos - Painel do Admin");
		setBounds(100, 100, 800, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		
		JPanel InicioTab = new JPanel();
		tabbedPane.addTab("Início", null, InicioTab, null);
		InicioTab.setLayout(null);
		
		JPanel CriarEventoTab = new JPanel();
		tabbedPane.addTab("Criar Evento", null, CriarEventoTab, null);
		CriarEventoTab.setLayout(null);
		
		JPanel EditarEventoTab = new JPanel();
		tabbedPane.addTab("Editar Evento", null, EditarEventoTab, null);
		EditarEventoTab.setLayout(null);
		
		JPanel ExcluirEventoTab = new JPanel();
		tabbedPane.addTab("Excluir Evento", null, ExcluirEventoTab, null);
		
		ExcluirEventoTab.setLayout(null);
		
		JPanel VisualizarEventoTab = new JPanel();
		tabbedPane.addTab("Visualizar Eventos", null, VisualizarEventoTab, null);
		VisualizarEventoTab.setLayout(null);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
		);
		contentPane.setLayout(gl_contentPane);
	}
}
