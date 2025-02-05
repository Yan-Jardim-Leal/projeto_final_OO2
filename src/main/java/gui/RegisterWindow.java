package gui;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.*;

public class RegisterWindow extends JFrame {
    private JPanel contentPane;
    private JLabel lblNomeCompleto;
    private JTextField textFieldNomeCompleto;
    private JLabel lblEmail;
    private JTextField textFieldEmail;
    private JLabel lblSenha;
    private JPasswordField passwordField;
    private JLabel lblDataNascimento;
    private JFormattedTextField formattedTextFieldDataNascimento;
    private JLabel lblCpf;
    private JFormattedTextField formattedTextFieldCpf;
    private JButton btnRegistrar;

    public RegisterWindow() {
        setTitle("Registro de Usuário");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setLayout(null);

        // Nome Completo
        lblNomeCompleto = new JLabel("Nome Completo:");
        lblNomeCompleto.setBounds(30, 30, 120, 25);
        textFieldNomeCompleto = new JTextField();
        textFieldNomeCompleto.setBounds(160, 30, 200, 25);

        // Email
        lblEmail = new JLabel("Email:");
        lblEmail.setBounds(30, 70, 120, 25);
        textFieldEmail = new JTextField();
        textFieldEmail.setBounds(160, 70, 200, 25);

        // Senha
        lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(30, 110, 120, 25);
        passwordField = new JPasswordField();
        passwordField.setBounds(160, 110, 200, 25);

        // Data de Nascimento
        lblDataNascimento = new JLabel("Data de Nascimento:");
        lblDataNascimento.setBounds(30, 150, 120, 25);
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            formattedTextFieldDataNascimento = new JFormattedTextField(dateMask);
        } catch (Exception e) {
            formattedTextFieldDataNascimento = new JFormattedTextField();
        }
        formattedTextFieldDataNascimento.setBounds(160, 150, 200, 25);

        // CPF
        lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(30, 190, 120, 25);
        try {
            MaskFormatter cpfMask = new MaskFormatter("###########");
            formattedTextFieldCpf = new JFormattedTextField(cpfMask);
        } catch (Exception e) {
            formattedTextFieldCpf = new JFormattedTextField();
        }
        formattedTextFieldCpf.setBounds(160, 190, 200, 25);

        // Botão de registro
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(160, 240, 120, 30);
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = textFieldNomeCompleto.getText();
                String email = textFieldEmail.getText();
                String senha = new String(passwordField.getPassword());
                String dataNascimento = formattedTextFieldDataNascimento.getText();
                String cpf = formattedTextFieldCpf.getText();

                // Aqui você deve converter a data de nascimento e validar os dados conforme necessário.
                // Em seguida, chame o método do UserManager para cadastrar o usuário.
                try {
                    // Exemplo:
                    // Participante participante = new Participante(nome, senha, email, cpf, converterData(dataNascimento));
                    // UserManager.cadastrarUsuario(participante);
                    JOptionPane.showMessageDialog(null, "Registro realizado com sucesso!");
                    dispose(); // fecha a janela após o cadastro
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao registrar: " + ex.getMessage());
                }
            }
        });

        // Adiciona os componentes ao painel
        contentPane.add(lblNomeCompleto);
        contentPane.add(textFieldNomeCompleto);
        contentPane.add(lblEmail);
        contentPane.add(textFieldEmail);
        contentPane.add(lblSenha);
        contentPane.add(passwordField);
        contentPane.add(lblDataNascimento);
        contentPane.add(formattedTextFieldDataNascimento);
        contentPane.add(lblCpf);
        contentPane.add(formattedTextFieldCpf);
        contentPane.add(btnRegistrar);

        setContentPane(contentPane);
    }
}
