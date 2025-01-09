package token;

public enum TokenType {
	// Token relativi ai tipi di dato
    TYFLOAT,           // Tipo float
    TYINT,             // Tipo intero
    FLOAT,             // Valore float
    INT,               // Valore intero

    // Token per identificatori
    ID,                // Identificatore

    // Operatori
    OP_ASSIGN,         // Operatore di assegnazione
    PLUS,              // Somma
    MINUS,             // Sottrazione
    TIMES,             // Moltiplicazione
    DIV,               // Divisione

    // Altri simboli
    SEMI,              // Punto e virgola
    ASSIGN,            // Assegnazione
    PRINT,             // Comando di stampa

    // Token di fine del file
    EOF;               // Fine del file
}
