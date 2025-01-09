package token;

public class Token {

	private int riga;
	private TokenType tipo;
	private String val;
	
	public Token(TokenType tipo, int riga, String val) {
		this.tipo=tipo;
		this.riga=riga;
		this.val=val;
	}
	
	public Token(TokenType tipo, int riga) {
		this.tipo = tipo;
		this.riga = riga;
	}

    // Getters per i campi
    
	public String toString() {
		if(val != null) {
			return "<" + this.tipo + ",r:" + this.riga + ", " + this.val + ">";			
		}
		else {
			return "<" + this.tipo + ",r:" + this.riga + ">";
		}
	}

	
	public int getRiga() {
		return riga;
	}

	

	public TokenType getTipo() {
		return tipo;
	}

	

	public String getVal() {
		return val;
	}

	
	

}
