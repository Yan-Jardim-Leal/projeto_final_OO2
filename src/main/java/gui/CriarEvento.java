package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import entities.Administrador;
import entities.Evento;
import entities.EventoCategoria;
import entities.EventoStatus;
import entities.Participante;
import exceptions.LoginException;
import service.EventoManager;
import service.LoginManager;

public final class CriarEvento extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextField tituloField;
    private JTextArea descricaoArea;
    private JTextField dataField;
    private JTextField horaField;
    private JTextField duracaoHorasField;
    private JTextField duracaoMinutosField;
    private JTextField localField;
    private JTextField capacidadeField;
    private JTextField precoField;
    private JComboBox<EventoCategoria> categoriaComboBox;
    private JComboBox<EventoStatus> statusComboBox;
    private JButton criarButton;
    private JButton cancelarButton;
    private JLabel isLinkLabel;
    private JComboBox<Boolean> isLinkComboBox;

    public CriarEvento() {
        criarComponentes();
    }

    private void criarComponentes() {
        setLayout(null);
        setBounds(138, 11, 515, 342);

        JLabel tituloLabel = new JLabel("Título:");
        tituloLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        tituloLabel.setBounds(10, 10, 80, 14);
        add(tituloLabel);

        tituloField = new JTextField();
        tituloField.setBounds(100, 7, 405, 20);
        add(tituloField);
        tituloField.setColumns(10);

        JLabel descricaoLabel = new JLabel("Descrição:");
        descricaoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        descricaoLabel.setBounds(10, 35, 80, 14);
        add(descricaoLabel);

        descricaoArea = new JTextArea();
        descricaoArea.setBounds(100, 32, 405, 51);
        descricaoArea.setLineWrap(true);
        descricaoArea.setWrapStyleWord(true);
        add(descricaoArea);

        JLabel dataLabel = new JLabel("Data (dd/mm/aaaa):");
        dataLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dataLabel.setBounds(295, 94, 120, 14);
        add(dataLabel);

        dataField = new JTextField();
        dataField.setBounds(425, 91, 80, 20);
        add(dataField);
        dataField.setColumns(10);

        JLabel horaLabel = new JLabel("Hora (hh:mm):");
        horaLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        horaLabel.setBounds(325, 122, 90, 14);
        add(horaLabel);

        horaField = new JTextField();
        horaField.setBounds(425, 119, 80, 20);
        add(horaField);
        horaField.setColumns(10);

        JLabel duracaoLabel = new JLabel("Duração:");
        duracaoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        duracaoLabel.setBounds(10, 125, 80, 14);
        add(duracaoLabel);

        duracaoHorasField = new JTextField();
        duracaoHorasField.setBounds(100, 122, 40, 20);
        add(duracaoHorasField);
        duracaoHorasField.setColumns(10);

        JLabel horasLabel = new JLabel("horas e");
        horasLabel.setBounds(145, 125, 40, 14);
        add(horasLabel);

        duracaoMinutosField = new JTextField();
        duracaoMinutosField.setBounds(185, 122, 40, 20);
        add(duracaoMinutosField);
        duracaoMinutosField.setColumns(10);

        JLabel minutosLabel = new JLabel("minutos");
        minutosLabel.setBounds(230, 125, 50, 14);
        add(minutosLabel);

        JLabel localLabel = new JLabel("Local/Link:");
        localLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        localLabel.setBounds(10, 150, 80, 14);
        add(localLabel);

        localField = new JTextField();
        localField.setBounds(100, 147, 405, 20);
        add(localField);
        localField.setColumns(10);

        JLabel capacidadeLabel = new JLabel("Capacidade:");
        capacidadeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        capacidadeLabel.setBounds(335, 179, 80, 14);
        add(capacidadeLabel);

        capacidadeField = new JTextField();
        capacidadeField.setBounds(425, 176, 80, 20);
        add(capacidadeField);
        capacidadeField.setColumns(10);

        JLabel categoriaLabel = new JLabel("Categoria:");
        categoriaLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        categoriaLabel.setBounds(10, 210, 80, 14);
        add(categoriaLabel);

        categoriaComboBox = new JComboBox<>(EventoCategoria.values());
        categoriaComboBox.setBounds(100, 207, 150, 22);
        add(categoriaComboBox);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusLabel.setBounds(45, 240, 50, 14);
        add(statusLabel);

        statusComboBox = new JComboBox<>(EventoStatus.values());
        statusComboBox.setBounds(100, 237, 150, 22);
        add(statusComboBox);

        JLabel precoLabel = new JLabel("Preço:");
        precoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        precoLabel.setBounds(335, 242, 80, 14);
        add(precoLabel);

        precoField = new JTextField();
        precoField.setBounds(425, 239, 80, 20);
        add(precoField);
        precoField.setColumns(10);

        criarButton = new JButton("Criar Evento");
        criarButton.setBounds(100, 282, 120, 23);
        add(criarButton);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.setBounds(230, 282, 100, 23);
        add(cancelarButton);

        JLabel lblNewLabel = new JLabel("Criar Evento");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(170, 0, 180, 30);
        add(lblNewLabel);

        isLinkLabel = new JLabel("É Link?");
        isLinkLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        isLinkLabel.setBounds(10, 182, 80, 14);
        add(isLinkLabel);

        isLinkComboBox = new JComboBox<>(new Boolean[] {false, true});
        isLinkComboBox.setBounds(100, 177, 150, 22);
        add(isLinkComboBox);


        criarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (cadastrarEvento()) {
                        JOptionPane.showMessageDialog(null, "Evento criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        limparFormulario();
                    }
                } catch (SQLException | LoginException | IllegalArgumentException | DateTimeParseException erro) {
                    JOptionPane.showMessageDialog(null, "Erro ao criar evento: " + erro.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    erro.printStackTrace(); // Para debug mais detalhado no console
                } catch (Exception erro) {
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

    private boolean cadastrarEvento() throws Exception {
        Evento evento = extrairEvento();
        if (evento != null) {
            return EventoManager.criarEvento(evento);
        }
        return false;
    }

    private Evento extrairEvento() throws IllegalArgumentException, DateTimeParseException {
        String titulo = tituloField.getText();
        String descricao = descricaoArea.getText();
        String dataTexto = dataField.getText();
        String horaTexto = horaField.getText();
        String duracaoHorasTexto = duracaoHorasField.getText();
        String duracaoMinutosTexto = duracaoMinutosField.getText();
        String local = localField.getText();
        String capacidadeTexto = capacidadeField.getText();
        String precoTexto = precoField.getText();
        EventoCategoria categoria = (EventoCategoria) categoriaComboBox.getSelectedItem();
        EventoStatus status = (EventoStatus) statusComboBox.getSelectedItem();
        boolean isLink = (boolean) isLinkComboBox.getSelectedItem();

        if (titulo.trim().isEmpty() || dataTexto.trim().isEmpty() || horaTexto.trim().isEmpty() || local.trim().isEmpty() || capacidadeTexto.trim().isEmpty() || precoTexto.trim().isEmpty()
        		|| duracaoHorasTexto.trim().isEmpty() || duracaoMinutosTexto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos os campos com * são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return null; // Retorna null se campos obrigatórios estiverem vazios
        }

        Date dataEvento;
        Time horaEvento;
        Duration duracaoEvento;
        int capacidadeMaxima;
        double preco;

        try {
            LocalDate localDate = LocalDate.parse(dataTexto, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            dataEvento = Date.valueOf(localDate);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de data inválido. Use dd/mm/aaaa.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            throw e; // Re-lança a exceção para ser capturada no ActionListener
        }

        try {
            LocalTime localTime = LocalTime.parse(horaTexto, java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
            horaEvento = Time.valueOf(localTime);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de hora inválido. Use hh:mm.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            throw e; // Re-lança a exceção
        }
        try {
            int horas = Integer.parseInt(duracaoHorasTexto);
            int minutos = Integer.parseInt(duracaoMinutosTexto);
            duracaoEvento = Duration.ofHours(horas).plusMinutes(minutos);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Duração inválida. Use números inteiros para horas e minutos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return null;
        }


        try {
            capacidadeMaxima = Integer.parseInt(capacidadeTexto);
            if (capacidadeMaxima <= 0) {
                JOptionPane.showMessageDialog(null, "Capacidade deve ser um número positivo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Capacidade inválida. Use um número inteiro.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        try {
            preco = Double.parseDouble(precoTexto);
            if (preco < 0) {
                JOptionPane.showMessageDialog(null, "Preço não pode ser negativo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Preço inválido. Use um número.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Inicializa HashMap para organizadores e participantes (vazios no momento da criação)
        HashMap<Integer, Administrador> organizadores = new HashMap<>();
        HashMap<Integer, Participante> participantes = new HashMap<>();
        organizadores.put(LoginManager.getUsuario().getId(), (Administrador) LoginManager.getUsuario()); //Adiciona o admin logado como organizador inicial.

        Evento evento = new Evento(
                null, // ID é auto-incremento no banco
                titulo,
                descricao,
                dataEvento,
                horaEvento,
                duracaoEvento,
                local,
                isLink,
                capacidadeMaxima,
                categoria,
                status,
                preco,
                organizadores,
                participantes
        );
        return evento;
    }

    private void limparFormulario() {
        tituloField.setText("");
        descricaoArea.setText("");
        dataField.setText("");
        horaField.setText("");
        duracaoHorasField.setText("");
        duracaoMinutosField.setText("");
        localField.setText("");
        capacidadeField.setText("");
        precoField.setText("");
        categoriaComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        isLinkComboBox.setSelectedIndex(0);
    }
}