package compiler.sintaxTree;

public class NodoDeclaracaoVar extends Ast {
    public NodoListaDeIds listIds;
    public NodoTipo tipo;

    public void visit (Visitor v) {
        v.visitNodoDeclaracaoVar(this);
    }  
}
