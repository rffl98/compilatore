package parser;

import java.io.IOException;
import java.util.ArrayList;

import ast.*;
import scanner.LexicalException;
import scanner.Scanner;
import token.Token;
//import token.*;
import token.TokenType;


public class Parser {
	
	/*un costruttore che prende come input uno Scanner da memorizzare in
	un campo privato, scanner,
	i metodi privati parseNT per ogni nonterminale della grammatica che
	restituiscono void se non ci sono stati errori oppure segnalano un
	errore che deve dire “quale è il token che causa l’errore e perchè”
	il metodo privato match descritto a lezione
	il metodo pubblico parse che ritorna parsePrg
	*/
    private Scanner scanner;
	// COSTRUTTORE
    public Parser(Scanner s) throws IOException, LexicalException {
        this.scanner = s;
        //scanner.nextToken();
    }
	
	
    public NodeProgram parse () throws SyntacticException, LexicalException, IOException {
		return this.parsePrg();
	}
      
    private NodeProgram parsePrg() throws SyntacticException, LexicalException, IOException{
		
		
		 Token tk = scanner.peekToken();
		
			switch (tk.getTipo()) {
		 
				case TYFLOAT:
				case TYINT:
				case ID:
				case PRINT:
				case EOF:
					ArrayList<NodeDecSt> dec = parseDSs();
					NodeProgram node = new NodeProgram(dec);
					match(TokenType.EOF);
					return node;
					
			default:
				throw new SyntacticException("Errore parser da ParsePrg: previsto un Token tra: TYFLOAT, TYINT, ID, PRINT, EOF;\n Token trovato: " + tk.getTipo() + ", alla riga " + tk.getRiga());
			}
					 // token tk alla riga tk.getRiga() non e’ inizio di programma
		}
	
    
    
    
    
    private ArrayList<NodeDecSt> parseDSs() throws LexicalException, IOException, SyntacticException {
		
		Token tk = scanner.peekToken();
		ArrayList<NodeDecSt> dec = new ArrayList<NodeDecSt>(); 
		
		switch (tk.getTipo()) {
		 
			// produzione -> Dcl DSs
			case TYFLOAT:
			case TYINT:
				NodeDecl node = parseDcl();
				dec = parseDSs();
				dec.add(0, node);
				return dec;
			// produzione -> Stm DSs
			case ID:
			case PRINT:
				NodeStm nodePrint = parseStm();
				dec = parseDSs();
				dec.add(0, nodePrint);
				return dec;
	
			// produzione -> Stringa vuota
			case EOF:
				return dec;
			
			default:
				throw new SyntacticException("Errore parser da ParseDSs: previsto un Token tra: TYFLOAT, TYINT, ID, PRINT, EOF;\n Token trovato: " + tk.getTipo() + ", alla riga " + tk.getRiga());
		}
	}

    private NodeDecl parseDcl() throws LexicalException, IOException, SyntacticException {
		Token tk = scanner.peekToken();
				
			switch (tk.getTipo()) {
			 
			//produzione Ty id DclP
			case TYFLOAT:
			case TYINT:
				LangType ty = parseTy();
			 	Token tk1 = match(TokenType.ID);
			 	NodeId nId = new NodeId(tk1.toString());
				NodeExpr nExpr = parseDclP();
				return new NodeDecl( nId ,ty, nExpr);
			default:
				throw new SyntacticException("Errore parser da ParseDcl: previsto un Token tra: TYFLOAT, TYINT;\n Token trovato: " + tk.getTipo() + ", alla riga " + tk.getRiga());
			}
	}
	
    private NodeExpr parseDclP() throws LexicalException, IOException, SyntacticException {
		
    	Token tk = scanner.peekToken();
    	NodeExpr e ;
    		
    		switch (tk.getTipo()) {
    		 
    		//produzione -> ;
    		case SEMI:
    			match(TokenType.SEMI);
    			return null;
    			
    			//produzione ->  = Exp ;
    		case ASSIGN:
    			match(TokenType.ASSIGN);
    			e = parseExp();
    			match(TokenType.SEMI);
    			return e;
    		
    		default:
    			throw new SyntacticException("Errore parser da ParseDclP: previsto un Token tra: SEMI, ASSIGN;\n Token trovato: " + tk.getTipo() + ", alla riga " + tk.getRiga());
    		}
    	}
    	
    private NodeStm parseStm() throws LexicalException, IOException, SyntacticException {
		
		Token tk = scanner.peekToken();
		Token tk1;
			
			switch (tk.getTipo()) {
			 
			//solo produzione 7 : Stm -> id Op Exp ;
			//produzione float
			case ID:
				tk1 = match(TokenType.ID);
				NodeId nodeId = parseOp();
				NodeExpr expr = parseExp();
				match(TokenType.SEMI);
				
				NodeAssign node = new NodeAssign(nodeId, expr);
				
				return node;

			//solo produzione 8 : Stm -> print id ;
			
			case PRINT:
				match(TokenType.PRINT);
				Token tkId = match(TokenType.ID);
				match(TokenType.SEMI);
				NodeId nId = new NodeId(tkId.toString());
				return new NodePrint(nId);
			default:
				throw new SyntacticException("Errore parser da ParseStm: previsto un Token tra: ID, PRINT;\n Token trovato: " + tk.getTipo() + ", alla riga " + tk.getRiga());
			}
		
	}
	
