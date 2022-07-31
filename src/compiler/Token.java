package compiler;
// na linha tal coluna tal token tal valor tal (um token em cada linha)

public class Token {
    public byte kind;
    public String spelling;
    
    public Token(byte kind, String spelling, int lin, int col){
        this.kind = kind;
        this.spelling = spelling;
        
        System.out.println("line: "+lin+" col: "+col+" token: "+this.kind+" value: "+this.spelling);
//        if (kind == IDENTIFIER){
//            for(byte k = BEGIN; k <= WHILE; k++){
//                if (spelling.equals(spellings[k])){
//                    this.kind = k;
//                    break;
//                }
//            }
//        }
    }
    
    public final static byte 
        IDENTIFIER = 0, 
        INTLITERAL = 1, 
        // BEGIN ATÉ WHILE É IDENTIFIER
        PROGRAM = 2,
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
        EOF = 15,
        WHILE = 16,
        // SOMA ATÉ BARRA É OPERATOR
        SOMA = 17,   
        MENORIGUAL = 18,
        MAIORIGUAL = 19,
        DIFERENTE = 20,
        SUBTRACAO = 21,   
        MULTIPLICACAO = 22,   
        DIVISAO = 23,  
        MAIOR = 24,   
        MENOR = 25,  
        IGUAL = 26, 
        BARRA = 27, 
        // LPAREN ATÉ VIRGULA É SÍMBOLO
        LPAREN = 28, 
        RPAREN = 29, 
        SEMICOLON = 30, 
        COLON = 31,
        COMENTARIO = 32,
        CERQUILHA = 33,
        PONTO = 34,
        ARROBA = 35,
        BECOMES = 36,
        VIRGULA = 37,
        // CÓDIGO DE ERRO LEXICO
        LEXICAL_ERROR = 100;
    
    // falta adicionar os outros
    private final static String[] spellings = {
        "<identifier>", 
        "<integer-literal>", 
        "<programr>", 
        "begin", 
        "true", 
	"if", 
        "then",
        "else",
        "do",
        "false",
        "integer",
        "boolean",
        "real",
        "or",
        "and",
        "eof",
        "while",
        "+",
        "<=",
        ">=",
        "<>",
        "-",
        "*",
        "\\",
        ">",
        "<",
        "=",
        "/",
        "(",
        ")",
        ";",
        ":",
        "!",
        "#",
        ".",
        "@",
        ":=",
        ",",
        "<eof>"
    };
}
