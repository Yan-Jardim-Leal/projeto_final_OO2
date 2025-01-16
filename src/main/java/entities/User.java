package entities;

public abstract class User {
	// ==========================||     VARIÁVEIS     ||========================== //]
	protected Integer id;
	
	protected String nome;
	protected String senha;
	
	protected String email; // DEVE SER DO TIPO UNIQUE NA HORA DE CRIAR O DB
	protected UsuarioTipo tipo;
	// ==========================||      FUNÇÕES      ||========================== //
	public User(Integer id, String nome, String senha, String email, UsuarioTipo tipo) {
		this.id = id;
		this.nome = nome;
		this.senha = senha; // ENCRIPTAR A SENHA
		this.email = email;
		this.tipo = tipo;
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
	
	public Integer getId() {
		return this.id;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
		
	// ==========================|| ================= ||========================== //
}
