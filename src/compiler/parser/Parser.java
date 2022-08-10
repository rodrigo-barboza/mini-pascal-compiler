package compiler.parser;

import compiler.CompilerScanner;
import compiler.Token;
import compiler.AbsSintTree.NodoComando;
import compiler.AbsSintTree.NodoComandoAtrib;
import compiler.AbsSintTree.NodoComandoComposto;
import compiler.AbsSintTree.NodoComandoCond;
import compiler.AbsSintTree.NodoComandoIterativo;
import compiler.AbsSintTree.NodoCorpo;
import compiler.AbsSintTree.NodoDeclaracao;
import compiler.AbsSintTree.NodoDeclaracaoVar;
import compiler.AbsSintTree.NodoDeclaracoes;
import compiler.AbsSintTree.NodoListaComandos;
import compiler.AbsSintTree.NodoListaDeIds;
import compiler.AbsSintTree.NodoProgram;
import compiler.AbsSintTree.NodoTipo;

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

    private NodoProgram parseProgram(){
        NodoProgram progAST = new NodoProgram();
        accept(Token.PROGRAM);
        progAST.id = parseId();
        accept(Token.SEMICOLON);
        progAST.corpo = parseCorpo();
        return progAST;
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

    private NodoComando parseComando(){
        NodoComando comandoAST = null;
        switch (currentToken.token){
            // Vamos atribuir para comandoAST o tipo especial de comando ao qual ele tá associado. Por exemplo:
            case Token.IDENTIFIER: 
                // Aqui parseAtribuição vai retornar um NodoComandoAtrib, e comandoAST vai acabar "virando" desse tipo,
                // que é uma classe filha da classe comando.
                comandoAST = parseAtribuicao(); 
                break;
            case Token.IF:
                comandoAST = parseCondicional();
                break;
            case Token.BEGIN:
                comandoAST = parseComandoComposto();
                break;
            case Token.WHILE:
                comandoAST = parseIterativo();
                break;
            default:
                //Mensagem de erro - acho que faz no AST?
                break;
        }
        return comandoAST;
    }

    private NodoComandoAtrib parseAtribuicao(){             
        NodoComandoAtrib comAtribAST = new NodoComandoAtrib();
        comAtribAST.var = parseVariavel();                        
        accept(Token.BECOMES);                  
        comAtribAST.expressao = parseExpressao();                       
    }

    private void parseVariavel(){
        parseId();
    }
    
    private void parseId(){
        if(currentToken.token == Token.IDENTIFIER){
            acceptIt();
        } else {
            //Chamada de erro
        }
        // Talvez a seguinte forma seja ainda melhor, pois em uma linha ele cobre o que ele quer acima:
        //accept(Token.IDENTIFIER);

    }
    
    private NodoListaDeIds parseListaDeIds(){ // Lógica parecida com o declarações*, mas é id,(id)* (ao menos 1 obrigatório)
        //Referente ao id obrigatório:
        NodoListaDeIds listaIdsAST = new NodoListaDeIds();
        listaIdsAST.id = parseId();
        //Referente ao id*
        NodoListaDeIds last, aux;
        while (currentToken.token == Token.VIRGULA)
        {   
            acceptIt();
            aux = new NodoListaDeIds();
            aux.id = parseId();
            if(last == null){ // Acontece somente no primeiro caso que não é obrigatório (aka segundo caso)
                listaIdsAST.next = aux;  
                last = aux;
            } else {
                last.next = aux; // Diz que o último objeto criado é o next de last.
                last = aux; // Passa o ponteiro referente a last para aux, que é o último objeto criado.
            }
        }
        return listaIdsAST;
    }

    private NodoComandoComposto parseComandoComposto(){
        NodoComandoComposto comCompAST = new NodoComandoComposto();
        accept(Token.BEGIN);
        comCompAST.listComands = parseListaDeComandos();
        accept(Token.END); // Tem que fazer um token pra end também (pode ser quando recebe '}')
        return comCompAST;
    }

    private NodoComandoCond parseCondicional(){
        NodoComandoCond comCondAST = new NodoComandoCond();
        accept(Token.IF);
        comCondAST.expressao = parseExpressao();
        accept(Token.THEN);
        comCondAST.comandoIf = parseComando(); // Comando caso entre no IF
        
        if(currentToken.token == Token.ELSE){ // Caso entre no else
            acceptIt();
            comCondAST.comandoElse = parseComando();
        }
    }

    private NodoCorpo parseCorpo(){
        NodoCorpo corpoAST = new NodoCorpo();
        corpoAST.decs = parseDeclaracoes();
        corpoAST.comComp = parseComandoComposto();

    }

    private NodoDeclaracoes parseDeclaracoes(){
        NodoDeclaracoes first = null, last = null, aux;
        while (currentToken.token == Token.IDENTIFIER)
        {   
            aux = new NodoDeclaracoes(); // Enquanto tiver identifier, vai criando novos objetos
            aux.dec = parseDeclaracao(); // Armazenamento temporário do objeto
            if(first == null){ //caso seja o primeiro objeto 'declaração'
                first = aux;
            } else { // Caso nao seja o primeiro
                last.next = aux;
            }
            last = aux; // Pega o caso mais atual
            //No primeiro caso, ele vira a declaração 1
            //Na segunda iteração, ele primeiro diz q o next da declaração 1 é a declaração 2, e dps vira a declaração 2
            //Nas iterações seguintes ele continua na mesma lógica: primeiramente last é X, depois diz q X.next é o novo 
            //objeto aux criado, e por último last vira o novo objeto aux (que é o objeto X+1) 

            accept(Token.SEMICOLON); // Separador entre declarações
        }
        return first;
    }

    private NodoDeclaracao parseDeclaracao(){
        NodoDeclaracao decAST = new NodoDeclaracao();
        decAST.declaracoesDeVariaveis = parseDeclaracaoDeVariavel();
    }

    private NodoDeclaracaoVar parseDeclaracaoDeVariavel(){
        NodoDeclaracaoVar decVar = new NodoDeclaracaoVar();
        accept(Token.VAR);
        decVar.listIds = parseListaDeIds();
        accept(Token.BECOMES);
        decVar.tipo = parseTipo();
        return decVar;
    }

    private NodoTipo parseTipo(){
        NodoTipo tipoAST = new NodoTipo();
        switch (currentToken.token)
        {
            case Token.INTEGER: case Token.REAL: case Token.BOOLEAN: 
                tipoAST.tipoSimp = parseTipoSimples();
                break;
            default:
                // ERRO
                break;
        }
        return tipoAST;
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

    private NodoComandoIterativo parseIterativo(){
        NodoComandoIterativo comIteAST = new NodoComandoIterativo();
        accept(Token.WHILE);
        comIteAST.expressao = parseExpressao();
        accept(Token.DO);
        comIteAST.comando = parseComando();
        return comIteAST;
    }

    private NodoListaComandos parseListaDeComandos(){ // Mesma lógica de parte de declarações (a derivação é comando*)
        NodoListaComandos first = null, last, aux;
        while(currentToken.token == Token.IF ||
            currentToken.token == Token.WHILE ||
            currentToken.token == Token.BEGIN ||
            currentToken.token == Token.IDENTIFIER)
        {
            aux = new NodoListaComandos();
            aux.comando = parseComando(); 
            if(first == null){
                first = aux;
            } else {
                last.next = aux;
            }
            last = aux;
            accept(Token.SEMICOLON);
        }
        return first;
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
