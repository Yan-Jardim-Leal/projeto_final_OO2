package service.evento;

import java.sql.Date;
import java.util.HashMap;

import service.user.Administrador;

@SuppressWarnings("unused")
public final class Evento {
	// status (fechado, aberto, encerrado ou cancelado)
	// categoria (palestra, workshop ou conferência)
	private Integer id; //Caso seja null significa que esse evento quer ser adicionado pelo banco de dados
	
	private String titulo;
	private String descricao;
	
	private Date dataEvento;
	private String horaEvento;
	private String duracaoEvento;
	
	private boolean isLink;
	
	private String local;
	private int capacidadeMaxima;
	
	private HashMap<Administrador,Boolean> organizadores;
	
	private EventoCategoria categoria;
	private EventoStatus status;
	
	private double preco;
	
	public Evento(
			Integer id,
			String titulo, 
			String descricao,
			
			Date dataEvento, 
			String horaEvento, 
			String duracaoEvento,
			
			String local, // Caso seja link, especificar
			boolean isLink,
			
			int capacidadeMaxima,
			HashMap<Administrador,Boolean> organizadores,
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
		this.organizadores = organizadores;
		this.categoria = categoria;
		this.status = status;
		this.preco = preco;
		this.isLink = isLink;
		
	}
	
	public Integer getId() {
		return this.id;
	}
	
	
}
