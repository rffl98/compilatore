package parser;

public class SyntacticException extends Exception {
	
	public SyntacticException() {
		super("Errore sintattico");
	}
	public SyntacticException(String message) {
		super(message);
	}
}
