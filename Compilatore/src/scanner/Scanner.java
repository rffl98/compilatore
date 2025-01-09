package scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException; //GESTISCE ERRORI DI INPUT/OUTPUT.
import java.io.PushbackReader; //BUFFER DI LETTURA: PUSHBACKREADER PERMETTE DI "GUARDARE AVANTI" O "RESTITUIRE" UN CARATTERE LETTO.
import java.util.ArrayList;
import java.util.HashMap;
import token.*; // IMPORTA CLASSI DALLA CARTELLA TOKEN (PROBABILMENTE TOKEN E TOKENTYPE)

// LEGGE CAR PER CAR, RICONOSCE I TOKEN E LI RESTITUISCE
// LAVORA IN STRETTA CONNESSIONE CON LE CLASSI DELLA CARTELLA TOKEN PER
// IDENTIFICARE I TIPI DI TOKEN.

public class Scanner {
    final char EOF = (char) - 1; // COSTANTE CHE RAPPRESENTA LA FINE DEL FILE,
    // UTILIZZANDO IL VALORE SPECIALE -1
    private int riga; // TENGO TRACCIA NUMERO RIGA
    private PushbackReader buffer; // OGGETTO PUSHBACKREADER UTILIZZATO PER LEGGERE I CARATTERI DAL
    // FILE E RESTITUIRE CARATTERI SE NECESSARIO

    // skpChars: insieme caratteri di skip (include EOF) e inizializzazione
    private ArrayList < Character > skpChars;
    // letters: insieme lettere
    private ArrayList < Character > letters;
    // digits: cifre
    private ArrayList < Character > digits;

    // operTkType: mapping fra caratteri '+', '-', '*', '/'  e il TokenType
    // corrispondente
    HashMap < Character, TokenType > operTkType;
    // delimTkType: mapping fra caratteri '=', ';' e il e il TokenType
    // corrispondente
    HashMap < Character, TokenType > delimTkType;
    // keyWordsTkType: mapping fra le stringhe "print", "float", "int" e il
    // TokenType  corrispondente
    HashMap < String, TokenType > keyWordsTkType;

    
    private Token nextTk = null; //CONTIENE IL PROSSIMO TOKEN OPPURE NULL SE IL PROX TOKEN 
    							 //DEVE ESSERE LETTO DAL FILE (CON NEXTTOKEN)

    public void fillSkippers() {
        skpChars = new ArrayList < Character > ();
        skpChars.add(' ');
        skpChars.add('\n');
        skpChars.add('\t');
        skpChars.add('\r');
        skpChars.add(EOF);
    }

    public void fillLetters() {
        letters = new ArrayList < Character > ();

        for (char c = 'a'; c <= 'z'; c++) {
            letters.add(c);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            letters.add(c);
        }
    }
    public void fillDigits() {
        digits = new ArrayList < Character > ();

        for (char i = '0'; i <= '9'; i++) {
            digits.add(i);
        }
    }

    public Scanner(String fileName) throws FileNotFoundException {
        this.buffer = new PushbackReader(new FileReader(
            fileName)); // INIZIALIZZA IL BUFFER CON UN OGGETTO PUSHBACKREADER CHE
        // LEGGE IL FILE SPECIFICATO
        riga = 1; // CONTATORE DI RIGA A 1
        // inizializzare campi che non hanno inizializzazione

        fillSkippers(); // INIZIALIZZO SKIPPERS
        fillDigits(); // INIZIALIZZO DIGITS
        fillLetters(); // INIZIALIZZO LETTERS

        operTkType = new HashMap < Character, TokenType > (); //OPERATORI MAPPATI
        keyWordsTkType = new HashMap < String, TokenType > ();
        delimTkType = new HashMap < Character, TokenType > ();

        operTkType.put('/', TokenType.DIV);
        operTkType.put('+', TokenType.PLUS);
        operTkType.put('-', TokenType.MINUS);
        operTkType.put('*', TokenType.TIMES);
        
        keyWordsTkType.put("float", TokenType.TYFLOAT);
        keyWordsTkType.put("int", TokenType.TYINT);
        keyWordsTkType.put("print", TokenType.PRINT);

        delimTkType.put('=', TokenType.ASSIGN);
        delimTkType.put(';', TokenType.SEMI);
    }

    // nextToken ritorna il prossimo token nel file di input e legge
    // i caratteri del token ritornato (avanzando fino al carattere
    // successivo all'ultimo carattere del token)
    
    //se il campo nextTk è null si continua l’esecuzione del metodo che restituirà 
    //un token (se non ci sono errori) e consumerà l’input
    //altrimenti si restituisce il valore di nextTk e si setta nextTk a null.

