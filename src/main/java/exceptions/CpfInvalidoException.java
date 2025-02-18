package exceptions;

public class CpfInvalidoException extends RegisterException {
	private static final long serialVersionUID = 1L;

	public CpfInvalidoException() {
        super("CPF inválido.");
    }
}