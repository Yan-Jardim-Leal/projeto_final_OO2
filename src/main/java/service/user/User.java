package service.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class User {
	// ==========================||     VARIÁVEIS     ||========================== //
	protected String nome;
	protected String senha;
	
	protected String email; // DEVE SER DO TIPO UNIQUE NA HORA DE CRIAR O DB
	protected UsuarioTipo tipo;
	// ==========================||      FUNÇÕES      ||========================== //
	public User(String nome, String senha, String email) {
		this.nome = nome;
		this.senha = encriptarSenha(senha); // ENCRIPTAR A SENHA
		this.email = email;
		this.tipo = null;
	}
	
	private final String encriptarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha);
	}
	
	public final boolean verificarSenha(String senha) {
		return false;
	}
	
	// ==========================|| GETTERS & SETTERS ||========================== //
	
	public String getNome() {
		return nome;
	}
	
	public final String getEmail() {
		return this.email;
	}

	public String getSenha() {
		return senha;
	}

	public UsuarioTipo getTipo() {
		return tipo;
	}
	
	public final void setTipo(UsuarioTipo tipo) {
		this.tipo = tipo;
	}
	
	// ==========================|| ================= ||========================== //
}