    public Token nextToken() throws IOException, LexicalException { // SCOPO: RESTITUISCE IL PROSSIMO TOKEN TROVATO NEL
        // FILE DI INPUT.

        if (nextTk != null) {
            Token t = nextTk;
            nextTk = null;
            return t;
        }

        // nextChar contiene il prossimo carattere dell'input (non consumato).
        char nextChar = peekChar(); // Catturate l'eccezione IOException e
        
         // ritornate una LexicalException che la contiene ????????????LKCDENKJNECW3JK DA FARE

        // Avanza nel buffer leggendo i carattere in skipChars
        // incrementando riga se leggi '\n'.
        while (skpChars.contains(nextChar)) {
            if (nextChar == '\n') {
                riga++;
            }

            // Se raggiungi la fine del file ritorna il Token EOF
            if (nextChar == EOF) {
                readChar();
                return new Token(TokenType.EOF, riga);
            }

            readChar();

            nextChar = peekChar();
        }

        // Se nextChar e' in letters
        // return scanId()
        // che deve generare o un Token ID o parola chiave
        if (letters.contains(nextChar)) {
            return scanId(nextChar);
        }
        
        //////////////////////////////////////////////////////////////////////////////////  
        
        // Se nextChar e' o in operators oppure delimitatore
        // ritorna il Token associato con l'operatore o il delimitatore
        // Attenzione agli operatori di assegnamento!

        /*if (operTkType.containsKey(nextChar)) {
            readChar();
            if (peekChar() == '=') {
                readChar();
                return new Token(TokenType.OP_ASSIGN, riga);
            }
            return new Token(operTkType.get(nextChar), riga);
        }
        */
           
        // Se nextChar e' ; o =
        // ritorna il Token associato
       
        /*
        if (delimTkType.containsKey(nextChar)) {
            readChar();
            return new Token(delimTkType.get(nextChar), riga);
        }
         */
        
       //////////////////////////////////////////////////////////////////////////////////
        
     // Se nextChar è un operatore o un delimitatore
        if (operTkType.containsKey(nextChar) || delimTkType.containsKey(nextChar)) {
            // Se è un operatore, gestiamo con scanOperator
            if (operTkType.containsKey(nextChar)) {
                return scanOperator(nextChar);
            }
            // Se è un delimitatore come '=' o ';', restituiamo il token corrispondente
            if (delimTkType.containsKey(nextChar)) {
                readChar(); // Consuma il delimitatore
                return new Token(delimTkType.get(nextChar), riga); // Restituisci il token per '=' o ';'
            }
        }

         
         
        // Se nextChar e' in numbers
        // return scanNumber()
        // che legge sia un intero che un float e ritorna il Token INUM o FNUM
        // i caratteri che leggete devono essere accumulati in una stringa
        // che verra' assegnata al campo valore del Token
        if (digits.contains(nextChar)) {
            return scanNumber(nextChar);
        }

        // Altrimenti il carattere NON E' UN CARATTERE LEGALE sollevate una
        // eccezione lessicale dicendo la riga e il carattere che la hanno
        // provocata.
        readChar();
        throw new LexicalException(
            "il carattere: '" + nextChar + "' non e' un carattere ammesso");
    }

    // private Token scanId()
	private Token scanId(char nextChar) throws IOException {
	    // Inizializziamo un oggetto StringBuilder per costruire dinamicamente la stringa dell'identificatore
	    StringBuilder stringa_di_appoggio = new StringBuilder();

	    // Ciclo che continua fino a quando il carattere successivo fa parte di letters
	    while (letters.contains(nextChar)) {
	        // Aggiunge il carattere corrente alla stringa in costruzione
	        stringa_di_appoggio.append(nextChar);
	        // Legge e consuma il carattere corrente dal buffer
	        readChar();
	        // Ottiene il prossimo carattere dal buffer senza consumarlo
	        nextChar = peekChar();
	    }

	    // Converte la stringa costruita in Stringa e verifica se corrisponde a una parola chiave
	    if (keyWordsTkType.get(stringa_di_appoggio.toString()) != null) {
	        // Se è una parola chiave(print,int,float), restituisce il Token corrispondente con il tipo della parola chiave
	        return new Token(keyWordsTkType.get(stringa_di_appoggio.toString()), riga);
	    }

	    // Se non è una parola chiave, restituisce un Token di tipo ID con la stringa costruita
	    return new Token(TokenType.ID, riga, stringa_di_appoggio.toString());
	}
    
	
	// private Token scanOperator()
	
