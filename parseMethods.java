/*<atribuição> ::=
        <variável> := <expressão>*/

private void parseAtribuicao(){             //<atribuição> ::=
    parseVariavel();                        //<variável>
    accept(Token.BECOMES);                  //:=
    parseExpressao();                       //<expressão>
}

/*<bool-lit> ::=
        true | false */

private void parseBoolLit()
{
    switch (currentToken.kind)
    {
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

/*<comando> ::=
    <atribuição>
    | <condicional>
    | <iterativo>
    | <comando-composto>
*/

private void parseBoolLit(){
    switch (currentToken.kind)
    {
    case Token.ATRIBUICAO:
        parseAtribuicao();
        break;
    case Token.ITERATIVO:
        parseIterativo();
        break;
    case Token.COMANDOCOMPOSTO:
        parseComandoComposto();
        break;
    case Token.CONDICIONAL:
        parseCondicional();
        break;
    default:
        // ERRO
        break;
    }
}

/*<comando-composto> ::=
    begin <lista-de-comandos> end
*/

private void parseComandoComposto(){
    accept(Token.BEGIN);
    parseListaDeComandos();
    accept(Token.END);
}

/*<condicional> ::=
    if <expressão> then <comando> ( else <comando> | <vazio> )
*/

private void parseCondicional(){
    accept(Token.IF);
    parseExpressao();
    accept(Token.THEN);
    parseComando();
    switch (currentToken.kind)
    {
    case Token.ELSE:
        acceptIt();
        parseComando();
        break;
    case Token.EOF: //VAZIO???
        acceptIt();
        break;
    default:
        //ERRO
        break;
    }
}

/*<corpo> ::=
<declarações><comando-composto>
*/

private void parseCorpo(){
    parseDeclaracoes();
    parseComandoComposto();
}

/*<declaração> ::=
    <declaração-de-variável>
*/

private void parseDeclaracao(){
    parseDeclaracaoDeVariavel();
}

/*<declaração-de-variável> ::=
    var <lista-de-ids> : <tipo>
*/

private void parseDeclaracaoDeVariavel(){
    accept(Token.VAR) //VAR FOI DECLARADO NO LEXICO?
    parseListaDeIds();
    accept(Token.COLON);
    parseTipo();
}

//<declarações> ::= (<declaração>)*

private void parseDeclaracoes()
{
    while (currentToken.kind == Token.DECLARACAO)
    {
        parseDeclaracao();
    }
    
}

/*<expressão-simples> ::= 
    <termo>(<op-ad> <termo>)*
 */

private void parseExpressao(){
    parseTermo();
    while (currentToken.kind == Token.OPAD)
    {
        parseOpAd();
        ParseTermo();
    }
}

/*<float-lit> ::=  
    <int-lit> . (<int-lit> | vazio)   
    | .<int-lit>
*/

private void parseFloatLit(){
    switch (currentToken.kind)
    {
        case Token.INTLIT:
            parseIntLit();
            accept(Token.PONTO);
            switch (currentToken.kind) {
                case Token.INTLIT:
                    parseIntLit();
                    break;
                case Token.EOF: // VAZIO???
                    acceptIt(); //ou parseVazio();
                    break;
                default:
                    // ERRO
                    break;
            }
        break;

        case Token.PONTO:
            parseIntLit();
        break;

        default:
            // ERRO
        break;
    }
}

// <id> ::= <letra>(<letra>|<digito>)*

private void parseId(){
    parseLetra();
    while (currentToken.kind == Token.LETRA || currentToken.kind == Token.DIGITO)
    {
        if (currentToken.kind == Token.LETRA)
            parseLetra();
        else
            parseDigito();
        
    }
}

//<int-lit> ::=<digito>(<digito>)*

private void parseIntLit(){
    parseDigito();
    while (currentToken.kind == Token.DIGITO){
        parseDigito();
    }
}

//<iterativo> ::= while <expressão> do <comando>

private void parseIterativo(){
    accept(Token.WHILE);
    parseExpressao();
    accept(Token.DO);
    parseComando();
}
//<lista-de-comandos> ::= (<comando>)*

private void parseListaDeComando()
{
    while (currentToken.kind == Token.COMANDO)
    {
        parseComando();
    }
}

//<lista-de-ids> ::= <id>(,<id>)*

private void parseListaDeIds(){
    parseId();
    while (currentToken.kind == Token.VIRGULA)
    {   
        acceptIt();
        parseId();
    }
}

/*<literal> ::=
    <bool-lit>
    | <int-lit>
    | <float-lit>
*/

private void parseLiteral(){
    switch (currentToken.kind)
    {
        case Token.BOOLLIT:
            parseBoolLit();
            break;
        case Token.INTLIT:
            parseIntLit();
            break;
        case Token.FLOATLIT:
            parseFloatLit();
            break;
        default:
            //ERRO
            break;
    }
}

//<programa> ::= program<id>;<corpo>.

private void parsePrograma(){
    accept(Token.PROGRAM);
    parseId();
    accept(Token.SEMICOLON);
    parseCorpo();
    accept(Token.PONTO);
}

//<termo> ::= <fator>(<op-mul> <fator>)*

private void parseTermo(){
    parseFator();
    while (currentToken.kind == Token.OPMUL)
    {
        parseOpMul();
        parseFator();
    }
}

//<tipo> ::= | <tipo-simples>

private void parseTipo(){
    switch (currentToken.kind)
    {
        case Token.TIPOSIMPLES:
            parseTipoSimples();
            break;
        case Token.EOF: // VAZIO???
            acceptIt();
            break;
        default:
            // ERRO
            break;
    }
}

/*<tipo-simples> ::=
    integer
    | real
    | boolean
*/

private void parseTipoSimples(){
        switch (currentToken.kind)
        {
            case Token.INTEGER
                acceptIt();
                break;
            case Token.REAL: 
                acceptIt();
                break;
            case Token.BOOLEAN:
                acceptIt();
                break;
            default:
                // ERRO
                break;
        }
}
//<variável> ::= <id>

private void parseVariavel(){
    parseId();
}

//<vazio> ::=

private void parseVazio(){
    acceptIt();
}