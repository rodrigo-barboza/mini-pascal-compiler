package compiler.parser;

import java.util.Scanner;
import compiler.CompilerScanner;
import compiler.Token;
import java.io.File;
import java.io.IOException;

public class Parser {
    private byte currentKind;
    private CompilerScanner lexScanner; 
    // O compilador da equipe de eduardo utiliza o scanner léxico pra várias funções.
    // Não sei se é 100% necessário, mas acho q será no accept e acceptIt
    
    public void parse(String args)throws IOException {
        File file = new File(args);
        
        Scanner scanner = new Scanner(file);
        int currentLine = 0, currentColumn = 0;
        String line = "";
        
        System.out.println("Iniciando análise léxica...");
        
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
               
            CompilerScanner scan = new CompilerScanner(
                line, 
                currentLine,
                currentColumn
            );
            currentLine = scan.getCurrentLine();
            currentKind = scan.getCurrentKind();
            // chamar o parser aqui
            parseProgram();
            
            if(currentKind != Token.EOF){
                //Reportar erro
            }
        }
        
    } 

    private void accept(byte expectedToken){
        if(currentKind == expectedToken){
            //Função accept
        } else {
            //Throw Exception -> mensagem de erro
        }
    }

    private void acceptIt(){
//        currentKind = lexScanner.scan();
    }

    private void parseProgram(){
        accept(Token.PROGRAM);
        parseId();
        accept(Token.SEMICOLON);
        parseCorpo();
    }


    private void parseBoolLit(){
        switch (currentKind){
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
        switch (currentKind){
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
        while (currentKind == Token.LETRA || currentKind == Token.DIGITO)
        {
            if (currentKind == Token.LETRA)
                parseLetra();
            else
                parseDigito();
            
        } */

        // pc: Como já temos o token IDENTIFIER, melhor só usar ele, não? Desse formato a seguir:
        if(currentKind == Token.IDENTIFIER){
            acceptIt();
        } else {
            //Chamada de erro
        }

    }
    
    private void parseListaDeIds(){
        parseId();
        while (currentKind == Token.VIRGULA)
        {   
            acceptIt();
            parseId();
        }
    }

    private void parseComandoComposto(){
        accept(Token.BEGIN); // Tem que fazer um token pra begin (poder ser quando recebe '{') 
        parseListaDeComandos();
//        accept(Token.END); // Tem que fazer um token pra end também (pode ser quando recebe '}')
    }

    private void parseCondicional(){
        accept(Token.IF);
        parseExpressao();
        accept(Token.THEN);
        parseComando();
        switch (currentKind)
        {
        case Token.ELSE:
            acceptIt();
            parseComando();
            break;
        case Token.EOF: //VAZIO???
                        // pc: o tratamento do vazio se dá por um conjunto de espaços vazios
                        // Então a gente pode só fazer um if else pra saber se tem comando, não?
            acceptIt();
            break;
        default:
            //ERRO
            break;
        }
    }

    private void parseCorpo(){
        parseDeclaracoes();
        parseComandoComposto();
    }

    private void parseDeclaracoes(){
        //nosso identifier é suficiente pra ver se é uma declaração?
        //creio q sim mas tenho dúvidas
        while (currentKind == Token.IDENTIFIER)
        {
            parseDeclaracao();
            accept(Token.SEMICOLON); // Separador entre declarações
        }
    
    }

    private void parseDeclaracao(){
        parseDeclaracaoDeVariavel();
    }

    private void parseDeclaracaoDeVariavel(){
        //accept(Token.VAR) //VAR FOI DECLARADO NO LEXICO?
        // pc: var é variável mesmo, não? Então isso vira identifier? Se for, fica assim:
        accept(Token.IDENTIFIER);
        parseListaDeIds();
        accept(Token.COLON);
        parseTipo();
    }

    private void parseTipo(){
        switch (currentKind)
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
        switch (currentKind)
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
        parseTermo();
        switch(currentKind){
            case Token.MENOR: case Token.MAIORIGUAL: case Token.MENORIGUAL: case Token.MAIOR:
            case Token.IGUAL: case Token.DIFERENTE:
                parseOpRel();
                parseExpressaoSimples();
                break;
            case Token.RPAREN: // é suficiente pra representar que a expressão acabou?
                acceptIt();
                break;
            default:
                //Erro? Como representar o vazio? Rparen serve?
                break;
        }
    }

    //Mesmo erro abaixo, como representar o vazio
    private void parseExpressaoSimples(){
        //Devem ser feitas as verificações de cada tipo de token individual
        parseTermo();
        while (currentKind == Token.SOMA ||
            currentKind == Token.SUBTRACAO || 
            currentKind == Token.OR){
            parseOpAd();
            parseTermo();
        }
    }
    
    private void parseTermo(){
        parseFator();
        while (currentKind == Token.MULTIPLICACAO || 
            currentKind == Token.DIVISAO || 
            currentKind == Token.AND){
            parseOpMul();
            parseFator();
        }
    }

    private void parseFator(){
        switch(currentKind){
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
        switch (currentKind)
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

    private void parseIntLit(){ //Isso aqui tá certo?
        accept(Token.INTLITERAL);
        while (currentKind == Token.INTLITERAL){
            accept(Token.INTLITERAL);
        }
    }
    //Parse dígito não é necessário, eu acho, mas devemos alterar isso no léxico

    private void parseFloatLit(){ 
        //não sei se é necessário mesmo, vale analisar isso melhor, e se for o caso,
        //refazer também no léxico
        accept(Token.REAL);
    }

    private void parseIterativo(){
        acceptIt();
        parseExpressao();
        accept(Token.DO);
        parseComando();
    }

    // Acho que não faz sentido entrar um parse letra, então não botei
    // As regras das letras acho q já são totalmente possíveis de serem tratadas no léxico.
    // No compilador completo os veteranos também não botaram.

    private void parseListaDeComandos(){
        while(currentKind == Token.IF ||
            currentKind == Token.WHILE ||
            currentKind == Token.BEGIN ||
            currentKind == Token.IDENTIFIER){
            parseComando();
            accept(Token.SEMICOLON);
        }
    }

    private void parseOpAd(){
        switch(currentKind){
            case Token.SOMA: case Token.SUBTRACAO: case Token.OR:
                acceptIt();
                break;
            default:
                //mensagem de erro
                break;
        }
    }

    private void parseOpMul(){
        switch(currentKind){
            case Token.MULTIPLICACAO: case Token.DIVISAO: case Token.AND:
                acceptIt();
                break;
            default:
                //mensagem de erro
                break;
        }
    }

    private void parseOpRel(){
        switch(currentKind){
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
    /* <programa> ::= //Não analisei se é LL1
         program <id> ; <corpo> .
    Como vai ser o parseProgram? Acho que precisamos modificar isso no
    léxico pra que tenha um símbolo de começo de programa  
    */



}
