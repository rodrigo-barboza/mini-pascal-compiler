package compiler.sintaxTree;

public class NodoComando extends Ast {
    public void visit (Visitor v) {
        v.visitNodoComando(this);
    }  
}
