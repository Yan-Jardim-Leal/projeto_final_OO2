package entities;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

//@SuppressWarnings("unused")
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
			double preco,
			
			HashMap<Integer,Administrador> organizadores,
			HashMap<Integer,Participante> participantes
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
		
		this.organizadores = organizadores; // precisa ter ao menos um organizador.
		this.participantes = participantes;
	}
	
	public void adicionarOrganizador(Administrador admin) {
		Integer id = admin.getId();
		
		this.organizadores.put(id, admin);
	}
	
	public void adicionarParticipante(Participante participante) {
		Integer id = participante.getId();
		
		this.participantes.put(id, participante);
	}
	
	public boolean removerOrganizador(Integer id) {
	    return this.organizadores.remove(id) != null;
	}

	public boolean removerParticipante(Integer id) {
	    return this.participantes.remove(id) != null;
	}
	
	public HashMap<Integer,Administrador> getOrganizadores(){
		return this.organizadores;
	}
	
	public HashMap<Integer,Participante> getParticipantes(){
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

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setDataEvento(Date dataEvento) {
	    LocalDate dataAtual = LocalDate.now();
	    LocalDate dataEventoInserida = dataEvento.toLocalDate();
	    
	    if (dataEventoInserida.isBefore(dataAtual)) {
	        throw new IllegalArgumentException("Data do evento não pode ser no passado.");
	    }
	    this.dataEvento = dataEvento;
	}

	public void setHoraEvento(Time horaEvento) {
	    // Combina data do evento com a hora fornecida
	    LocalDateTime dataHoraEvento = LocalDateTime.of(
	        this.dataEvento.toLocalDate(), 
	        horaEvento.toLocalTime()
	    );
	    
	    LocalDateTime agora = LocalDateTime.now();
	    LocalDateTime umaHoraAFrente = agora.plusHours(1);
	    
	    if (dataHoraEvento.isBefore(umaHoraAFrente)) {
	        throw new IllegalArgumentException(
	            "Horário do evento deve ser pelo menos 1 hora a partir de agora."
	        );
	    }
	    this.horaEvento = horaEvento;
	}

	public void setDuracaoEvento(Duration duracaoEvento) {
		this.duracaoEvento = duracaoEvento;
	}

	public void setLink(boolean isLink) {
		this.isLink = isLink;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public void setCapacidadeMaxima(int capacidadeMaxima) {
		this.capacidadeMaxima = capacidadeMaxima;
	}

	public void setCategoria(EventoCategoria categoria) {
		this.categoria = categoria;
	}

	public void setStatus(EventoStatus status) {
		this.status = status;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public void setOrganizadores(HashMap<Integer, Administrador> organizadores) {
		this.organizadores = organizadores;
	}

	public void setParticipantes(HashMap<Integer, Participante> participantes) {
		this.participantes = participantes;
	}
	
}
