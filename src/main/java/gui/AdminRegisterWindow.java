package gui;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.*;

public class AdminRegisterWindow extends JFrame {
    private JPanel contentPane;
    private JLabel lblNomeCompleto;
    private JTextField textFieldNomeCompleto;
    private JLabel lblEmail;
    private JTextField textFieldEmail;
    private JLabel lblSenha;
    private JPasswordField passwordField;
    private JLabel lblCargo;
    private JTextField textFieldCargo;
    private JLabel lblDataContratacao;
    private JFormattedTextField formattedTextFieldDataContratacao;
    private JButton btnRegistrar;

    public AdminRegisterWindow() {
        setTitle("Registro de Administrador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
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

        // Cargo
        lblCargo = new JLabel("Cargo:");
        lblCargo.setBounds(30, 150, 120, 25);
        textFieldCargo = new JTextField();
        textFieldCargo.setBounds(160, 150, 200, 25);

        // Data de Contratação
        lblDataContratacao = new JLabel("Data de Contratação:");
        lblDataContratacao.setBounds(30, 190, 120, 25);
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            formattedTextFieldDataContratacao = new JFormattedTextField(dateMask);
        } catch (Exception e) {
            formattedTextFieldDataContratacao = new JFormattedTextField();
        }
        formattedTextFieldDataContratacao.setBounds(160, 190, 200, 25);

        // Botão de registro
        btnRegistrar = new JButton("Registrar Admin");
        btnRegistrar.setBounds(160, 250, 150, 30);
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = textFieldNomeCompleto.getText();
                String email = textFieldEmail.getText();
                String senha = new String(passwordField.getPassword());
                String cargo = textFieldCargo.getText();
                String dataContratacao = formattedTextFieldDataContratacao.getText();

                try {
                    // Converter dataContratacao para o formato Date se necessário
                    // Exemplo:
                    // Date data = converterData(dataContratacao);
                    // Administrador admin = new Administrador(nome, senha, email, cargo, data);
                    // UserManager.cadastrarUsuario(admin);

                    JOptionPane.showMessageDialog(null, "Administrador registrado com sucesso!");
                    dispose(); // Fecha a janela após o registro
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao registrar administrador: " + ex.getMessage());
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
        contentPane.add(lblCargo);
        contentPane.add(textFieldCargo);
        contentPane.add(lblDataContratacao);
        contentPane.add(formattedTextFieldDataContratacao);
        contentPane.add(btnRegistrar);

        setContentPane(contentPane);
    }
}

