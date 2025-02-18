package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import entities.Evento;
import entities.UsuarioTipo;
import service.EventoManager;
import service.LoginManager;

public final class PesquisaPorNome extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTable tabela;
    private JTextField pesquisarField;
    
    public PesquisaPorNome() throws Exception {
        criarComponentes();
    }
    
    private void criarComponentes() throws Exception {
    	setBounds(138, 11, 515, 342);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 31, 515, 311);
        this.add(scrollPane);
        
        tabela = new JTable();
        tabela.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] { "Id", "Nome", "Data", "Vagas", "Local" }
        ));
        tabela.getColumnModel().getColumn(0).setPreferredWidth(29);
        scrollPane.setViewportView(tabela);
        
        pesquisarField = new JTextField();
        pesquisarField.setToolTipText("PESQUISAR");
        pesquisarField.setColumns(10);
        pesquisarField.setBounds(0, 0, 416, 20);
        this.add(pesquisarField);
        
        JButton pesquisarButton = new JButton("Pesquisar");
        pesquisarButton.setBounds(426, 0, 89, 23);
        this.add(pesquisarButton);
        
        this.setVisible(true);
        
        pesquisarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				try {
					atualizarComponentesPesquisaPorNome();
				} catch (Exception erro) {
					JOptionPane.showMessageDialog(null, "Ocorreu um erro com sua busca,\nverifique sua estabilidade com o banco de dados.","Erro Grave", JOptionPane.ERROR_MESSAGE);
				}
			}
        });
        atualizarComponentesPesquisaPorNome();
    }
    
    private void atualizarComponentesPesquisaPorNome() throws Exception {
        if (pesquisarField.getText() == null)
            return;
        
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount(0);
        
        List<Evento> eventos = null;
        
        eventos = LoginManager.getUsuario().getTipo() == UsuarioTipo.ADMIN ? EventoManager.buscarEventosPorNome(pesquisarField.getText()) : EventoManager.buscarEventosPorNomeCliente(pesquisarField.getText()); 
        
        for(Evento evento : eventos) {
            model.addRow(new Object[] {
                evento.getId(),
                evento.getTitulo(),
                evento.getDataEvento(),
                evento.getCapacidadeMaxima(),
                evento.getLocal()
            });
        }
    }
}
