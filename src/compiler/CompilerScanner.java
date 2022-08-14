package compiler;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CompilerScanner {
    private char currentChar;
    private byte currentKind;
    private int currentLine;
    private int currentColumn;
    private Scanner scanner;
    private StringBuffer currentSpelling;
    private String line;
    private boolean finishedLine = false;
    private int beginSpellingPos = 0;
    
    public CompilerScanner(String args)  throws IOException {
        File file = new File(args);
        scanner = new Scanner(file);
        
        this.currentLine = 0;
        this.currentColumn = 0;
        this.line = scanner.nextLine();
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
        if (currentColumn != (line.length()-1))
            currentChar = line.charAt(++currentColumn);
        else 
            finishedLine = true;
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
            case 'p': case 'q': case 'r':
            case 's': case 't': case 'u':
            case 'v': case 'w': case 'x':
            case 'y': case 'z':        
                takeIt();
                while((isLetter(currentChar) || isDigit(currentChar)) 
                        && currentColumn != (line.length() - 1)){
                    takeIt();
                    if (currentChar == 'n')
                        takeIt();
                }
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
    
    public Token scan(){
        int line = 0, column = 0;
        
        if (!finishedLine) {
            while( currentChar == '!'  || 
                    currentChar == ' '  || 
                    currentChar == '\n' || 
                    currentChar == '\r' ) {
                 scanSeparator();
             }
            
            currentSpelling = new StringBuffer("");
            currentKind = scanToken();
            
            line = currentLine + 1;
            column = (beginSpellingPos + 1);
            
            return new Token(
                currentKind, 
                currentSpelling.toString(), 
                line, 
                column
            );
        } 
        
        this.currentLine++;
        this.currentColumn = 0;

        if (!getNextLine()) {
            return new Token(
                Token.EOF, 
                "<eof>", 
                line, 
                column
            );
        }

        this.finishedLine = false; 
        this.currentChar = this.line.charAt(currentColumn);
        return null;
    }

    private Boolean getNextLine() {
        if (scanner.hasNextLine()) {
            this.line = scanner.nextLine();

            while(this.line.length() == 0) {
                this.currentLine++;
                this.currentColumn = 0;
                getNextLine();
            }

            return true;
        } 
        
        return false;
    }
}
