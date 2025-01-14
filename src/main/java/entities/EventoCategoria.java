package entities;

public enum EventoCategoria {
	WORKSHOP("workshop"),
	PALESTRA("palestra"),
	CONFERENCIA("conferencia");
	
	private String sqlName;
	
	EventoCategoria(String nome) {
		this.sqlName = nome;
	}
	
	public String getSQL() {
		return this.sqlName;
	}
}
