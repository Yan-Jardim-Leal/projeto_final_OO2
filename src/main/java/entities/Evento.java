package entities;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.util.HashMap;

@SuppressWarnings("unused")
public final class Evento {
	// status (fechado, aberto, encerrado ou cancelado)
	// categoria (palestra, workshop ou conferência)
	private Integer id; //Caso seja null significa que esse evento quer ser adicionado pelo banco de dados
	
	private String titulo;
	private String descricao;
	
	private Date dataEvento;
	private Time horaEvento;
	private Duration duracaoEvento;
	
	private boolean isLink;
	
	private String local;
	private int capacidadeMaxima;
	
	private EventoCategoria categoria;
	private EventoStatus status;
	
	private double preco;
	
	private HashMap<Integer,Administrador> organizadores;
	private HashMap<Integer,Participante> participantes;
	
	public Evento(
			Integer id,
			String titulo, 
			String descricao,
			
			Date dataEvento, 
			Time horaEvento, 
			Duration duracaoEvento,
			
			String local, // Caso seja link, especificar
			boolean isLink,
			
			int capacidadeMaxima,
			EventoCategoria categoria,
			EventoStatus status,
			double preco
			) {
		
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.dataEvento = dataEvento;
		this.horaEvento = horaEvento;
		this.duracaoEvento = duracaoEvento;
		this.local = local;
		this.capacidadeMaxima = capacidadeMaxima;
		this.categoria = categoria;
		this.status = status;
		this.preco = preco;
		this.isLink = isLink;
		
		this.organizadores = new HashMap<Integer,Administrador>(); // precisa ter ao menos um organizador.
		this.participantes = new HashMap<Integer,Participante>();
	}
	
	public void adicionarOrganizador(Administrador admin) {
		Integer id = admin.getId();
		
		this.organizadores.put(id, admin);
	}
	
	public void adicionarParticipante(Participante participante) {
		Integer id = participante.getId();
		
		this.participantes.put(id, participante);
	}
	
	public HashMap<Integer,Administrador> getOrganizadores(){
		return this.organizadores;
	}
	
	public HashMap<Integer,Participante> getParticipante(){
		return this.participantes;
	}
	
	public Integer getId() {
		return this.id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Date getDataEvento() {
		return dataEvento;
	}

	public Time getHoraEvento() {
		return horaEvento;
	}

	public Duration getDuracaoEvento() {
		return duracaoEvento;
	}

	public boolean isLink() {
		return isLink;
	}

	public String getLocal() {
		return local;
	}

	public int getCapacidadeMaxima() {
		return capacidadeMaxima;
	}

	public EventoCategoria getCategoria() {
		return categoria;
	}

	public EventoStatus getStatus() {
		return status;
	}

	public double getPreco() {
		return preco;
	}
	
}
