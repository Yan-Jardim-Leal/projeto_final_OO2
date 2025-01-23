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
	
	public static EventoCategoria getFromString(String tipoString) {
		for (EventoCategoria tipo : EventoCategoria.values()) 
			if (tipo.getSQL().equals(tipoString))
				return tipo;
		
		return null;
	}
	
}
