package compiler.sintaxTree;

public class NodoComandoComposto extends NodoComando {
    public NodoListaComandos listComands;

    public void visit (Visitor v) {
        v.visitNodoComandoComposto(this);
    }  
}
