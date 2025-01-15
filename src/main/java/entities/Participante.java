package entities;

import java.sql.Date;

public final class Participante extends User {
	private String cpf;
	private Date dataNascimento;
	
	public Participante(int id, String nome, String senha, String email, String cpf, Date dataNascimento) throws Exception {
		super(id, nome, senha, email, UsuarioTipo.DEFAULT);
		
		if (validarCPF(cpfNumbers(cpf.toCharArray())) == false)
			throw new Exception("CPF INVÁLIDO");
		
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
	}
	
	private static char[] cpfNumbers(char[] cpf) {
		if (cpf == null || cpf.length < 12)
			return null;
		
		int charAtual = 0;
		char[] cpfNumerico = new char[9];
		
		for (char caractere : cpf) {
			if ((int) caractere > 47 &&  (int) caractere < 58) {
				cpfNumerico[charAtual] = caractere;
				charAtual++;
			}
		}
		
		if (charAtual != 9)
			return null;
		
		return cpfNumerico;
	}
	
	private static int verificarCPFEtapa(char[] cpf, int maxIndex) {
		int resultado = 0;
		int oposto = maxIndex+1;
		
		for (int indice = 0; indice < maxIndex; indice++) {
			resultado += ((int) cpf[indice] - 48) * oposto;
			oposto--;
		}
		
		int restoDivisao = resultado % 11;
		
		if (restoDivisao >= 2) {
			return 11 - restoDivisao;
		}else {
			return 0;
		}
		
	}
	
	private static boolean validarCPF(char[] cpf) {
		// Retorna true se é válido
		int primeiroDigitoVerificador = verificarCPFEtapa(cpf, 9);
		
		if ((int) cpf[9] - 48 != primeiroDigitoVerificador)
			return false;
		
		int segundoDigitoVerificador = verificarCPFEtapa(cpf, 10);
		
		if ((int) cpf[10] - 48 != segundoDigitoVerificador)
			return false;
		
		return true;
	}
	
	public String getCpf() {
		return cpf;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	
	
	
	
	
}
