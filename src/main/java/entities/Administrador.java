package entities;

import java.sql.Date;

public final class Administrador extends User {
	private String cargo;
	private Date dataContratado;
	
	public Administrador(Integer id, String nome, String senha, String email, String cargo, Date dataContratado) {
		super(id, nome, senha, email, UsuarioTipo.ADMIN);
		
		this.cargo = cargo;
		this.dataContratado = dataContratado;
	}

	public String getCargo() {
		return cargo;
	}

	public Date getDataContratado() {
		return dataContratado;
	}
	
	
	
}
