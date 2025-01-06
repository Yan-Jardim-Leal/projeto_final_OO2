package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JList;
import javax.swing.JFormattedTextField;
import javax.swing.JPasswordField;
import java.awt.Scrollbar;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.TextField;
import java.sql.Date;
import java.text.ParseException;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegisterWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblNomeCompleto;
	private JTextField textFieldNomeCompleto;
	private JLabel lblEmail;
	private JTextField textFieldEmail;
	private JLabel lblSenha;
	private JPasswordField passwordField;
	private JLabel lblDataNascimento;
	private JFormattedTextField formattedTextFieldDataNascimento;
	private MaskFormatter mascaraData;
	private JLabel lblCpf;
	private JFormattedTextField formattedTextFieldCpf;
	private MaskFormatter mascaraCpf;
	private JLabel lblTipoUsuario;
	private JPasswordField passwordFieldSenhaMestra;
	private JLabel lblSenhaMestra;
	private JButton btnRegistrar;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterWindow frame = new RegisterWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public RegisterWindow() throws ParseException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Sistema de Gerenciamento de Eventos - Registrar");
		setBounds(100, 100, 450, 620);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.mascaraData = new MaskFormatter("##/##/####");
		this.mascaraCpf = new MaskFormatter("###.###.###-##");

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textFieldNomeCompleto = new JTextField();
		textFieldNomeCompleto.setBounds(80, 59, 265, 29);
		contentPane.add(textFieldNomeCompleto);
		textFieldNomeCompleto.setColumns(10);
		
		textFieldEmail = new JTextField();
		textFieldEmail.setBounds(79, 132, 265, 29);
		contentPane.add(textFieldEmail);
		textFieldEmail.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(79, 208, 265, 29);
		contentPane.add(passwordField);
		
		lblNomeCompleto = new JLabel("Nome Completo");
		lblNomeCompleto.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNomeCompleto.setBounds(79, 33, 102, 14);
		contentPane.add(lblNomeCompleto);
		
		lblEmail = new JLabel("E-Mail");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmail.setBounds(78, 108, 46, 14);
		contentPane.add(lblEmail);
		
		lblSenha = new JLabel("Senha");
		lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSenha.setBounds(80, 188, 46, 14);
		contentPane.add(lblSenha);

		
		formattedTextFieldDataNascimento = new JFormattedTextField(mascaraData);
		formattedTextFieldDataNascimento.setBounds(80, 285, 265, 29);
		contentPane.add(formattedTextFieldDataNascimento);
		
		lblDataNascimento = new JLabel("Data de Nascimento");
		lblDataNascimento.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDataNascimento.setBounds(80, 260, 117, 14);
		contentPane.add(lblDataNascimento);
		
		formattedTextFieldCpf = new JFormattedTextField(mascaraCpf);
		formattedTextFieldCpf.setBounds(80, 354, 265, 29);
		contentPane.add(formattedTextFieldCpf);
		
		lblCpf = new JLabel("CPF");
		lblCpf.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCpf.setBounds(78, 335, 46, 14);
		contentPane.add(lblCpf);
		
		lblTipoUsuario = new JLabel("Tipo de Usuário");
		lblTipoUsuario.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTipoUsuario.setBounds(80, 402, 86, 14);
		contentPane.add(lblTipoUsuario);
		
		btnRegistrar = new JButton("Registrar");
		btnRegistrar.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnRegistrar.setBounds(158, 505, 100, 29);
		contentPane.add(btnRegistrar);
		
		JComboBox<String> comboBoxTipoUsuario = new JComboBox<String>();
		comboBoxTipoUsuario.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxTipoUsuario.setModel(new DefaultComboBoxModel<>(new String[] {"Participante", "Administrador"}));
		comboBoxTipoUsuario.setMaximumRowCount(2);
		comboBoxTipoUsuario.setBounds(80, 427, 265, 29);
		contentPane.add(comboBoxTipoUsuario);
		comboBoxTipoUsuario.addActionListener(new ActionListener() {

		private boolean senhaMestraAdicionada = false;	
			
		@Override
		public void actionPerformed(ActionEvent e) {
			if (comboBoxTipoUsuario.getSelectedIndex() == 0){
					contentPane.remove(passwordFieldSenhaMestra);
					contentPane.remove(lblSenhaMestra);
							
					setBounds(100, 100, 450, 620);
					btnRegistrar.setBounds(158, 505, 100, 29);	
					senhaMestraAdicionada = false;
					contentPane.repaint();
			} else if(comboBoxTipoUsuario.getSelectedIndex() == 1 && senhaMestraAdicionada == false) {
					lblSenhaMestra = new JLabel("Senha Mestra");
					lblSenhaMestra.setFont(new Font("Tahoma", Font.PLAIN, 12));
					lblSenhaMestra.setBounds(80, 483, 84, 14);
					contentPane.add(lblSenhaMestra);
							
					passwordFieldSenhaMestra = new JPasswordField();
					passwordFieldSenhaMestra.setBounds(80, 502, 265, 29);
					contentPane.add(passwordFieldSenhaMestra);
							
					setBounds(100, 100, 450, 700);
					btnRegistrar.setBounds(158, 579, 100, 29);
					senhaMestraAdicionada = true;
					contentPane.repaint();
				}
			}
			
		});	
	}

}