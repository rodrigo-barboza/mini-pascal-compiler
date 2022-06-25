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
        BEGIN = 3,
        WHILE = 13,     
        IDENTIFIER = 0, 
        INTLITERAL = 1, 
        OPERATOR = 2, 
        SEMICOLON = 23, 
        BECOMES = 4, 
        COLON = 5, 
        IS = 20, 
        LPAREN = 6, 
        RPAREN = 7, 
        EOF = 8, 
        SOMA = 9,   
        SUBTRACAO = 10,   
        MULTIPLICACAO = 11,   
        DIVISAO = 12,   
        MENOR = 23,   
        MAIOR = 14,   
        IGUAL = 15, 
        BARRA = 16, 
        OR = 33,
        AND = 34,
        MENORIGUAL = 35,
        MAIORIGUAL = 36,
        DIFERENTE = 37,
        COMENTARIO = 38,
        ARROBA = 39,
        CERQUILHA = 40,
        TRES_PONTOS = 41,
        LEXICAL_ERROR = -1;
    
    private final static String[] spellings = {
        "<identifier>", 
        "<integer-literal>", 
        "<operator>", 
        "begin", 
        "const", 
        "do", 
        "else", 
        "end", 
        "if", 
        "in", 
        "let", 
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
