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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import entities.Evento;
import entities.EventoCategoria;
import entities.EventoStatus;
import exceptions.LoginException;
import service.EventoManager;

public final class EditarEvento extends JPanel {
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
    private JButton salvarButton; // Changed from criarButton to salvarButton
    private JButton cancelarButton;
    private JLabel isLinkLabel;
    private JComboBox<Boolean> isLinkComboBox;

    private Evento eventoParaEditar; // To hold the Evento object being edited

    public EditarEvento(Evento evento) { // Constructor now takes an Evento object
        this.eventoParaEditar = evento;
        criarComponentes();
        preencherFormulario(evento); // Populate form with event data
    }

    private void criarComponentes() {
        setLayout(null);
        setBounds(138, 11, 515, 342);

        JLabel tituloLabel = new JLabel("Título:");
        tituloLabel.setBounds(10, 10, 80, 14);
        add(tituloLabel);

        tituloField = new JTextField();
        tituloField.setBounds(100, 7, 405, 20);
        add(tituloField);
        tituloField.setColumns(10);

        JLabel descricaoLabel = new JLabel("Descrição:");
        descricaoLabel.setBounds(10, 35, 80, 14);
        add(descricaoLabel);

        descricaoArea = new JTextArea();
        descricaoArea.setBounds(100, 32, 405, 60);
        descricaoArea.setLineWrap(true);
        descricaoArea.setWrapStyleWord(true);
        add(descricaoArea);

        JLabel dataLabel = new JLabel("Data (dd/mm/aaaa):");
        dataLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dataLabel.setBounds(10, 100, 120, 14);
        add(dataLabel);

        dataField = new JTextField();
        dataField.setBounds(140, 97, 80, 20);
        add(dataField);
        dataField.setColumns(10);

        JLabel horaLabel = new JLabel("Hora (hh:mm):");
        horaLabel.setBounds(230, 100, 90, 14);
        add(horaLabel);

        horaField = new JTextField();
        horaField.setBounds(330, 97, 80, 20);
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

        JLabel horasLabel = new JLabel("horas");
        horasLabel.setBounds(145, 125, 40, 14);
        add(horasLabel);

        duracaoMinutosField = new JTextField();
        duracaoMinutosField.setBounds(185, 122, 40, 20);
        add(duracaoMinutosField);
        duracaoMinutosField.setColumns(10);

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
        capacidadeLabel.setBounds(10, 175, 80, 14);
        add(capacidadeLabel);

        capacidadeField = new JTextField();
        capacidadeField.setBounds(100, 172, 80, 20);
        add(capacidadeField);
        capacidadeField.setColumns(10);

        JLabel categoriaLabel = new JLabel("Categoria:");
        categoriaLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        categoriaLabel.setBounds(10, 202, 80, 14);
        add(categoriaLabel);

        categoriaComboBox = new JComboBox<>(EventoCategoria.values());
        categoriaComboBox.setBounds(100, 199, 150, 22);
        add(categoriaComboBox);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusLabel.setBounds(10, 233, 80, 14);
        add(statusLabel);

        statusComboBox = new JComboBox<>(EventoStatus.values());
        statusComboBox.setBounds(100, 229, 150, 22);
        add(statusComboBox);

        JLabel precoLabel = new JLabel("Preço:");
        precoLabel.setBounds(310, 253, 80, 14);
        add(precoLabel);

        precoField = new JTextField();
        precoField.setBounds(400, 250, 80, 20);
        add(precoField);
        precoField.setColumns(10);

        salvarButton = new JButton("Salvar Evento"); // Changed text to "Salvar Evento"
        salvarButton.setBounds(100, 290, 120, 23);
        add(salvarButton);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.setBounds(230, 290, 100, 23);
        add(cancelarButton);

        JLabel lblNewLabel = new JLabel("Editar Evento"); // Changed label to "Editar Evento"
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(170, 0, 180, 30);
        add(lblNewLabel);

        isLinkLabel = new JLabel("É Link?");
        isLinkLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        isLinkLabel.setBounds(10, 262, 80, 14);
        add(isLinkLabel);

        isLinkComboBox = new JComboBox<>(new Boolean[] {false, true});
        isLinkComboBox.setBounds(100, 257, 150, 22);
        add(isLinkComboBox);


        salvarButton.addActionListener(new ActionListener() { // Changed to salvarButton
            public void actionPerformed(ActionEvent e) {
                try {
                    if (salvarEvento()) { // Changed to salvarEvento method
                        JOptionPane.showMessageDialog(null, "Evento atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        limparFormulario();
                    }
                } catch (SQLException | LoginException | IllegalArgumentException | DateTimeParseException erro) {
                    JOptionPane.showMessageDialog(null, "Erro ao atualizar evento: " + erro.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    erro.printStackTrace(); // Para debug mais detalhado no console
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

    private boolean salvarEvento() throws SQLException, LoginException, IllegalArgumentException, DateTimeParseException { // Renamed to salvarEvento
        Evento eventoEditado = extrairEvento();
        if (eventoEditado != null) {
            eventoEditado.setId(eventoParaEditar.getId()); // Keep the original ID
            return EventoManager.editarEvento(eventoEditado); // Call editarEvento instead of criarEvento
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

        Evento evento = new Evento(
                eventoParaEditar.getId(), // Keep the original ID here as well, though it's set again in salvarEvento for clarity
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
                eventoParaEditar.getOrganizadores(), // Keep existing organizers
                eventoParaEditar.getParticipantes()  // Keep existing participants
        );
        return evento;
    }

    private void preencherFormulario(Evento evento) {
        tituloField.setText(evento.getTitulo());
        descricaoArea.setText(evento.getDescricao());
        dataField.setText(evento.getDataEvento().toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        horaField.setText(evento.getHoraEvento().toLocalTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
        duracaoHorasField.setText(String.valueOf(evento.getDuracaoEvento().toHours()));
        duracaoMinutosField.setText(String.valueOf(evento.getDuracaoEvento().toMinutesPart()));
        localField.setText(evento.getLocal());
        capacidadeField.setText(String.valueOf(evento.getCapacidadeMaxima()));
        precoField.setText(String.valueOf(evento.getPreco()));
        categoriaComboBox.setSelectedItem(evento.getCategoria());
        statusComboBox.setSelectedItem(evento.getStatus());
        isLinkComboBox.setSelectedItem(evento.isLink());
    }


    private void limparFormulario() {
        this.setVisible(false);
    }
}