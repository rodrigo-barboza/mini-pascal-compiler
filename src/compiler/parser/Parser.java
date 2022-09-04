package compiler.parser;

import compiler.lexicalAnalyzer.CompilerScanner;
import compiler.lexicalAnalyzer.Token;
import compiler.sintaxTree.NodoComando;
import compiler.sintaxTree.NodoComandoAtrib;
import compiler.sintaxTree.NodoComandoComposto;
import compiler.sintaxTree.NodoComandoCond;
import compiler.sintaxTree.NodoComandoIterativo;
import compiler.sintaxTree.NodoCorpo;
import compiler.sintaxTree.NodoDeclaracao;
import compiler.sintaxTree.NodoDeclaracaoVar;
import compiler.sintaxTree.NodoDeclaracoes;
import compiler.sintaxTree.NodoExpressao;
import compiler.sintaxTree.NodoExpressaoSimples;
import compiler.sintaxTree.NodoExpressaoSimplesEstrela;
import compiler.sintaxTree.NodoFator;
import compiler.sintaxTree.NodoListaComandos;
import compiler.sintaxTree.NodoListaDeIds;
import compiler.sintaxTree.NodoProgram;
import compiler.sintaxTree.NodoTermo;
import compiler.sintaxTree.NodoTermoEstrela;
import compiler.sintaxTree.NodoTipo;

import java.io.IOException;

public class Parser {
    private Token currentToken;
    private CompilerScanner scan;

    public NodoProgram parse(String args) throws IOException {
        scan = new CompilerScanner(args);
        currentToken = scan.scan();
        
        return parseProgram();
    } 
    
