package service.user;

import java.sql.Date;

public final class Administrador extends User {
	private String cargo;
	private Date dataContratado;
	
	public Administrador(String nome, String senha, String email, String cargo, Date dataContratado) {
		super(nome, senha, email);
		
		this.cargo = cargo;
		this.dataContratado = dataContratado;
		
		this.setTipo(UsuarioTipo.ADMIN);
	}

	public String getCargo() {
		return cargo;
	}

	public Date getDataContratado() {
		return dataContratado;
	}
	
	
	
}
