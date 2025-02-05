package gui;

import javax.swing.*;
import java.awt.event.*;

public class ParticipantScreen extends JFrame {
    private JPanel contentPane;
    private JLabel lblWelcome;
    private JButton btnParticipateEvent;
    private JButton btnConfirmPresence;
    private JButton btnExitEvent;
    
    public ParticipantScreen() {
        setTitle("Painel do Participante");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null); // centraliza a janela
        
        contentPane = new JPanel();
        contentPane.setLayout(null);
        
        // Rótulo de boas-vindas
        lblWelcome = new JLabel("Bem-vindo, Participante!");
        lblWelcome.setBounds(150, 20, 200, 30);
        contentPane.add(lblWelcome);
        
        // Botão para participar do evento
        btnParticipateEvent = new JButton("Participar do Evento");
        btnParticipateEvent.setBounds(150, 70, 200, 30);
        btnParticipateEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aqui você deve chamar o método do EventoManager para participar do evento
                JOptionPane.showMessageDialog(null, "Você se inscreveu no evento!");
            }
        });
        contentPane.add(btnParticipateEvent);
        
        // Botão para confirmar presença
        btnConfirmPresence = new JButton("Confirmar Presença");
        btnConfirmPresence.setBounds(150, 120, 200, 30);
        btnConfirmPresence.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chamada ao método do EventoManager para confirmar presença
                JOptionPane.showMessageDialog(null, "Presença confirmada!");
            }
        });
        contentPane.add(btnConfirmPresence);
        
        // Botão para sair do evento
        btnExitEvent = new JButton("Sair do Evento");
        btnExitEvent.setBounds(150, 170, 200, 30);
        btnExitEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chamada ao método do EventoManager para sair do evento
                JOptionPane.showMessageDialog(null, "Você saiu do evento!");
            }
        });
        contentPane.add(btnExitEvent);
        
        setContentPane(contentPane);
    }
    
    public static void main(String[] args) {
        // Garantir que a GUI seja criada na Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new ParticipantScreen().setVisible(true));
    }
}