    private void accept(byte expectedToken) throws ParserException {
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
    
    private NodoProgram parseProgram() throws ParserException {
        NodoProgram progAST = new NodoProgram();
        
        if (currentToken != null) {
            accept(Token.PROGRAM);
            progAST.id = parseId();
            accept(Token.SEMICOLON);
            progAST.corpo = parseCorpo();
        }
        
        return progAST;
    }


    private Token parseBoolLit(){
        Token boollitAST = null;
        switch (currentToken.token){
        case Token.TRUE:
            boollitAST = currentToken;
            acceptIt();
            break;
        case Token.FALSE:
            boollitAST = currentToken;
            acceptIt();
            break;
        }
        return boollitAST;
    }

    private NodoComando parseComando() throws ParserException {
        NodoComando comandoAST = null;
        switch (currentToken.token){
            case Token.IDENTIFIER:
                parseAtribuicao();
                break;
            case Token.IF:
                comandoAST = parseCondicional();
                break;
            case Token.BEGIN:
                comandoAST = parseComandoComposto();
                break;
            case Token.WHILE:
                parseIterativo();
                break;
        }
        return comandoAST;
    }

    private NodoComandoAtrib parseAtribuicao() throws ParserException {             
        NodoComandoAtrib comAtribAST = new NodoComandoAtrib();
        comAtribAST.var = parseVariavel();                        
        accept(Token.BECOMES);                  
        comAtribAST.expressao = parseExpressao();
        return comAtribAST;      
    }

    private Token parseVariavel(){ //podia ser simplificado, mas deixei pra seguir a orientação da árvore desenhada
        return parseId();
    }
    
    private Token parseId(){
        Token auxToken;
        if(currentToken.token == Token.IDENTIFIER){
            auxToken = currentToken;
            acceptIt();
            return auxToken;
        }
        return null;
    }
    
    private NodoListaDeIds parseListaDeIds(){ // Lógica parecida com o declarações*, mas é id,(id)* (ao menos 1 obrigatório)
        //Referente ao id obrigatório:
        NodoListaDeIds listaIdsAST = new NodoListaDeIds();
        listaIdsAST.id = parseId();
        //Referente ao id*
        NodoListaDeIds last=null, aux;
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

    private NodoComandoComposto parseComandoComposto() throws ParserException {
        NodoComandoComposto comCompAST = new NodoComandoComposto();
        accept(Token.BEGIN);
        comCompAST.listComands = parseListaDeComandos();
        accept(Token.END); // Tem que fazer um token pra end também (pode ser quando recebe '}')
        return comCompAST;
    }

    private NodoComandoCond parseCondicional() throws ParserException {
        NodoComandoCond comCondAST = new NodoComandoCond();
        accept(Token.IF);
        comCondAST.expressao = parseExpressao();
        accept(Token.THEN);
        comCondAST.comandoIf = parseComando(); // Comando caso entre no IF
        
        if(currentToken.token == Token.ELSE){ // Caso entre no else
            acceptIt();
            comCondAST.comandoElse = parseComando();
        }
        return comCondAST;
    }

    private NodoCorpo parseCorpo() throws ParserException {
        NodoCorpo corpoAST = new NodoCorpo();
        corpoAST.decs = parseDeclaracoes();
        corpoAST.comComp = parseComandoComposto();
        return corpoAST;
    }

    private NodoDeclaracoes parseDeclaracoes() throws ParserException {
        NodoDeclaracoes first = null, last = null, aux;

        while (currentToken.token == Token.VAR)
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

    private NodoDeclaracao parseDeclaracao() throws ParserException {
        NodoDeclaracao decAST = new NodoDeclaracao();
        decAST.declaracoesDeVariaveis = parseDeclaracaoDeVariavel();
        return decAST;
    }

    private NodoDeclaracaoVar parseDeclaracaoDeVariavel() throws ParserException {
        NodoDeclaracaoVar decVar = new NodoDeclaracaoVar();
        accept(Token.VAR);
        decVar.listIds = parseListaDeIds();
        accept(Token.COLON);
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
            case Token.EOF:
                acceptIt();
                break;
        }
        return tipoAST;
    }

    private Token parseTipoSimples(){
        Token tiposimplesAST = null;
        switch (currentToken.token)
        {
            case Token.INTEGER: case Token.REAL: case Token.BOOLEAN:
                tiposimplesAST = currentToken;
                acceptIt();
                break;
        }
        return tiposimplesAST;
    }

    private NodoExpressao parseExpressao() throws ParserException {
        NodoExpressao expressaoAST = new NodoExpressao();
        expressaoAST.expSimp1 = parseExpressaoSimples();
        if( currentToken.token == Token.MENOR || 
            currentToken.token == Token.MENORIGUAL || 
            currentToken.token == Token.MAIOR || 
            currentToken.token == Token.MAIORIGUAL || 
            currentToken.token == Token.IGUAL || 
            currentToken.token == Token.DIFERENTE )
        {
            expressaoAST.oprel = parseOpRel();
            //acceptIt(); // já tem um accept it no parseOpRel()
            expressaoAST.expSimp2 = parseExpressaoSimples();
        }
        return expressaoAST;
    }

    private NodoExpressaoSimples parseExpressaoSimples() throws ParserException {
        NodoExpressaoSimples expSimpAST = new NodoExpressaoSimples();
        expSimpAST.termo1 = parseTermo();
        //Para o grupo (op_ad + termo)*, temos:
        NodoExpressaoSimplesEstrela first = null, last=null, aux;
        while (currentToken.token == Token.SOMA ||
            currentToken.token == Token.SUBTRACAO || 
            currentToken.token == Token.OR){
                aux = new NodoExpressaoSimplesEstrela();
                aux.opAd = parseOpAd();
                aux.termo2 = parseTermo();
                if(first == null){
                    first = aux;
                } else {
                    last.next = aux;
                }
                last = aux;
        }
        expSimpAST.possibleNext = first;
        return expSimpAST;
    }
    
    private NodoTermo parseTermo() throws ParserException {
        NodoTermo termoAST = new NodoTermo();
        termoAST.fator = parseFator();
        //Para o grupo (op_mul + fator)*, temos:
        NodoTermoEstrela first=null, last=null, aux;
        while (currentToken.token == Token.MULTIPLICACAO || 
            currentToken.token == Token.DIVISAO || 
            currentToken.token == Token.AND){
                aux = new NodoTermoEstrela();
                aux.opMul = parseOpMul();
                aux.fator2 = parseFator();
                if(first == null){
                    first = aux;
                } else {
                    last.next = aux;
                }
                last = aux;
        }
        termoAST.possibleNext = first;
        return termoAST;
    }

    private NodoFator parseFator() throws ParserException {
        NodoFator fatorAST = new NodoFator();
        switch(currentToken.token){
            case Token.IDENTIFIER: case Token.TRUE: case Token.FALSE: case Token.INTLITERAL: 
            case Token.REAL: 
                fatorAST.terminal = parseLiteral();
                break;
            case Token.LPAREN:
                acceptIt();
                fatorAST.expressao = parseExpressao();
                accept(Token.RPAREN);
                break;
        }
        return fatorAST;
    }
    
    private Token parseLiteral() throws ParserException {
        Token literalAST=null;
        switch (currentToken.token)
        {
            case Token.BOOLEAN:
                literalAST = parseBoolLit();
                break;
            case Token.INTLITERAL:
                literalAST = parseIntLit();
                break;
            case Token.REAL:
                literalAST = parseFloatLit();
                break;
        }
        return literalAST;
    }

    private Token parseIntLit() throws ParserException { 
        Token intlitAST=currentToken; 
        // Teoricamente, a entrada nessa função só ocorre caso já tenha sido verificado o tipo
        // Erros sintáticos em teoria não podem ocorrer por causa dessa função, então (?)  
        accept(Token.INTLITERAL); 
        return intlitAST;
    }

    private Token parseFloatLit() throws ParserException { 
        // mesmo caso do parseintlit
        Token floatlitAST = currentToken;
        accept(Token.REAL);
        return floatlitAST;
    }

    private NodoComandoIterativo parseIterativo() throws ParserException {
        NodoComandoIterativo comIteAST = new NodoComandoIterativo();
        accept(Token.WHILE);
        comIteAST.expressao = parseExpressao();
        accept(Token.DO);
        comIteAST.comando = parseComando();
        return comIteAST;
    }

    private NodoListaComandos parseListaDeComandos() throws ParserException { // Mesma lógica de parte de declarações (a derivação é comando*)
        NodoListaComandos first = null, last=null, aux;
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

    private Token parseOpAd(){
        Token auxToken = null;
        switch(currentToken.token){
            case Token.SOMA: case Token.SUBTRACAO: case Token.OR:
                auxToken = currentToken;
                acceptIt();
                break;
        }
        return auxToken;
    }

    private Token parseOpMul(){
        Token auxToken = null;
        switch(currentToken.token){
            case Token.MULTIPLICACAO: case Token.DIVISAO: case Token.AND:
                auxToken = currentToken;
                acceptIt();
                break;
        }
        return auxToken;
    }

    private Token parseOpRel(){
        Token auxToken = null;
        switch(currentToken.token){
            case Token.MENOR: case Token.MENORIGUAL: case Token.IGUAL:
            case Token.MAIORIGUAL: case Token.MAIOR: case Token.DIFERENTE:
                auxToken = currentToken;
                acceptIt();
                break;
        }
        return auxToken;
    }
}
