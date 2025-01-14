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
