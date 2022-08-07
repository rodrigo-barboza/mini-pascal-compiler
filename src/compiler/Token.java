package compiler;
// na linha tal coluna tal token tal valor tal (um token em cada linha)

public class Token {
    public byte kind;
    public byte token;
    public String spelling;
    public int line;
    public int column;
    
    public Token(byte token, String spelling, int line, int column){
        this.token = token;
        this.spelling = spelling;
        this.line = line;
        this.column = column;
        
        if (token == IDENTIFIER){
            for(byte k = PROGRAM; k <= VAR; k++){
                if (spelling.equals(spellings[k])){
                    this.token = k;
                    break;
                }
            }
        }

        System.out.println("line:"+line+" col:"+column+" token: "+this.token+" value: "+this.spelling);
    }
    
    public final static byte 
        IDENTIFIER = 0, 
        INTLITERAL = 1, 
        // BEGIN ATÉ VAR É IDENTIFIER (palavra reservada)
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
        END = 15,  // tirei o EOF e deixei END
        WHILE = 16,
        VAR = 17,
        // SOMA ATÉ BARRA É OPERATOR
        SOMA = 28,    // mudei esse pra incluir o VAR
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
        LPAREN = 38, // mudei esse pra incluir o VAR
        RPAREN = 29, 
        SEMICOLON = 30, 
        COLON = 31,
        COMENTARIO = 32,
        CERQUILHA = 33,
        PONTO = 34,
        ARROBA = 35,
        BECOMES = 36,
        VIRGULA = 37,
        EOF = 39,
        // CÓDIGO DE ERRO LEXICO
        LEXICAL_ERROR = 100;
    
    // falta adicionar os outros
    public final static String[] spellings = {
        "<identifier>", 
        "<integer-literal>", 
        "program", 
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
        "end",
        "while",
        "var",
        //antiga posição do "+",
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
        "+",
        //antiga posição do "("
        ")",
        ";",
        ":",
        "!",
        "#",
        ".",
        "@",
        ":=",
        ",",
        "(",
        "<eof>"
    };
}