	private Token scanOperator(char nextChar) throws IOException, LexicalException {
	    // Aggiungiamo il carattere corrente all'operatore
	    StringBuilder operatore = new StringBuilder();
	    operatore.append(nextChar);

	    // Leggiamo il prossimo carattere (senza consumarlo) per verificare se è un '='
	    readChar();
	    char nextCharAfterOperator = peekChar();

	    // Se il prossimo carattere è '=', è un operatore di assegnamento combinato (es. '+=')
	    if (nextCharAfterOperator == '=') {
	        operatore.append(nextCharAfterOperator);  // Aggiungi '=' all'operatore
	        readChar(); // Consuma il carattere '='
	        
	        // Restituiamo il token OP_ASSIGN senza modificarlo (dato che l'assegnamento è già gestito dal token OP_ASSIGN)
	        return new Token(TokenType.OP_ASSIGN, riga);  // Ritorniamo OP_ASSIGN per qualsiasi operatore di assegnamento
	    } else {
	        // Se il prossimo carattere non è '=', restituiamo il token per l'operatore aritmetico
	        switch (nextChar) {
	            case '+': return new Token(TokenType.PLUS, riga);
	            case '-': return new Token(TokenType.MINUS, riga);
	            case '*': return new Token(TokenType.TIMES, riga);
	            case '/': return new Token(TokenType.DIV, riga);
	            default:
	                // Se troviamo un carattere che non è un operatore valido
	                throw new LexicalException("Operatore non valido alla riga " + riga);
	        }
	    }
	}

	
	
	// private Token scanNumber()
	private Token scanNumber(char nextChar) throws IOException, LexicalException {
		
	    StringBuilder numero = new StringBuilder(); // Accumula i caratteri del numero
	   
		boolean isFloat = false; // Per distinguere tra INT e FLOAT
	    int numbCounter = 0;
	    
	
	    while (nextChar == '0') {
	    	if(numero.length() < 1)
	        	numero.append('0'); // Manteniamo uno zero
	    	readChar(); // Consuma lo zero
	        nextChar = peekChar();
	        	        if (nextChar == '.') { // Caso di "0." per float
	        		break;
	        	}
	    }
	   
	    while (Character.isDigit(nextChar) || nextChar == '.') { // cicliamo finchè il prossimo carattere è un numero o un '.'
	    	
	    	if (nextChar == '.') {  
	            if (isFloat) { // se legge più di un punto 
	            	readChar();
	                throw new LexicalException("Numero non valido (doppio punto) alla riga " + riga); // Se troviamo un secondo punto, è un errore lessicale
	            }
	            isFloat = true; // Indichiamo che il numero è un FLOAT
	        }
	        
	        if(isFloat && nextChar != '.') {
	        	numbCounter++;
	        }
	        
	        numero.append(nextChar); // Aggiungiamo il carattere al numero
	        readChar(); // Consumiamo il carattere
	        nextChar = peekChar(); // Leggiamo il prossimo carattere
	        /*
	        //non accetto 5. come float;
	        if(isFloat && !Character.isDigit(nextChar)) {
            	throw new LexicalException("Numero non valido alla riga " + riga + "; non c'è niente dopo il punto");
            }*/
	    }//fine while
	    
	
	    
	    //accetto 5. come float e lo salvo come 5.0;
	    if(numero.length() > 0 && isFloat && numero.charAt(numero.length()-1) == '.') { // se l'ultimo elemento del numero è '.'
	    	numero.append('0');
	    	numbCounter = 1;
	    }
	    
	    if(numero.charAt(0) == '0' && numero.length() > 1 && numero.charAt(1) != '0' && numero.charAt(1) != '.'  ) {
	    	numero.deleteCharAt(0);	    	
	    }
	    
	    if( numbCounter > 5) {
	    	throw new LexicalException("Numero non valido alla riga " + riga + "; ci sono più di 5 cifre dopo il '.'");
	    }	  
	    
	    if (isFloat) {
	        return new Token(TokenType.FLOAT, riga, numero.toString());
	    } else {	
	        return new Token(TokenType.INT, riga, numero.toString());
	    }
	}
   
    //E RESTITUISCE IL PROSSIMO TOKEN, MA NON CONSUMA
	//L’INPUT, IN MODO TALE CHE UNA SUCCESSIVA NEXTTOKEN() O PEEKTOKEN()
	//RESTITUISCA LO STESSO TOKEN.
	//PER IL METODO PEEKTOKEN SE IL CAMPO NEXTTK È
	//NULL SI SETTA NEXTTK AL VALORE RESTITUITO DA NEXTTOKEN() E SI RESTITUISCE
	//IL VALORE DI NEXTTK,
	//ALTRIMENTI SI RESTITUISCE IL VALORE DI NEXTTK
	public Token peekToken() throws LexicalException, IOException {
		
		if(nextTk == null) {
			nextTk = nextToken();
		}
		return nextTk;
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // LEGGE IL PROSSIMO CARATTERE DAL BUFFER E LO RESTITUISCE.
    // LANCIA UN'ECCEZIONE IOEXCEPTION SE SI VERIFICA UN ERRORE DI LETTURA
    private char readChar() throws IOException {
        return ((char) this.buffer.read());
    }

    // GUARDA IL PROSSIMO CARATTERE NEL BUFFER SENZA CONSUMARLO
    // LEGGE UN CARATTERE DAL BUFFER.
    // LO "RESTITUISCE" AL BUFFER USANDO BUFFER.UNREAD(C).
    // RESTITUISCE IL CARATTERE LETTO.
    private char peekChar() throws IOException {
        char c = (char) buffer.read();
        buffer.unread(c);
        return c;
    }
}