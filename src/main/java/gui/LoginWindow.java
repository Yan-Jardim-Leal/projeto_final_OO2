package gui;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JPasswordField;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblEmail;
	private JTextField txtFieldEmail;
	private JLabel lblSenha;
	private JPasswordField passwordFieldSenha;
	private JButton btnRegistrar;
	private JButton btnLogin;
	private RegisterWindow registerWindow;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow frame = new LoginWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public LoginWindow() {
		this.setTitle("Sistema de Gerenciamento de Eventos - Login");
		this.setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblEmail = new JLabel("E-Mail");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmail.setBounds(81, 36, 46, 14);
		contentPane.add(lblEmail);
		
		txtFieldEmail = new JTextField();
		txtFieldEmail.setBounds(81, 61, 265, 29);
		contentPane.add(txtFieldEmail);
		txtFieldEmail.setColumns(15);
		
		lblSenha = new JLabel("Senha");
		lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSenha.setBounds(81, 104, 46, 14);
		contentPane.add(lblSenha);
		
		passwordFieldSenha = new JPasswordField();
		passwordFieldSenha.setBounds(81, 133, 265, 29);
		contentPane.add(passwordFieldSenha);
		
		btnRegistrar = new JButton("Registrar");
		btnRegistrar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				criarRegisterWindow();
		}});
		btnRegistrar.setBounds(81, 198, 89, 25);
		contentPane.add(btnRegistrar);
		
		btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLogin.setBounds(257, 198, 89, 25);
		contentPane.add(btnLogin);
		
	}
	
	private void criarRegisterWindow() {
		registerWindow = null;
		try {
			registerWindow = new RegisterWindow();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		registerWindow.setVisible(true);
	}
}
