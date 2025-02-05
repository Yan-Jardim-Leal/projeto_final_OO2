package gui;

import javax.swing.*;
import java.awt.event.*;

public class AdminScreen extends JFrame {
    private JPanel contentPane;
    private JLabel lblWelcome;
    private JButton btnCreateEvent;
    private JButton btnEditEvent;
    private JButton btnDeleteEvent;
    private JButton btnPrintXLS;
    
    public AdminScreen() {
        setTitle("Painel do Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);
        setLocationRelativeTo(null); // centraliza a janela
        
        contentPane = new JPanel();
        contentPane.setLayout(null);
        
        // Rótulo de boas-vindas
        lblWelcome = new JLabel("Bem-vindo, Administrador!");
        lblWelcome.setBounds(200, 20, 200, 30);
        contentPane.add(lblWelcome);
        
        // Botão para criar evento
        btnCreateEvent = new JButton("Criar Evento");
        btnCreateEvent.setBounds(200, 70, 200, 30);
        btnCreateEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chame o método do EventoManager para criar evento
                JOptionPane.showMessageDialog(null, "Evento criado com sucesso!");
            }
        });
        contentPane.add(btnCreateEvent);
        
        // Botão para editar evento
        btnEditEvent = new JButton("Editar Evento");
        btnEditEvent.setBounds(200, 120, 200, 30);
        btnEditEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chame o método do EventoManager para editar evento
                JOptionPane.showMessageDialog(null, "Evento editado com sucesso!");
            }
        });
        contentPane.add(btnEditEvent);
        
        // Botão para excluir evento
        btnDeleteEvent = new JButton("Excluir Evento");
        btnDeleteEvent.setBounds(200, 170, 200, 30);
        btnDeleteEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chame o método do EventoManager para excluir evento
                JOptionPane.showMessageDialog(null, "Evento excluído com sucesso!");
            }
        });
        contentPane.add(btnDeleteEvent);
        
        // Botão para imprimir relatório XLS
        btnPrintXLS = new JButton("Imprimir Evento (XLS)");
        btnPrintXLS.setBounds(200, 220, 200, 30);
        btnPrintXLS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chame o método do EventoManager para gerar relatório XLS
                JOptionPane.showMessageDialog(null, "Relatório XLS gerado com sucesso!");
            }
        });
        contentPane.add(btnPrintXLS);
        
        setContentPane(contentPane);
    }
    
    public static void main(String[] args) {
        // Cria a GUI na Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new AdminScreen().setVisible(true));
    }
}
