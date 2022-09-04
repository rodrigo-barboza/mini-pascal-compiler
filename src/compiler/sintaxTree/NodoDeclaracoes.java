package compiler.sintaxTree;

public class NodoDeclaracoes extends Ast {
    public NodoDeclaracao dec;
    public NodoDeclaracoes next;

    public void visit (Visitor v) {
        v.visitNodoDeclaracoes(this);
    }  
}
