package compiler.sintaxTree;

public class NodoCorpo extends Ast {
    public NodoDeclaracoes decs;
    public NodoComandoComposto comComp;

    public void visit (Visitor v) {
        v.visitNodoCorpo(this);
    } 
}
