package compiler.parser;

import compiler.CompilerScanner;
import compiler.Token;
import java.io.IOException;

public class Parser {
    private Token currentToken;
    private CompilerScanner scan;
    private boolean hasError = false;

    public void parse(String args) throws IOException {
        scan = new CompilerScanner(args);
        currentToken = scan.scan();
        
        if (currentToken != null) {
            parseProgram();
        }
    } 
    
    private void accept(byte expectedToken){
        System.out.println("Chegou: "+currentToken.token+" -> Experava: "+expectedToken+"\n");
        
        if(currentToken.token == expectedToken){
            acceptIt();
        } else {
            hasError = true;
            System.out.println(getParserError(Token.spellings[expectedToken]));
        }
    }

    private void acceptIt(){
        Token temp = scan.scan();
        while (temp == null) {
            temp = scan.scan();
        }
        currentToken = temp;
    }
    
    private String getParserError(String expected) {
        return "sintax error: " + currentToken.line + ":"
               + currentToken.column + ": " 
               + "expected '" + expected  + "' but received '"
               + Token.spellings[currentToken.token]  + "'";
    }

    private void parseProgram(){
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
        default:
            //ERRO
            break;
        }
    }

    private void parseComando(){
        switch (currentToken.token){
            // pc: Tem que rever isso aqui debaixo
            // Acho que deve começar com variável, assim:
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
            default:
                //Mensagem de erro - acho que faz no AST?
                break;
        }
    }

    private void parseAtribuicao(){             
        parseVariavel();                        
        accept(Token.BECOMES);                  
        parseExpressao();                       
    }

    private void parseVariavel(){
        parseId();
    }
    
    private void parseId(){

        // pc: Eu acho q tem que mudar a forma da verificação que Luan fez, até pq na nossa gramática não 
        // caracterizamos diretamente tokens LETRA e DÍGITO (apesar de ter interger) 

        /* parseLetra();
        while (currentToken.token == Token.LETRA || currentToken.token == Token.DIGITO)
        {
            if (currentToken.token == Token.LETRA)
                parseLetra();
            else
                parseDigito();
            
        } */

        // pc: Como já temos o token IDENTIFIER, melhor só usar ele, não? Desse formato a seguir:
        if(currentToken.token == Token.IDENTIFIER){
            acceptIt();
        } else {
            //Chamada de erro
        }
        // Talvez a seguinte forma seja ainda melhor, pois em uma linha ele cobre o que ele quer acima:
        //accept(Token.IDENTIFIER);

    }
    
    private void parseListaDeIds(){
        parseId();
        while (currentToken.token == Token.VIRGULA)
        {   
            acceptIt();
            parseId();
        }
    }

    private void parseComandoComposto(){
        accept(Token.BEGIN); 
        parseListaDeComandos();
        accept(Token.END); // Tem que fazer um token pra end também (pode ser quando recebe '}')
    }

    private void parseCondicional(){
        accept(Token.IF);
        parseExpressao();
        accept(Token.THEN);
        parseComando();
        
        if(currentToken.token == Token.ELSE){
            acceptIt();
            parseComando();
        }
    }

    private void parseCorpo(){
        parseDeclaracoes();
        parseComandoComposto();
    }

    private void parseDeclaracoes(){
        //nosso identifier é suficiente pra ver se é uma declaração?
        //creio q sim mas tenho dúvidas
        while (currentToken.token == Token.IDENTIFIER)
        {
            parseDeclaracao();
            accept(Token.SEMICOLON); // Separador entre declarações
        }
    
    }

    private void parseDeclaracao(){
        parseDeclaracaoDeVariavel();
    }

    private void parseDeclaracaoDeVariavel(){
        accept(Token.VAR);
        parseListaDeIds();
        accept(Token.BECOMES);
        parseTipo();
    }

    private void parseTipo(){
        switch (currentToken.token)
        {
            //case Token.TIPOSIMPLES: //Tem que separar pra todos os tipos, ficando então
            case Token.INTEGER: case Token.REAL: case Token.BOOLEAN: 
                parseTipoSimples();
                break;
            //Estou comentando o código abaixo pq não foi especificada um tipo vazio
            /* case Token.EOF: // VAZIO???
                acceptIt();
                break; */
            default:
                // ERRO
                break;
        }
    }

    private void parseTipoSimples(){
        switch (currentToken.token)
        {
            case Token.INTEGER: case Token.REAL: case Token.BOOLEAN:
                acceptIt();
                break;
            default:
                // ERRO
                break;
        }
    }
    //Parse dígito não é necessário, eu acho, mas devemos alterar isso no léxico


    /* <expressão> ::= 
    <expressão-simples> ( VAZIO |  <op-rel> <expressão-simples>) */

    private void parseExpressao(){
        parseExpressaoSimples();
        if( currentToken.token == Token.MENOR || 
            currentToken.token == Token.MENORIGUAL || 
            currentToken.token == Token.MAIOR || 
            currentToken.token == Token.MAIORIGUAL || 
            currentToken.token == Token.IGUAL || 
            currentToken.token == Token.DIFERENTE )
        {
            acceptIt();
            parseExpressaoSimples();
        }
    }

    //Mesmo erro abaixo, como representar o vazio
    private void parseExpressaoSimples(){
        //Devem ser feitas as verificações de cada tipo de token individual
        parseTermo();
        while (currentToken.token == Token.SOMA ||
            currentToken.token == Token.SUBTRACAO || 
            currentToken.token == Token.OR){
            parseOpAd();
            parseTermo();
        }
    }
    
    private void parseTermo(){
        parseFator();
        while (currentToken.token == Token.MULTIPLICACAO || 
            currentToken.token == Token.DIVISAO || 
            currentToken.token == Token.AND){
            parseOpMul();
            parseFator();
        }
    }

    private void parseFator(){
        switch(currentToken.token){
            case Token.IDENTIFIER: case Token.TRUE: case Token.FALSE: case Token.INTLITERAL: 
            case Token.REAL: // vai ficar assim mesmo, a solução do float-literal e int literal?
                parseLiteral();
                break;

            case Token.LPAREN:
                acceptIt();
                parseExpressao();
                accept(Token.RPAREN);
                break;

            default:
                //mensagem de erro
                break;
        }
    }
    
    private void parseLiteral(){
        switch (currentToken.token)
        {
            case Token.BOOLEAN:
                parseBoolLit();
                break;
            case Token.INTLITERAL:
                parseIntLit();
                break;
            case Token.REAL:
                parseFloatLit();
                break;
            default:
                //ERRO
                break;
        }
    }

    private void parseIntLit(){ 
        accept(Token.INTLITERAL); // Eu acho q só o accept pode ser suficiente já, mas vale testar
        /* while (currentToken.token == Token.INTLITERAL){
            accept(Token.INTLITERAL);
        } */
    }

    private void parseFloatLit(){ 
        // mesmo caso do parseintlit
        accept(Token.REAL);
    }

    private void parseIterativo(){
        accept(Token.WHILE);
        parseExpressao();
        accept(Token.DO);
        parseComando();
    }

    private void parseListaDeComandos(){
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
            default:
                //mensagem de erro
                break;
        }
    }

    private void parseOpMul(){
        switch(currentToken.token){
            case Token.MULTIPLICACAO: case Token.DIVISAO: case Token.AND:
                acceptIt();
                break;
            default:
                //mensagem de erro
                break;
        }
    }

    private void parseOpRel(){
        switch(currentToken.token){
            case Token.MENOR: case Token.MENORIGUAL: case Token.IGUAL:
            case Token.MAIORIGUAL: case Token.MAIOR: case Token.DIFERENTE:
                acceptIt();
                break;
            default:
                //mensagem de erro
                break;
        }
    }

    //Outras coisas que faltam representar nesses parses, que estou em dúvida e por isso não farei:
    /* <outros> ::=
         !
         |@
         |#
         | ...
    Esse outro é referente só a comentários mesmo, ou tem outras coisas?
    */
}
