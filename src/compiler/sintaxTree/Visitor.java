package compiler.sintaxTree;

// import compiler.lexicalAnalyzer.Token;

public interface Visitor {
    public void visitNodoProgram (NodoProgram nodoProgram);
    public void visitNodoComando (NodoComando nodoComando);
    public void visitNodoComandoAtrib (NodoComandoAtrib nodoComandoAtrib);
    public void visitNodoComandoComposto (NodoComandoComposto nodoComandoComposto);
    public void visitNodoComandoCond (NodoComandoCond nodoComandoCond);
    public void visitNodoComandoIterativo (NodoComandoIterativo nodoComandoIterativo);
    public void visitNodoCorpo (NodoCorpo nodoCorpo);
    public void visitNodoDeclaracao (NodoDeclaracao nodoDeclaracao);
    public void visitNodoDeclaracaoVar (NodoDeclaracaoVar nodoDeclaracaoVar);
    public void visitNodoDeclaracoes (NodoDeclaracoes nodoDeclaracoes);
    public void visitNodoExpressao (NodoExpressao nodoExpressao);
    public void visitNodoExpressaoSimples (NodoExpressaoSimples nodoExpressaoSimples);
    public void visitNodoExpressaoSimplesEstrela (NodoExpressaoSimplesEstrela nodoExpressaoSimplesEstrela);
    public void visitNodoFator (NodoFator nodoFator);
    public void visitNodoListaComandos (NodoListaComandos nodoListaComandos);
    public void visitNodoListaDeIds (NodoListaDeIds nodoListaDeIds);
    public void visitNodoTermo (NodoTermo nodoTermo);
    public void visitNodoTermoEstrela (NodoTermoEstrela nodoTermoEstrela);
    public void visitNodoTipo (NodoTipo nodoTipo);
    // public void visitNodoTipo (Token nodoTipo);
}
