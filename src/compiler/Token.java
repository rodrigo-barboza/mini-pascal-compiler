package compiler;

public class Token {
    public byte kind;
    public String spelling;
    
    public Token(byte kind, String spelling){
        this.kind = kind;
        this.spelling = spelling;
        
        if (kind == IDENTIFIER){
            for(byte k = BEGIN; k <= WHILE; k++){
                this.kind = k;
                break;
            }
        }
    }
    
    public final static byte 
        IDENTIFIER = 0, 
        INTLITERAL = 1, 
        OPERATOR = 2, 
        BEGIN = 3,
        TRUE = 4,
        IF = 5,
        THEN = 6,
        ELSE = 7,
        DO = 8,
        FALSE = 9,
        INTEGER = 10,
        BOOLEAN = 11,
        REAL = 12,
        OR = 13,
        AND = 14,
        WHILE = 15,
        SOMA = 16,   
        LPAREN = 17, 
        RPAREN = 18, 
        SEMICOLON = 19, 
        MENOR = 20,   
        COLON = 21,
        MENORIGUAL = 22,
        MAIORIGUAL = 23,
        DIFERENTE = 24,
        COMENTARIO = 25,
        CERQUILHA = 26,
        TRES_PONTOS = 27,
        ARROBA = 28,
        EOF = 29, 
        BECOMES = 30,
        SUBTRACAO = 31,   
        MULTIPLICACAO = 32,   
        DIVISAO = 33,  
        MAIOR = 34,   
        IGUAL = 35, 
        BARRA = 36, 
        LEXICAL_ERROR = -1;
    
    private final static String[] spellings = {
        "<identifier>", 
        "<integer-literal>", 
        "<operator>", 
        "begin", 
        "else", 
        "end", 
        "if", 
        "then",
        "var", 
        "while", 
        ";", 
        ";", 
        ":=", 
        "(", 
        ")", 
        "<eot>"
    };
}
