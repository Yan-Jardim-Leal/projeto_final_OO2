package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import com.google.protobuf.TextFormat.ParseException;
import java.awt.FlowLayout;
import java.awt.Dialog.ModalExclusionType;

public class AdminRegisterWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JLabel lblNomeCompleto;
	private JTextField textFieldNomeCompleto;
	private JLabel lblEmail;
	private JTextField textFieldEmail;
	private JLabel lblSenha;
	private JPasswordField passwordField;
	private JLabel lblCargo;
	private JFormattedTextField formattedTextFieldCargo;
	private MaskFormatter mascaraData;
	private JLabel lblDataContracao;
	private JFormattedTextField formattedTextFieldDataContracao;
	private JButton btnRegistrar;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminRegisterWindow frame = new AdminRegisterWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AdminRegisterWindow() throws ParseException, java.text.ParseException {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Sistema de Gerenciamento de Eventos - Admin");
		setBounds(100, 100, 450, 520);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mascaraData = new MaskFormatter("##/##/####");
		
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
		lblNomeCompleto.setBounds(80, 33, 102, 14);
		contentPane.add(lblNomeCompleto);
		
		lblEmail = new JLabel("E-Mail");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmail.setBounds(80, 108, 46, 14);
		contentPane.add(lblEmail);
		
		lblSenha = new JLabel("Senha");
		lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSenha.setBounds(80, 186, 46, 14);
		contentPane.add(lblSenha);

		
		formattedTextFieldCargo = new JFormattedTextField();
		formattedTextFieldCargo.setBounds(80, 285, 265, 29);
		contentPane.add(formattedTextFieldCargo);
		
		lblCargo = new JLabel("Cargo");
		lblCargo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCargo.setBounds(80, 260, 117, 14);
		contentPane.add(lblCargo);
		
		formattedTextFieldDataContracao = new JFormattedTextField(mascaraData);
		formattedTextFieldDataContracao.setBounds(80, 354, 265, 29);
		contentPane.add(formattedTextFieldDataContracao);
		
		lblDataContracao = new JLabel("Data de Contração");
		lblDataContracao.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDataContracao.setBounds(80, 331, 114, 14);
		contentPane.add(lblDataContracao);
				
		btnRegistrar = new JButton("Registrar");
		btnRegistrar.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnRegistrar.setBounds(158, 420, 100, 29);
		contentPane.add(btnRegistrar);
	}
}
