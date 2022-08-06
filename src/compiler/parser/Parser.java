package compiler.parser;

import java.util.Scanner;
import compiler.CompilerScanner;
import java.util.ArrayList;
import compiler.Token;
import java.io.File;
import java.io.IOException;
import compiler.Constants;

public class Parser {
    private Token currentToken;
    private ArrayList<Token> tokens = new ArrayList<>();
    private int pos = 0; 
    private boolean hasError = false;
    private int possibleError;
    // O compilador da equipe de eduardo utiliza o scanner léxico pra várias funções.
    // Não sei se é 100% necessário, mas acho q será no accept e acceptIt

    public void parse(String args) throws IOException {
        File file = new File(args);
        Scanner scanner = new Scanner(file);
        
        int currentLine = 0;
        int currentColumn = 0;
        boolean finishedLine = false;
        String line = "";
        
        System.out.println("Iniciando análise léxica...");
        
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            finishedLine = false;
            
            while (!finishedLine) {
                // inicializa o léxico com a linha atual
                if (line.length() > 0){
                    CompilerScanner scan = new CompilerScanner(
                        line, 
                        currentLine,
                        currentColumn
                    );

                    // executa o léxico
                    tokens.add(scan.scan()); // armazena os tokens
                    currentLine = scan.getCurrentLine();
                    currentColumn = scan.getCurrentColumn();
                    finishedLine = scan.getFinishedLine();
                } else {
                    currentLine++;
                    break;
                }
            }
        }
        
        System.out.println("Análise léxica concluída. \n");
        
        
        System.out.println("Iniciando análise sintática...");
        // inicializa o currentToken com o primeiro Token
        currentToken = tokens.get(0);
        // chama a primeira regra da gramática
        parseProgram();
            
//            if(currentKind != Token.EOF){
//                //Reportar erro
//            }
        if (!hasError) {
            System.out.println("Análise sintática concluída.\n");
        }
            
    } 
    
    private void accept(byte expectedToken){
        System.out.println("Chegou: "+currentToken.token+" -> Experava: "+expectedToken);
        if(currentToken.token == expectedToken){
            currentToken = tokens.get(++pos);
            //Função accept
        } else {
            hasError = true;
            System.out.println(getParserError(
                    possibleError,
                    ";",
                    currentToken.spelling
            ));
        }
    }

    private void acceptIt(){
        currentToken = tokens.get(++pos);
    }
    
    private String getParserError(
            int error, 
            String expected, 
            String received
    ) {
        switch(error) {
            case 10: 
                return "sintax error: " + currentToken.line + ":"
                       + currentToken.column + ": " 
                       + "expected '" + expected + "' but received '"
                       + received + "'";
        }
        
        return "";
        
    }
    // Dá pra generalizar o formato dos erros, fazendo o seguinte:
    private String generalGetParserError(byte expectedType) {
        return "sintax error: " + currentToken.line + ":"
               + currentToken.column + ": " 
               + "expected '" + Token.spellings[expectedType] + "' but received '"
               + Token.spellings[currentToken.token] + "'";
    }


    private void parseProgram(){
        accept(Token.PROGRAM);
        parseId();
        possibleError = Constants.SINTAX_ERROR_SEMICOLON;
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
            //ERRO // Usando a função geral de erro:
            generalGetParserError(Token.BOOLEAN);
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
                //Mensagem de erro usando a função geral de erro:
                System.out.println(generalGetParserError(Token.IDENTIFIER) + "OR");
                System.out.println(generalGetParserError(Token.IF) + "OR");
                System.out.println(generalGetParserError(Token.BEGIN) + "OR");
                System.out.println(generalGetParserError(Token.WHILE));
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
        // pc: Como já temos o token IDENTIFIER, melhor só usar ele, não? Desse formato a seguir:
        if(currentToken.token == Token.IDENTIFIER){
            acceptIt();
        } else {
            generalGetParserError(Token.IDENTIFIER);
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
        accept(Token.END); 
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
                System.out.println(generalGetParserError(Token.INTEGER) + "OR");
                System.out.println(generalGetParserError(Token.REAL) + "OR");
                System.out.println(generalGetParserError(Token.BOOLEAN));
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
        if(currentToken.token == Token.MENOR || currentToken.token == Token.MENORIGUAL || currentToken.token == Token.MAIOR
            || currentToken.token == Token.MAIORIGUAL || currentToken.token == Token.IGUAL || currentToken.token == Token.DIFERENTE
            ){
            acceptIt();
            parseExpressaoSimples();
        }
        //pc: Acho q isso aqui embaixo tá errado, o que está acima é pq eu refiz
        /* parseTermo();
        switch(currentToken.token){
            case Token.MENOR: case Token.MAIORIGUAL: case Token.MENORIGUAL: case Token.MAIOR:
            case Token.IGUAL: case Token.DIFERENTE:
                parseOpRel();
                parseExpressaoSimples();
                break;
            case Token.RPAREN: // é suficiente pra representar que a expressão acabou?
                acceptIt();
                break;
            default:
                //Pode-se representar o vazio simplesmente não fazendo nada, e deixando o tratamento de erro
                //Para a função seguinte
                break;
        } */
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
                //ERRO - função geral de erro a seguir
                System.out.println(generalGetParserError(Token.BOOLEAN) + "OR");
                System.out.println(generalGetParserError(Token.INTLITERAL) + "OR");
                System.out.println(generalGetParserError(Token.REAL));
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
            default: // chamada da função geral de erro
                System.out.println(generalGetParserError(Token.SOMA) + "OR");
                System.out.println(generalGetParserError(Token.SUBTRACAO) + "OR");
                System.out.println(generalGetParserError(Token.OR));
                break;
        }
    }

    private void parseOpMul(){
        switch(currentToken.token){
            case Token.MULTIPLICACAO: case Token.DIVISAO: case Token.AND:
                acceptIt();
                break;
            default:
                //mensagem de erro -- chamada da função geral de erro
                System.out.println(generalGetParserError(Token.MULTIPLICACAO) + "OR");
                System.out.println(generalGetParserError(Token.DIVISAO) + "OR");
                System.out.println(generalGetParserError(Token.AND));
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
                //mensagem de erro -- chamadas da mesma função geral de erro
                System.out.println(generalGetParserError(Token.MENOR) + "OR");
                System.out.println(generalGetParserError(Token.MENORIGUAL) + "OR");
                System.out.println(generalGetParserError(Token.IGUAL) + "OR");
                System.out.println(generalGetParserError(Token.MAIORIGUAL) + "OR");
                System.out.println(generalGetParserError(Token.MAIOR) + "OR");
                System.out.println(generalGetParserError(Token.DIFERENTE));
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
