package compiler.sintaxTree;

import compiler.lexicalAnalyzer.Token;

public class NodoComandoAtrib extends NodoComando {
    public Token var; // Pode ser token mesmo, pois é um terminal
    public NodoExpressao expressao;

    public void visit (Visitor v) {
        v.visitNodoComandoAtrib(this);
    } 
}