    private NodeExpr parseExp() throws LexicalException, IOException, SyntacticException {
		
		Token tk = scanner.peekToken();
		NodeExpr left;
		NodeExpr nExpr; 
		
			switch(tk.getTipo()) {
				//produzione -> Tr ExpP
				case ID:
				case FLOAT:
				case INT:
					left = parseTr();
					nExpr = parseExpP(left);
					return nExpr;
				
				default:
					throw new SyntacticException("Errore parser da ParseExp: previsto un Token tra: ID, FLOAT, INT;\n Token trovato: " + tk.getTipo() + ", alla riga " + tk.getRiga());
			}
	}
	
    private NodeExpr parseExpP(NodeExpr left) throws LexicalException, IOException, SyntacticException {
		
		Token tk = scanner.peekToken();
		NodeExpr right;
		
		switch(tk.getTipo()) {
			//produzione -> + Tr ExpP
			case PLUS:
				match(TokenType.PLUS);
				right = parseTr();
				return parseExpP(new NodeBinOp(LangOper.PLUS, left, right));
				
				
			//produzione -> - Tr ExpP
			case MINUS:
				match(TokenType.MINUS);
				right = parseTr();
				return parseExpP(new NodeBinOp(LangOper.MINUS, left, right));
				
			//produzione -> ϵ 
			case SEMI:
				return left;
				
			default:
				throw new SyntacticException("Errore parser da ParseExpP: previsto un Token tra: PLUS, MINUS, SEMI;\n Token trovato: " + tk.getTipo() + ", alla riga " + tk.getRiga());
		}
	}
	
    private NodeExpr parseTr() throws LexicalException, IOException, SyntacticException {
		Token tk = scanner.peekToken();
		NodeExpr left;
		switch(tk.getTipo()) {
			//produzione -> Val Trp
			case ID:
			case FLOAT:
			case INT:
				left = parseVal();
				return parseTrP(left);
			
			default:
				throw new SyntacticException("Errore parser da ParseTr: previsto un Token tra: ID, FLOAT, INT;\n Token trovato: " + tk.getTipo() + ", alla riga " + tk.getRiga());
		}
	}
	
    private NodeExpr parseTrP(NodeExpr left) throws LexicalException, IOException, SyntacticException {
		
		Token tk = scanner.peekToken();
		NodeExpr right;
		
		switch(tk.getTipo()) {
			//produzione -> * Val TrP
			case TIMES:
				match(TokenType.TIMES);
				right = parseVal();
				return parseTrP(new NodeBinOp(LangOper.TIMES, left, right));
				
				
			//produzione -> / Val TrP
			case DIV:
				match(TokenType.DIV);
				right = parseVal();
				return parseTrP(new NodeBinOp(LangOper.DIV, left, right));
				
			//produzione -> ϵ 
			case MINUS:
			case PLUS:
			case SEMI:
				return left;
				
			default:
				throw new SyntacticException("Errore parser da ParseTrP: previsto un Token tra: TIMES, DIV, MINUS, PLUS, SEMI;\n Token trovato: " + tk.getTipo() + ", alla riga " + tk.getRiga());
		}
	}
	
    private LangType parseTy() throws LexicalException, IOException, SyntacticException {
    	
		Token tk = scanner.peekToken();
		
		switch (tk.getTipo()) {
		 
		//produzione float
		case TYFLOAT:
			match(TokenType.TYFLOAT);
			return LangType.FLOAT;
		//produzione int
		case TYINT:
			match(TokenType.TYINT);
			return LangType.INT;
		default:
			throw new SyntacticException("Errore parser da ParseTy: previsto un Token tra: TYFLOAT, TYINT;\n Token trovato: " + tk.getTipo() + ", alla riga " + tk.getRiga());
		}
		
		
	}

    private NodeExpr parseVal() throws LexicalException, IOException, SyntacticException {
		
		Token tk = scanner.peekToken();
		Token tk1;
		
		switch(tk.getTipo()) {
			//produzione -> int Val
			case INT:
				tk1 = match(TokenType.INT);
				return new NodeCost( tk1.getVal() , LangType.INT );
				
			//produzione -> float Val
			case FLOAT:
				tk1 = match(TokenType.FLOAT);
				return new NodeCost(tk1.getVal(), LangType.FLOAT);
				
			//produzione -> id
			case ID:
				tk1 = match(TokenType.ID);
				NodeId nId = new NodeId(tk1.getVal());
				return new NodeDeref(nId);
	
			default:
				throw new SyntacticException("Errore parser da ParseVal: previsto un Token tra: INT, FLOAT, ID;\n Token trovato: " + tk.getTipo() + ", alla riga " + tk.getRiga());
		}
	}
	
    private NodeId parseOp() throws LexicalException, IOException, SyntacticException {
		
		Token tk = scanner.peekToken();
		Token tk1;
		NodeExpr expr;
		
		switch(tk.getTipo()) {
			//produzione -> =
			case ASSIGN:
				tk1 = match(TokenType.ASSIGN);
				return new NodeId(tk1.getVal());
				
				
			//produzione -> OpAss
			case OP_ASSIGN:
				tk1 = match(TokenType.OP_ASSIGN);
				return new NodeId(tk1.getVal());
				
	
			default:
				throw new SyntacticException("Errore parser da ParseOp: previsto un Token tra: ASSIGN, OP_ASSIGN;\n Token trovato: " + tk.getTipo() + ", alla riga " + tk.getRiga());
		}
	}
	
    
    
    
    
    
    private Token match(TokenType type) throws LexicalException, SyntacticException, IOException {
        Token tk = scanner.peekToken();
        if (type.equals(tk.getTipo())) {
            return scanner.nextToken();
        } else {
            throw new SyntacticException("Errore sintattico: aspettato token '" + type + "', trovato '" + tk.getTipo() + "' alla riga " + tk.getRiga() + ".");
        }
    }
	
}
