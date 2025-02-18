package gui;

import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import entities.Evento;
import entities.UsuarioTipo;
import service.EventoManager;
import service.LoginManager;

public final class EventosCadastrados extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel tableModel; // Keep a reference to the model

    public EventosCadastrados() throws Exception {
        criarComponentes();
        atualizarLista();
    }

    private void criarComponentes() {
        setBounds(145, 11, 509, 342);
        setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 489, 320);
        add(scrollPane);

        // Create a DefaultTableModel and override isCellEditable
        tableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "Id", "presença confirmada", "titulo", "categoria", "hora", "data"
            }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        table = new JTable(tableModel); // Use the custom model
        table.setModel(tableModel); // Redundant but for clarity

        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(156);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(94);
        scrollPane.setViewportView(table);

        this.setVisible(true);
    }

    private void atualizarLista() throws Exception {
        List<Evento> eventos;

        if (LoginManager.getUsuario().getTipo() == UsuarioTipo.ADMIN) {
            eventos = EventoManager.getEventosCadastradosAdministrador();
        } else {
            eventos = EventoManager.getEventosCadastradosCliente();
        }

        // Clear existing rows to prevent duplicates
        tableModel.setRowCount(0); // This line is already present and correct

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        for (Evento evento : eventos) {
        	
        	System.out.println("ESTOU RECEBENDO ESSE EVENTO: "+evento.getId());
        	
            String confirmacao;
            if (LoginManager.getUsuario().getTipo() == UsuarioTipo.ADMIN) {
                confirmacao = "N/A";
            } else {
                confirmacao = (evento.getParticipantes() != null && EventoManager.getPresenca(evento.getId())) ? "Sim" : "Não";
            }

            String dataStr = evento.getDataEvento() != null ? dateFormat.format(evento.getDataEvento()) : "N/A";
            String horaStr = evento.getHoraEvento() != null ? timeFormat.format(evento.getHoraEvento()) : "N/A";

            tableModel.addRow(new Object[]{
                evento.getId(),
                confirmacao,
                evento.getTitulo(),
                evento.getCategoria(),
                horaStr,
                dataStr
            });
        }
    }
}