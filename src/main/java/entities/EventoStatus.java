package entities;

public enum EventoStatus {
	ABERTO("aberto"),
	FECHADO("fechado"),
	ANDAMENTO("andamento"),
	ENCERRADO("encerrado"),
	CANCELADO("cancelado");
	
	private String sqlName;
	
	EventoStatus(String nome){
		this.sqlName = nome;
	}
	
	public String getSQL() {
		return this.sqlName;
	}
	
	public static EventoStatus getFromString(String tipoString) {
		for (EventoStatus tipo : EventoStatus.values()) 
			if (tipo.getSQL().equals(tipoString))
				return tipo;
		
		return null;
	}
	
}
