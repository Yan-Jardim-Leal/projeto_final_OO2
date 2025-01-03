package service.evento;

public enum VariavelEvento {
	TITULO("titulo"),
	DESCRICAO("descricao"),
	DATA("data"),
	HORA("hora"),
	DURACAO("duracao"),
	LOCAL("local"),
	IS_LINK("is_link"),
	CATEGORIA("categoria"),
	CAPACIDADE("capacidade_maxima"),
	STATUS("status"),
	PRECO("preco");
	
	//PARTICIPANTES(), // Isso é uma relação separada 
	//ORGANIZADORES(); // Isso é uma relação separada
	//Depois adicionar os nomes equivalentes ao BD
	
	private String dataBaseVar;
	
	VariavelEvento(String dataBaseVar) {
		
	}
	
	// ==========================|| GETTERS & SETTERS ||========================== //
	
	public String getdataBaseVar() {
		return dataBaseVar;
	}
	// ==========================|| ================= ||========================== //
	
}
