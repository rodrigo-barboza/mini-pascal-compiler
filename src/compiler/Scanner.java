package compiler;

public class Scanner {
    private char currentChar;
    private byte currentKind;
    private StringBuffer currentSpelling;
    
    public Scanner (String source){
//        this.currentChar = source[0];
    }
    
    private void take(char expectedChar){
        if (currentChar == expectedChar){
            currentSpelling.append(currentChar);
            currentChar = 'p'; // next source char
        } else {
            System.out.println("Erro Lexico");
        }
    }
    
    private void takeIt(){
        currentSpelling.append(currentChar);
        currentChar = '2'; // next source charactere
    }
    
    private boolean isLetter(char charactere){
        return charactere >= 'a' && charactere <= 'z';
    }
    
    private boolean isDigit(char charactere){
        return charactere >= 0 && charactere <= 9;
    }
    
    private boolean isGraphic (char charactere){
        return charactere == '!' || charactere == ' ' || charactere == 'F';
    }
    
    private byte scanToken (){
        switch(currentChar){
            case 'a':
                takeIt();
                if (currentChar == 'n'){
                    takeIt();
                    if(currentChar == 'd'){
                        takeIt();
                        return Token.AND;
                    }
                }
            case 'b':case 'c':case 'd':
            case 'e':case 'f':case 'g':case 'h':
            case 'i':case 'j':case 'k':case 'l':
            case 'm':case 'n':case 'o':
                takeIt();
                if(currentChar == 'r'){
                    takeIt();
                    return Token.OR;
                }
            case 'p': case 'q':case 'r':case 's':
            case 't': case 'u':case 'v':case 'w':
            case 'x': case 'y':case 'z':
                takeIt();
                while(isLetter(currentChar) || isDigit(currentChar))
                    takeIt();
                return Token.IDENTIFIER;
            case '0': case '1': case '2': case '3': 
            case '4': case '5': case '6': case '7': 
            case '8': case '9':
                takeIt();
                while(isDigit(currentChar))
                    takeIt();
                return Token.INTLITERAL;
            case '+':  return Token.SOMA;
            case '-':  return Token.SUBTRACAO;
            case '*':  return Token.MULTIPLICACAO;
            case '/':  return Token.DIVISAO;
            case '<':
                takeIt();
                if (currentChar == '='){
                    takeIt();
                    return Token.MENORIGUAL; // FALTA ADD NUMERAÇÃO
                } else if (currentChar == '>'){
                    return Token.DIFERENTE;
                } else return Token.MENOR;
            case '>':
                takeIt();
                if (currentChar == '='){
                    takeIt();
                    return Token.MAIORIGUAL; // FALTA ADD NUMERAÇÃO
                } else return Token.MAIOR;
            case '=':  return Token.IGUAL;
            case '\\': return Token.BARRA;
            case ';':  
                takeIt();
                return Token.SEMICOLON;
            case ':':
                takeIt();
                if (currentChar == '='){
                    takeIt();
                    return Token.BECOMES;
                } else return Token.COLON;
            case '~': 
                takeIt();
                return Token.IS;
            case '(': 
                takeIt();
                return Token.LPAREN;
            case ')': 
                takeIt();
                return Token.RPAREN;
            case '!':
                return Token.COMENTARIO; //FALTA ADD NUMERAÇÃO
            case '@':
                return Token.ARROBA; // FALTA ADD NUMERAÇÃO
            case '#':
                return Token.CERQUILHA; // FALTA ADD NUMERAÇÃO
            case '.':
                takeIt();
                if (currentChar == '.')
                {
                    takeIt();
                    if (currentChar == '.')
                    {
                        takeIt();
                        return Token.TRES_PONTOS; 
                    }
                }
            case '\000': return Token.EOF;
            default: return Token.LEXICAL_ERROR;
        }
    }
    
    private void scanSeparator(){
        switch(currentChar){
            case'!':
                takeIt();      
                while(isGraphic(currentChar))
                    takeIt();
                take('\n');       
                break;
            case ' ': case '\n':       
                takeIt();            
                break;
        }
    }
    
    public Token scan(){
        while(currentChar == '!' || currentChar == ' ' || currentChar == '\n')
            scanSeparator();
        currentSpelling = new StringBuffer("");
        currentKind = scanToken();
        return new Token(currentKind, currentSpelling.toString());
    }
    
}
