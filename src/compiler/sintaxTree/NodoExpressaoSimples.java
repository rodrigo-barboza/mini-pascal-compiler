package compiler.sintaxTree;

public class NodoExpressaoSimples extends NodoExpressao{
    public NodoTermo termo1;
    public NodoExpressaoSimplesEstrela possibleNext;

    public void visit (Visitor v) {
        v.visitNodoExpressaoSimples(this);
    }
}
