package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import entities.Administrador;
import exceptions.LoginException;
import service.UserManager;

public final class CriarAdministrador extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextField nomeField;
    private JTextField emailField;
    private JPasswordField senhaField;
    private JTextField cargoField;
    private JTextField dataContratacaoField;
    private JButton criarButton;
    private JButton cancelarButton;

    public CriarAdministrador() {
        criarComponentes();
    }

    private void criarComponentes() {
        setLayout(null);
        setBounds(138, 11, 515, 342);

        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setBounds(10, 10, 80, 14);
        add(nomeLabel);

        nomeField = new JTextField();
        nomeField.setBounds(100, 7, 405, 20);
        add(nomeField);
        nomeField.setColumns(10);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 66, 80, 14);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(100, 63, 405, 20);
        add(emailField);
        emailField.setColumns(10);

        JLabel senhaLabel = new JLabel("Senha:");
        senhaLabel.setBounds(10, 38, 80, 14);
        add(senhaLabel);

        senhaField = new JPasswordField();
        senhaField.setBounds(100, 35, 150, 20);
        add(senhaField);
        senhaField.setColumns(10);

        JLabel cargoLabel = new JLabel("Cargo:");
        cargoLabel.setBounds(270, 38, 80, 14);
        add(cargoLabel);

        cargoField = new JTextField();
        cargoField.setBounds(355, 35, 150, 20);
        add(cargoField);
        cargoField.setColumns(10);

        JLabel dataContratacaoLabel = new JLabel("Data Contratação (dd/mm/aaaa):");
        dataContratacaoLabel.setBounds(170, 94, 180, 14);
        add(dataContratacaoLabel);

        dataContratacaoField = new JTextField();
        dataContratacaoField.setBounds(355, 91, 150, 20);
        add(dataContratacaoField);
        dataContratacaoField.setColumns(10);

        criarButton = new JButton("Criar Administrador");
        criarButton.setBounds(245, 127, 150, 23);
        add(criarButton);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.setBounds(405, 127, 100, 23);
        add(cancelarButton);

        JLabel titleLabel = new JLabel("Criar Administrador");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(170, 0, 180, 30);
        add(titleLabel);


        criarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (cadastrarAdministrador()) {
                        JOptionPane.showMessageDialog(null, "Administrador criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        limparFormulario();
                    }
                } catch (SQLException | LoginException | IllegalArgumentException | DateTimeParseException erro) {
                    JOptionPane.showMessageDialog(null, "Erro ao criar administrador: " + erro.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    erro.printStackTrace(); // Para debug mais detalhado no console
                } catch (Exception erro) {
                    JOptionPane.showMessageDialog(null, "Erro inesperado ao criar administrador: " + erro.getMessage(), "Erro Inesperado", JOptionPane.ERROR_MESSAGE);
                    erro.printStackTrace();
                }
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limparFormulario();
            }
        });

        this.setVisible(true);
    }

    private boolean cadastrarAdministrador() throws SQLException, LoginException, IllegalArgumentException, DateTimeParseException, Exception {
        Administrador administrador = extrairAdministrador();
        if (administrador != null) {
            UserManager.cadastrarAdministrador(administrador);
            return true;
        }
        return false;
    }

    private Administrador extrairAdministrador() throws IllegalArgumentException, DateTimeParseException {
        String nome = nomeField.getText();
        String email = emailField.getText();
        String senha = new String(senhaField.getPassword());
        String cargo = cargoField.getText();
        String dataContratacaoTexto = dataContratacaoField.getText();

        if (nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty() || cargo.trim().isEmpty() || dataContratacaoTexto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos os campos com * são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return null; // Retorna null se campos obrigatórios estiverem vazios
        }

        Date dataContratacao;

        try {
            LocalDate localDate = LocalDate.parse(dataContratacaoTexto, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            dataContratacao = Date.valueOf(localDate);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de data de contratação inválido. Use dd/mm/aaaa.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            throw e; // Re-lança a exceção para ser capturada no ActionListener
        }

        Administrador administrador = new Administrador(
                null, // ID é auto-incremento no banco
                nome,
                senha,
                email,
                cargo,
                dataContratacao
        );
        return administrador;
    }

    private void limparFormulario() {
        this.setVisible(false);
    }
}