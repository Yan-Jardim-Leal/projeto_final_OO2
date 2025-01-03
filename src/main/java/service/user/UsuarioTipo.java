package service.user;

public enum UsuarioTipo {
	ADMIN("admin"),
	DEFAULT("participante");
	
	private String SQLValue;
	
	UsuarioTipo(String nome){
		this.SQLValue = nome;
	}
	
	public static UsuarioTipo getFromString(String tipoString) {
		for (UsuarioTipo tipo : UsuarioTipo.values()) 
			if (tipo.getSQLValue().equals(tipoString))
				return tipo;
		
		return null;
	}
	
	public String getSQLValue() {
		return SQLValue;
	}
	
}
