package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

import entities.Administrador;
import service.UserManager;

public final class RegistroAdministradorWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JTextField nomeField;
	private JTextField emailField;
	private JPasswordField senhaField;
	
	private JTextField cargoField;
	private JFormattedTextField contratoField;
	
	private LoginWindow loginWindow;
	
	private MaskFormatter mascaraData;
	
	public RegistroAdministradorWindow(LoginWindow loginWindow) {
		this.loginWindow = loginWindow;
		
		this.setSize(452, 208);
		this.setLocationRelativeTo(null);
		
		formatarTexto();
		criarComponentes();
	}
	
	public void criarComponentes() {
		// ==========================||     ELEMENTOS     ||========================== //
		setTitle("Tela de Registro");
		setResizable(false);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		nomeField = new JTextField();
		nomeField.setBounds(84, 11, 281, 20);
		getContentPane().add(nomeField);
		nomeField.setColumns(10);
		
		emailField = new JTextField();
		emailField.setColumns(10);
		emailField.setBounds(84, 42, 281, 20);
		getContentPane().add(emailField);
		
		senhaField = new JPasswordField();
		senhaField.setBounds(84, 104, 281, 20);
		getContentPane().add(senhaField);
		
		cargoField = new JFormattedTextField();
		cargoField.setBounds(251, 73, 114, 20);
		getContentPane().add(cargoField);
		
		contratoField = new JFormattedTextField(mascaraData);
		contratoField.setBounds(84, 73, 118, 20);
		getContentPane().add(contratoField);
		
		JLabel lblNewLabel_2 = new JLabel("Nome");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2.setBounds(10, 14, 64, 14);
		getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("E-mail");
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2_1.setBounds(10, 45, 64, 14);
		getContentPane().add(lblNewLabel_2_1);
		
		JLabel lblNewLabel_2_2 = new JLabel("Cargo");
		lblNewLabel_2_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2_2.setBounds(212, 76, 29, 14);
		getContentPane().add(lblNewLabel_2_2);
		
		JLabel lblNewLabel_2_3 = new JLabel("Contratado");
		lblNewLabel_2_3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2_3.setBounds(10, 76, 64, 14);
		getContentPane().add(lblNewLabel_2_3);
		
		JLabel lblNewLabel_2_4 = new JLabel("Senha");
		lblNewLabel_2_4.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2_4.setBounds(10, 107, 64, 14);
		getContentPane().add(lblNewLabel_2_4);
		
		JButton registrarButton = new JButton("Registrar");

		registrarButton.setBounds(177, 135, 89, 23);
		getContentPane().add(registrarButton);
		
		JButton loginButton = new JButton("Login");

		loginButton.setBounds(276, 135, 89, 23);
		getContentPane().add(loginButton);
		
		this.setVisible(true);
		
		// ==========================||      EVENTOS      ||========================== //
		
		registrarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				registrar();
			}
		});
		
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				login();
			}
		});
		// ==========================|| ================= ||========================== //
	}
	
	private void registrar() {
		
		//Integer id, String nome, String senha, String email, String cpf, Date dataContrato
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		String nome = nomeField.getText();
		String email = emailField.getText();
		String senha = String.valueOf(senhaField.getPassword());
		
		//Somente do administrador:
		String cargo = cargoField.getText();
		
		Administrador administrador;
		try {
			
			Date data = new Date(sdf.parse(contratoField.getText()).getTime());
			
			administrador = new Administrador(
					null,
					nome,
					senha,
					email,
					
					cargo,
					data
					
					);
			
			UserManager.registrarUsuario(administrador);
		} catch (Exception erro) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao registrar um usuário,\ntente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
			loginWindow.setLocationRelativeTo(null);
			loginWindow.setVisible(true);
			this.dispose();
		}
	}
	
	private void login() {
		
		loginWindow.setLocationRelativeTo(null);
		loginWindow.setVisible(true);
		this.dispose();
	}
	
	private void formatarTexto() {
		try {
			
			this.mascaraData = new MaskFormatter("##/##/####");
		} catch(Exception erro) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao criar máscara para data.", "Erro máscara", JOptionPane.ERROR_MESSAGE);
			finalizar();
		}
	}
	
	private void finalizar() {
		this.dispose();
		System.exit(0);
	}
}
