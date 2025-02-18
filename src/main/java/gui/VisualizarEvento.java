package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Duration;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import entities.Administrador;
import entities.Evento;
import entities.Participante;
import entities.User;
import entities.UsuarioTipo;
import service.EventoManager;
import service.LoginManager;

public class VisualizarEvento extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Evento evento;
    
    private JLabel tituloLabel;
    private JLabel descriptionLabel;
    private JLabel idLabel;
    private JLabel categoriaLabel;
    private JLabel duracaoLabel;
    private JLabel dataLabel;
    private JLabel horaLabel;
    private JLabel localLabel;
    private JLabel precoLabel;
    
    private JTable tabelaOrganizadores;
    private JTable tabelaParticipantes;
    
    private JButton participarButton;
    private JButton sairButton;
    
    public VisualizarEvento(Evento evento) {
        this.evento = evento;
        
        criarComponentes();
        inicializarComponentes();
        atualizarBotoes();
    }
    
    private void criarComponentes() {
        this.setLayout(null);
        this.setBounds(145, 11, 509, 342);        
        
        tituloLabel = new JLabel("New label");
        tituloLabel.setBounds(10, 11, 489, 27);
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
        add(tituloLabel);
        
        descriptionLabel = new JLabel("New description");
        descriptionLabel.setBackground(new Color(239, 239, 239));
        descriptionLabel.setForeground(new Color(0, 0, 0));
        descriptionLabel.setBounds(10, 81, 489, 49);
        descriptionLabel.setVerticalAlignment(SwingConstants.TOP);
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(descriptionLabel);
        
        JPanel panel_1 = new JPanel();
        panel_1.setBounds(10, 43, 489, 27);
        add(panel_1);
        
        JLabel lblId = new JLabel("Id:");
        panel_1.add(lblId);
        
        idLabel = new JLabel("222");
        panel_1.add(idLabel);
        
        JLabel lblCategoria = new JLabel("Categoria:");
        panel_1.add(lblCategoria);
        
        categoriaLabel = new JLabel("Palestra");
        panel_1.add(categoriaLabel);
        
        JLabel lblDuracao = new JLabel("Duração:");
        panel_1.add(lblDuracao);
        
        duracaoLabel = new JLabel("2h");
        panel_1.add(duracaoLabel);
        
        JLabel lblData = new JLabel("Data:");
        panel_1.add(lblData);
        
        dataLabel = new JLabel("22/22/2222");
        panel_1.add(dataLabel);
        
        JLabel lblHora = new JLabel("Hora:");
        panel_1.add(lblHora);
        
        horaLabel = new JLabel("22h");
        panel_1.add(horaLabel);
        
        JPanel panel_2 = new JPanel();
        panel_2.setBounds(10, 141, 489, 27);
        add(panel_2);
        
        JLabel lblLocal = new JLabel("Local:");
        panel_2.add(lblLocal);
        
        localLabel = new JLabel("Local exemplo");
        panel_2.add(localLabel);
        
        JLabel lblPreco = new JLabel("Preço:");
        panel_2.add(lblPreco);
        
        precoLabel = new JLabel("20R$");
        panel_2.add(precoLabel);
        
        JPanel panel_3 = new JPanel();
        panel_3.setBounds(10, 179, 160, 152);
        add(panel_3);
        
        participarButton = new JButton("Participar do Evento");
        panel_3.add(participarButton);
        
        sairButton = new JButton("Sair do Evento");
        panel_3.add(sairButton);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportBorder(new TitledBorder(null, "Organizadores", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        scrollPane.setBounds(365, 179, 134, 163);
        add(scrollPane);
        
        tabelaOrganizadores = new JTable();
        tabelaOrganizadores.setBorder(null);
        tabelaOrganizadores.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] { "Id", "Nome" }
        ));
        tabelaOrganizadores.getColumnModel().getColumn(0).setPreferredWidth(31);
        scrollPane.setViewportView(tabelaOrganizadores);
        
        if (LoginManager.getUsuario().getTipo() == UsuarioTipo.ADMIN) {
            JScrollPane scrollPane_1 = new JScrollPane();
            scrollPane_1.setViewportBorder(new TitledBorder(
                    new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), 
                    "Participantes", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
            scrollPane_1.setBounds(221, 179, 134, 163);
            add(scrollPane_1);
        	
            tabelaParticipantes = new JTable();
            tabelaParticipantes.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "Id", "Nome" }
            ));
            tabelaParticipantes.getColumnModel().getColumn(0).setPreferredWidth(29);
            tabelaParticipantes.setBorder(null);
            scrollPane_1.setViewportView(tabelaParticipantes);
        } else {
        	tabelaParticipantes = null;
        }
        
        participarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
                participarButton.setEnabled(false); // Desabilita o botão no início
				try {
					if (!EventoManager.participarEvento(evento.getId())) {
						System.out.println("Não conseguiu participar do evento");
						JOptionPane.showMessageDialog(null, "Não foi possível participar.","Erro Grave", JOptionPane.ERROR_MESSAGE);
						return;
					}
				} catch (Exception erro) {
					System.out.println("Erro:"+erro);
					JOptionPane.showMessageDialog(null, "Não foi possível participar do evento.","Erro Grave", JOptionPane.ERROR_MESSAGE);
				} finally {
                    participarButton.setEnabled(true); // Reabilita o botão no finally
                    atualizarBotoes(); // Garante que atualizarBotoes é chamado mesmo em caso de erro
                }
			}
		});

        sairButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
                sairButton.setEnabled(false); // Desabilita o botão no início
				try {
					if (!EventoManager.sairEvento(evento.getId()))
						JOptionPane.showMessageDialog(null, "O último usuário não pode sair.","Erro", JOptionPane.ERROR_MESSAGE);
						return;
				} catch (Exception erro) {
					JOptionPane.showMessageDialog(null, "Não foi possível sair do evento.","Erro Grave", JOptionPane.ERROR_MESSAGE);
				} finally {
                    sairButton.setEnabled(true); // Reabilita o botão no finally
                    atualizarBotoes(); // Garante que atualizarBotoes é chamado mesmo em caso de erro
                }
				atualizarBotoes();
			}
		});
        
        this.setVisible(true);
    }
    
    private void inicializarComponentes() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        
        tituloLabel.setText(evento.getTitulo());
        descriptionLabel.setText(evento.getDescricao());

        idLabel.setText(evento.getId() != null ? evento.getId().toString() : "N/A");
        
        categoriaLabel.setText(evento.getCategoria() != null ? evento.getCategoria().name() : "N/A");
        
        Duration duracao = evento.getDuracaoEvento();
        if (duracao != null) {
            long hours = duracao.toHours();
            long minutes = duracao.toMinutes() % 60;
            duracaoLabel.setText(hours + "h " + minutes + "m");
        } else {
            duracaoLabel.setText("N/A");
        }
        
        Date data = evento.getDataEvento();
        if (data != null) {
            dataLabel.setText(dateFormat.format(data));
        } else {
            dataLabel.setText("N/A");
        }

        Time hora = evento.getHoraEvento();
        if (hora != null) {
            horaLabel.setText(timeFormat.format(hora));
        } else {
            horaLabel.setText("N/A");
        }
        
        localLabel.setText(evento.getLocal() != null ? evento.getLocal() : "N/A");
        precoLabel.setText("R$ " + evento.getPreco());
        
        DefaultTableModel modelOrg = (DefaultTableModel) tabelaOrganizadores.getModel();
        modelOrg.setRowCount(0); // Limpa linhas existentes
        if (evento.getOrganizadores() != null) {
            for (Administrador admin : evento.getOrganizadores().values()) {
                modelOrg.addRow(new Object[] { admin.getId(), admin.getNome() });
            }
        }
        
        if (tabelaParticipantes != null) {
            DefaultTableModel modelPart = (DefaultTableModel) tabelaParticipantes.getModel();
            modelPart.setRowCount(0);
            if (evento.getParticipantes() != null) {
                for (Participante participante : evento.getParticipantes().values()) {
                    modelPart.addRow(new Object[] { participante.getId(), participante.getNome() });
                }
            }
        }
    }

    private void atualizarBotoes() {
    	try {
			this.evento = EventoManager.getEventoPorId(evento.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        User usuario = LoginManager.getUsuario();
        
        if (usuario.getTipo() == UsuarioTipo.ADMIN) {
            boolean jaOrganizador = evento.getOrganizadores() != null &&
                    evento.getOrganizadores().values().stream()
                          .anyMatch(admin -> admin.getId().equals(usuario.getId()));
            
            if (jaOrganizador) {
                participarButton.setEnabled(false);
                sairButton.setEnabled(true);
            } else {
                participarButton.setEnabled(true);
                sairButton.setEnabled(false);
            }
        } else {
            boolean jaParticipante = evento.getParticipantes() != null &&
                    evento.getParticipantes().values().stream()
                          .anyMatch(participante -> participante.getId().equals(usuario.getId()));
            
            if (jaParticipante) {
                participarButton.setEnabled(false);
                sairButton.setEnabled(true);
            } else {
                participarButton.setEnabled(true);
                sairButton.setEnabled(false);
            }
        }
        
        atualizarTabelas();
    }
    
    private void atualizarTabelas() {
        DefaultTableModel modelOrg = (DefaultTableModel) tabelaOrganizadores.getModel();
        modelOrg.setRowCount(0); // Limpa linhas existentes
        if (evento.getOrganizadores() != null) {
            for (Administrador admin : evento.getOrganizadores().values()) {
                modelOrg.addRow(new Object[] { admin.getId(), admin.getNome() });
            }
        }
        
        if (tabelaParticipantes != null) {
            DefaultTableModel modelPart = (DefaultTableModel) tabelaParticipantes.getModel();
            modelPart.setRowCount(0);
            if (evento.getParticipantes() != null) {
                for (Participante participante : evento.getParticipantes().values()) {
                    modelPart.addRow(new Object[] { participante.getId(), participante.getNome() });
                }
            }
        }
    }


}


