package compiler.sintaxTree;

public class NodoTermo extends Ast {
    public NodoFator fator;
    public NodoTermoEstrela possibleNext;

    public void visit (Visitor v) {
        v.visitNodoTermo(this);
    } 
}
