package compiler;

public class CompilerScanner {
    private char currentChar;
    private byte currentKind;
    private int currentLine;
    private int currentColumn;
    private StringBuffer currentSpelling;
    private String line;
    private boolean finishedLine = false;
    private int beginSpellingPos = 0;
    
    public CompilerScanner(String line, int currentLine, int currentColumn) {
        this.line = line;
        this.currentLine = currentLine;
        this.currentColumn = currentColumn;
        this.currentChar = line.charAt(currentColumn);
}
    
    private void take(char expectedChar){
        if (currentChar == expectedChar){
            currentSpelling.append(currentChar);
            nextSourceChar();
        } else {
            System.out.println("Erro Lexico");
        }
    }
    
    private void takeIt(){
        if (!isGraphic(currentChar)){
            currentSpelling.append(currentChar);
        }
        nextSourceChar();
    }
    
    private void nextSourceChar(){
        if (currentColumn != (line.length()-1)){
            currentColumn++;
            currentChar = line.charAt(currentColumn);
        } else {
            currentColumn = 0;
            currentLine++;
            finishedLine = true;
        }
    }
    
    private boolean isLetter(char charactere){
        return charactere >= 'a' && charactere <= 'z';
    }
    
    private boolean isDigit(char charactere){
        return charactere >= '0' && charactere <= '9';
    }
    
    private boolean isGraphic (char charactere){
        return charactere == '!' || charactere == ' ' || charactere == '\n';
    }
    
    private byte scanToken (){
        beginSpellingPos = currentColumn;
        
        switch(currentChar){
            case 'a': case 'b': case 'c':
            case 'd': case 'e': case 'f':
            case 'g': case 'h': case 'i':
            case 'j': case 'k': case 'l':
            case 'm': case 'n': case 'o':
            case 'p': 
                takeIt();
                if (currentChar == 'r'){
                    takeIt();
                    if (currentChar == 'o') {
                        takeIt();
                        if (currentChar == 'g') {
                            takeIt();
                            if (currentChar == 'r') {
                                takeIt();
                                if (currentChar == 'a') {
                                    takeIt();
                                    if (currentChar == 'm') {
                                        takeIt();
                                        return Token.PROGRAM;
                                    } 
                                }
                            }
                        }
                    }
                }
            case 'q': case 'r':
            case 's': case 't': case 'u':
            case 'v': case 'w': case 'x':
            case 'y': case 'z':        
                takeIt();
                while((isLetter(currentChar) || isDigit(currentChar)) 
                        && currentColumn != (line.length() - 1)){
                    takeIt();
                }
                if (currentChar != ';')
                    takeIt();
                return Token.IDENTIFIER;
            
            case '0': case '1': case '2': case '3': 
            case '4': case '5': case '6': case '7': 
            case '8': case '9':
                takeIt();
                while(isDigit(currentChar)) {
                    takeIt();
                }
                return Token.INTLITERAL;
                
            case '+':  takeIt(); return Token.SOMA;
            case '-':  takeIt(); return Token.SUBTRACAO;
            case '*':  takeIt(); return Token.MULTIPLICACAO;
            case '/':  takeIt(); return Token.DIVISAO;
            
            case '<':
                takeIt();
                switch(currentChar) {
                    case '=': takeIt(); return Token.MENORIGUAL;
                    case '>': takeIt(); return Token.DIFERENTE;
                    default: take(currentChar); return Token.MENOR;
                }
                
            case '>':
                takeIt();
                if (currentChar == '='){
                    takeIt();
                    return Token.MAIORIGUAL;
                } else {
                    take(currentChar);
                    return Token.MAIOR;
                } 
                
            case '=': takeIt(); return Token.IGUAL;
            case '\\': takeIt(); return Token.BARRA;
            case ';':  takeIt(); return Token.SEMICOLON;
            
            case ':':
                takeIt();
                if (currentChar == '='){
                    takeIt();
                    return Token.BECOMES;
                } else {
                    takeIt();
                    return Token.COLON;
                }
                
            case '(': takeIt(); return Token.LPAREN;
            case ')': takeIt(); return Token.RPAREN;
            case '.': takeIt(); return Token.PONTO;
            case ',': takeIt(); return Token.VIRGULA;
            case '!': takeIt(); return Token.COMENTARIO; 
            case '@': takeIt(); return Token.ARROBA; 
            case '#': takeIt(); return Token.CERQUILHA;
            case '\000': takeIt(); return Token.EOF;
            default: return Token.LEXICAL_ERROR;
        }
    }
    
    private void scanSeparator(){
        switch(currentChar){
            case'!':
                takeIt();      
                while(isGraphic(currentChar)) {
                    takeIt();
                }
                take('\n');       
                break;
            case ' ':  //case '\n':     
                takeIt();            
                break;
            case '\n':
                takeIt();
                break;
        }
    }
    
    public int getCurrentLine(){
        return currentLine;
    }
    
    public int getCurrentColumn() {
        return currentColumn;
    }
    
    public boolean getFinishedLine() {
        return finishedLine;
    }
    
    public Token scan(){
        while( currentChar == '!'  || 
               currentChar == ' '  || 
               currentChar == '\n' || 
               currentChar == '\r' ) {
            scanSeparator();
        }
            
        currentSpelling = new StringBuffer("");
        currentKind = scanToken();
        
        int line = (!this.finishedLine) ? 
                    currentLine + 1: 
                    currentLine;
        int column = (beginSpellingPos + 1);
        
        return new Token(
            currentKind, 
            currentSpelling.toString(), 
            line, 
            column
        );
    }
}
