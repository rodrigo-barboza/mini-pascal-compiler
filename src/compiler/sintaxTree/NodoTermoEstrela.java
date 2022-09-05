package compiler.sintaxTree;

import compiler.lexicalAnalyzer.Token;

public class NodoTermoEstrela extends NodoTermo{
    public Token opMul;
    public NodoFator fator2;
    public NodoTermoEstrela next;

    public void visit (Visitor v) {
        v.visitNodoTermoEstrela(this);
    } 
}
