package compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CompilerScanner  {
    private char currentChar;
    private byte currentKind;
    private int currentLine = 0;
    private int currentColumn = -1;
    private int aux = -1;
    private StringBuffer currentSpelling;
    private String stringTeste; // = "integer a2c = 12;";
    
    public CompilerScanner(Path testFile) throws IOException{
        stringTeste = Files.readString(testFile); 
        System.out.println(stringTeste);
        System.out.println(stringTeste.length());
        nextSourceChar();
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
        currentSpelling.append(currentChar);
        nextSourceChar(); // next source charactere
    }
    
    private void nextSourceChar(){
        if (aux < stringTeste.length()-1){
            currentChar = stringTeste.charAt(++aux);
            currentColumn++;
        }
    }
    private boolean isLetter(char charactere){
        return charactere >= 'a' && charactere <= 'z';
    }
    
    private boolean isDigit(char charactere){
        return charactere >= '0' && charactere <= '9';
    }
    
    private boolean isGraphic (char charactere){
        return charactere == '!' || charactere == ' ' || charactere == 'F';
    }
    
    private byte scanToken (){
        switch(currentChar){
            case 'a': case 'b': case 'c':
            case 'd': case 'e': case 'f':
            case 'g': case 'h': case 'i':
            case 'j': case 'k': case 'l':
            case 'm': case 'n': case 'o':
            case 'p': case 'q': case 'r':
            case 's': case 't': case 'u':
            case 'v': case 'w': case 'x':
            case 'y': case 'z':        
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
                // falta botar takeIt antes de retornar
            case '+':  takeIt(); return Token.SOMA;
            case '-':  takeIt(); return Token.SUBTRACAO;
            case '*':  takeIt(); return Token.MULTIPLICACAO;
            case '/':  takeIt(); return Token.DIVISAO;
            case '<':
                takeIt();
                if (currentChar == '='){
                    takeIt();
                    return Token.MENORIGUAL;
                } else if (currentChar == '>'){
                    return Token.DIFERENTE;
                } else return Token.MENOR;
            case '>':
                takeIt();
                if (currentChar == '='){
                    takeIt();
                    return Token.MAIORIGUAL;
                } else return Token.MAIOR;
            case '=': takeIt(); return Token.IGUAL;
            case '\\': takeIt(); return Token.BARRA;
            case ';':  takeIt(); return Token.SEMICOLON;
            case ':':
                takeIt();
                if (currentChar == '='){
                    takeIt();
                    return Token.BECOMES;
                } else return Token.COLON;
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
                while(isGraphic(currentChar))
                    takeIt();
                take('\n');       
                break;
            case ' ': case '\r': //case '\n':     
                takeIt();            
                break;
            case '\n':
                takeIt();
                currentLine++;
                currentColumn=-1; 
                break;
            default: System.out.println("teste");
        }
    }
    
    public Token scan(){
        while(currentChar == '!' || currentChar == ' ' || currentChar == '\n' || currentChar == '\r')
            scanSeparator();
        currentSpelling = new StringBuffer("");
        currentKind = scanToken();
        // retornar linha e coluna
        return new Token(currentKind, currentSpelling.toString(), currentLine, currentColumn);
    }
    
    public void lexScan(){
        while(aux < stringTeste.length()-1){
            this.scan();
        }
    }
}
