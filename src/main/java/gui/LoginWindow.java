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
	private JButton btnZero;
	private JTextField txtFieldUsuario;
	private JPasswordField passwordFieldSenha;

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
		
		txtFieldUsuario = new JTextField();
		txtFieldUsuario.setBounds(81, 61, 265, 29);
		contentPane.add(txtFieldUsuario);
		txtFieldUsuario.setColumns(15);
		
		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSenha.setBounds(81, 104, 46, 14);
		contentPane.add(lblSenha);
		
		JLabel lblEmail = new JLabel("E-Mail");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmail.setBounds(81, 36, 46, 14);
		contentPane.add(lblEmail);
		
		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegisterWindow registerWindow = null;
				try {
					registerWindow = new RegisterWindow();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				registerWindow.setVisible(true);
		}});
		btnRegistrar.setBounds(81, 198, 89, 25);
		contentPane.add(btnRegistrar);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLogin.setBounds(257, 198, 89, 25);
		contentPane.add(btnLogin);
		
		passwordFieldSenha = new JPasswordField();
		passwordFieldSenha.setBounds(81, 133, 265, 29);
		contentPane.add(passwordFieldSenha);
	}
}
