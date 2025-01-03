package service.user;

import java.sql.Date;

public final class Participante extends User {
	private String cpf;
	private Date dataNascimento;
	
	public Participante(String nome, String senha, String email, String cpf, Date dataNascimento) {
		super(nome, senha, email);
		
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		
		this.setTipo(UsuarioTipo.DEFAULT);
	}

	public String getCpf() {
		return cpf;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	
	
	
	
	
}
