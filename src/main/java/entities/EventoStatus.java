package entities;

public enum EventoStatus {
	ABERTO("aberto"),
	FECHADO("fechado"),
	ENCERRADO("encerrado"),
	CANCELADO("cancelado");
	
	private String sqlName;
	
	EventoStatus(String nome){
		this.sqlName = nome;
	}
	
	public String getSQL() {
		return this.sqlName;
	}
}
