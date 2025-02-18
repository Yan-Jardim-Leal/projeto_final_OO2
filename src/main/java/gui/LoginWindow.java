package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import entities.UsuarioTipo;
import exceptions.LoginException;
import service.LoginManager;
import service.UserManager;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

public final class LoginWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JTextField emailField;
	private JPasswordField senhaField;
	
	public LoginWindow() {
		
		this.setSize(311, 178);
		this.setLocationRelativeTo(null);
		
		criarComponentes();
	}
	
	private void criarComponentes() {
		// ==========================||     ELEMENTOS     ||========================== //
		setTitle("Tela de Login");
		setResizable(false);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("Senha");
		lblNewLabel.setBounds(10, 45, 46, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("E-Mail");
		lblNewLabel_1.setBounds(10, 14, 46, 14);
		getContentPane().add(lblNewLabel_1);
		
		emailField = new JTextField();
		emailField.setBounds(66, 11, 213, 20);
		getContentPane().add(emailField);
		emailField.setColumns(10);
		
		JButton loginButton = new JButton("Login");

		loginButton.setBounds(97, 73, 89, 23);
		getContentPane().add(loginButton);
		
		JButton registerButton = new JButton("Registrar");

		registerButton.setBounds(97, 105, 89, 23);
		getContentPane().add(registerButton);
		
		senhaField = new JPasswordField();
		senhaField.setBounds(66, 42, 213, 20);
		getContentPane().add(senhaField);
		
		// ==========================||      EVENTOS      ||========================== //
		
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				login();
			}
		});
		
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				registrar();
			}
		});
		// ==========================|| ================= ||========================== //
		this.setVisible(true);
	}
	
	private void login() {
		
		try {
			LoginManager.login(emailField.getText(), String.valueOf(senhaField.getPassword()));
		} catch (LoginException erro) {
			if (!(erro.getMessage() == "Usuário já está logado."))
			JOptionPane.showMessageDialog(null, "Senha ou E-mail incorretos.","Oops.", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		this.dispose();
		//JOptionPane.showMessageDialog(null, "Login realizado com sucesso!","YaY!", JOptionPane.PLAIN_MESSAGE);
		
		if (LoginManager.getUsuario().getTipo() == UsuarioTipo.ADMIN)
			new AdministradorWindow();
		else
			new ParticipanteWindow();
		
	}
	
	private void registrar() {
		try {
			if (UserManager.usuariosMaiorQue(0))
				new RegistroParticipanteWindow(this);
			else
				System.out.println("Devemos registrar um administrador!");
		} catch (Exception erro) {
			
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao escolher que tipo de registro o usuário deve realizar,\ntente novamente mais tarde ou verifique sua conexão com o banco de dados.","Erro Grave", JOptionPane.ERROR_MESSAGE);
			finalizar();
		}
		
		setLoginVisible(false);
		return;
	}
	
	public void setLoginVisible(boolean visible) {
		this.setVisible(visible);
	}
	
	public void finalizar() {
		this.dispose();
		System.exit(0);
	}
	
}
