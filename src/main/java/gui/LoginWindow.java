package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import entities.User;
import entities.UsuarioTipo;
import service.LoginManager;
import service.UserManager;

public class LoginWindow extends JFrame {
    private JPanel contentPane;
    private JLabel lblEmail;
    private JTextField txtFieldEmail;
    private JLabel lblSenha;
    private JPasswordField passwordFieldSenha;
    private JButton btnRegistrar;
    private JButton btnLogin;
    
    // Referência para a janela de registro
    private RegisterWindow registerWindow;

    public LoginWindow() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null); // centraliza a janela

        contentPane = new JPanel();
        contentPane.setLayout(null);

        // Rótulo e campo de email
        lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 50, 80, 25);
        txtFieldEmail = new JTextField();
        txtFieldEmail.setBounds(150, 50, 180, 25);

        // Rótulo e campo de senha
        lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(50, 90, 80, 25);
        passwordFieldSenha = new JPasswordField();
        passwordFieldSenha.setBounds(150, 90, 180, 25);

        // Botão de login
        btnLogin = new JButton("Login");
        btnLogin.setBounds(150, 130, 90, 25);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtFieldEmail.getText();
                String senha = new String(passwordFieldSenha.getPassword());
                try {
                    // Chama o método de login do seu LoginManager
                    // Exemplo: LoginManager.login(email, senha);
                    
                    // Se o login for bem-sucedido, abra a próxima tela do sistema ou continue o fluxo
                    
                    LoginManager.login(email, senha);
                    
                    User usuarioLogado = LoginManager.getUsuario();
                    
                    if (usuarioLogado != null && usuarioLogado.getTipo() == UsuarioTipo.ADMIN) {
                        new AdminScreen().setVisible(true);
                    } else {
                        new ParticipantScreen().setVisible(true);
                    }

                    
                    dispose(); // fecha a tela de login
                    JOptionPane.showMessageDialog(null, "Login realizado com sucesso!");
                } catch (Exception ex) { // troque pela exceção específica do seu sistema
                    JOptionPane.showMessageDialog(null, "Erro no login: " + ex.getMessage());
                }
            }
        });

        // Botão para abrir a tela de registro
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(240, 130, 90, 25);
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Verifica se há algum usuário cadastrado
                    if (!UserManager.usuariosMaiorQue(0)) {
                        // Nenhum usuário cadastrado: abre a janela de registro de administrador
                        new AdminRegisterWindow().setVisible(true);
                    } else {
                        // Existem usuários: abre a janela de registro normal
                        new RegisterWindow().setVisible(true);
                    }
                } catch (Exception erroVerificar) {
                    JOptionPane.showMessageDialog(null, "Erro ao verificar a quantidade de usuários: " + erroVerificar.getMessage());
                }
            }
        });


        // Adiciona os componentes ao painel
        contentPane.add(lblEmail);
        contentPane.add(txtFieldEmail);
        contentPane.add(lblSenha);
        contentPane.add(passwordFieldSenha);
        contentPane.add(btnLogin);
        contentPane.add(btnRegistrar);

        setContentPane(contentPane);
    }
    
    public static void main(String[] args) {
        // Garanta que a criação e manipulação dos componentes ocorra na thread de eventos
        SwingUtilities.invokeLater(() -> {
            new LoginWindow().setVisible(true);
        });
    }
}
