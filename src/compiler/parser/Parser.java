package compiler.parser;

import compiler.CompilerScanner;
import compiler.Token;
import java.io.IOException;

public class Parser {
    private Token currentToken;
    private CompilerScanner scan;

    public void parse(String args) throws IOException {
        scan = new CompilerScanner(args);
        currentToken = scan.scan();
        
        if (currentToken != null) {
            parseProgram();
        }
    } 
    
    private void accept(byte expectedToken) throws ParserException {
        System.out.println("Chegou: "+currentToken.token+" -> Experava: "+expectedToken+"\n");
        
        if(currentToken.token == expectedToken){
            acceptIt();
        } else {
            sintaxError(expectedToken);
        }
    }
    
    private void sintaxError(byte expectedToken) throws ParserException {
        throw new ParserException(currentToken, Token.spellings[expectedToken]);
    }

    private void acceptIt(){
        Token temp = scan.scan();
        while (temp == null) {
            temp = scan.scan();
        }
        currentToken = temp;
    }
    
    private void parseProgram() throws ParserException {
        accept(Token.PROGRAM);
        parseId();
        accept(Token.SEMICOLON);
        parseCorpo();
    }


    private void parseBoolLit(){
        switch (currentToken.token){
        case Token.TRUE:
            acceptIt();
            break;
        case Token.FALSE:
            acceptIt();
            break;
        }
    }

    private void parseComando() throws ParserException {
        switch (currentToken.token){
            case Token.IDENTIFIER:
                parseAtribuicao();
                break;
            case Token.IF:
                parseCondicional();
                break;
            case Token.BEGIN:
                parseComandoComposto();
                break;
            case Token.WHILE:
                parseIterativo();
                break;
        }
    }

    private void parseAtribuicao() throws ParserException {             
        parseVariavel();                        
        accept(Token.BECOMES);                  
        parseExpressao();                       
    }

    private void parseVariavel(){
        parseId();
    }
    
    private void parseId(){
        if(currentToken.token == Token.IDENTIFIER){
            acceptIt();
        }
    }
    
    private void parseListaDeIds(){
        parseId();
        while (currentToken.token == Token.VIRGULA) {   
            acceptIt();
            parseId();
        }
    }

    private void parseComandoComposto() throws ParserException {
        accept(Token.BEGIN); 
        parseListaDeComandos();
        accept(Token.END); 
    }

    private void parseCondicional() throws ParserException {
        accept(Token.IF);
        parseExpressao();
        accept(Token.THEN);
        parseComando();
        
        if(currentToken.token == Token.ELSE){
            acceptIt();
            parseComando();
        }
    }

    private void parseCorpo() throws ParserException {
        parseDeclaracoes();
        parseComandoComposto();
    }

    private void parseDeclaracoes() throws ParserException {
        while (currentToken.token == Token.VAR) {
            parseDeclaracao();
            accept(Token.SEMICOLON);
        }
    
    }

    private void parseDeclaracao() throws ParserException {
        parseDeclaracaoDeVariavel();
    }

    private void parseDeclaracaoDeVariavel() throws ParserException {
        accept(Token.VAR);
        parseListaDeIds();
        accept(Token.COLON);
        parseTipo();
    }

    private void parseTipo(){
        switch (currentToken.token) {
            case Token.INTEGER: case Token.REAL: case Token.BOOLEAN: 
                parseTipoSimples();
                break;
            case Token.EOF:
                acceptIt();
                break;
        }
    }

    private void parseTipoSimples(){
        switch (currentToken.token)
        {
            case Token.INTEGER: case Token.REAL: case Token.BOOLEAN:
                System.out.println("entrou");
                acceptIt();
                break;
        }
    }

    private void parseExpressao() throws ParserException {
        parseExpressaoSimples();
        if( currentToken.token == Token.MENOR || 
            currentToken.token == Token.MENORIGUAL || 
            currentToken.token == Token.MAIOR || 
            currentToken.token == Token.MAIORIGUAL || 
            currentToken.token == Token.IGUAL || 
            currentToken.token == Token.DIFERENTE ) {
            acceptIt();
            parseExpressaoSimples();
        }
    }

    private void parseExpressaoSimples() throws ParserException {
        parseTermo();
        while (currentToken.token == Token.SOMA ||
            currentToken.token == Token.SUBTRACAO || 
            currentToken.token == Token.OR){
            parseOpAd();
            parseTermo();
        }
    }
    
    private void parseTermo() throws ParserException {
        parseFator();
        while (currentToken.token == Token.MULTIPLICACAO || 
            currentToken.token == Token.DIVISAO || 
            currentToken.token == Token.AND){
            parseOpMul();
            parseFator();
        }
    }

    private void parseFator() throws ParserException {
        switch(currentToken.token){
            case Token.IDENTIFIER: case Token.TRUE: case Token.FALSE: case Token.INTLITERAL: 
            case Token.REAL: 
                parseLiteral();
                break;
            case Token.LPAREN:
                acceptIt();
                parseExpressao();
                accept(Token.RPAREN);
                break;
        }
    }
    
    private void parseLiteral() throws ParserException {
        switch (currentToken.token) {
            case Token.BOOLEAN:
                parseBoolLit();
                break;
            case Token.INTLITERAL:
                parseIntLit();
                break;
            case Token.REAL:
                parseFloatLit();
                break;
        }
    }

    private void parseIntLit() throws ParserException { 
        accept(Token.INTLITERAL); 
    }

    private void parseFloatLit() throws ParserException { 
        accept(Token.REAL);
    }

    private void parseIterativo() throws ParserException {
        accept(Token.WHILE);
        parseExpressao();
        accept(Token.DO);
        parseComando();
    }

    private void parseListaDeComandos() throws ParserException {
        while(currentToken.token == Token.IF ||
            currentToken.token == Token.WHILE ||
            currentToken.token == Token.BEGIN ||
            currentToken.token == Token.IDENTIFIER){
            parseComando();
            accept(Token.SEMICOLON);
        }
    }

    private void parseOpAd(){
        switch(currentToken.token){
            case Token.SOMA: case Token.SUBTRACAO: case Token.OR:
                acceptIt();
                break;
        }
    }

    private void parseOpMul(){
        switch(currentToken.token){
            case Token.MULTIPLICACAO: case Token.DIVISAO: case Token.AND:
                acceptIt();
                break;
        }
    }

    private void parseOpRel(){
        switch(currentToken.token){
            case Token.MENOR: case Token.MENORIGUAL: case Token.IGUAL:
            case Token.MAIORIGUAL: case Token.MAIOR: case Token.DIFERENTE:
                acceptIt();
                break;
        }
    }
}
