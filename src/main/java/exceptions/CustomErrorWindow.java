package exceptions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomErrorWindow extends JFrame {
    public CustomErrorWindow(Throwable ex) {
        super("Erro Crítico");
        // Configura a operação de fechamento para encerrar a aplicação
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Centraliza a janela

        // Cria um rótulo com a mensagem de erro
        JLabel label = new JLabel(
            "<html><body style='text-align: center;'>Ocorreu um erro crítico:<br>" 
            + ex.getMessage() + "</body></html>", SwingConstants.CENTER);
        
        // Cria um botão para fechar o programa
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1); // Encerra o programa com código de erro
            }
        });
        
        // Organiza os componentes na janela
        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        add(btnFechar, BorderLayout.SOUTH);
    }
}

