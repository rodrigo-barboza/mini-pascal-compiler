package compiler.sintaxTree;

public class NodoListaComandos extends Ast {
    public NodoComando comando; //Pode ser null
    public NodoListaComandos next; // Representativo do Comando*;

    public void visit (Visitor v) {
        v.visitNodoListaComandos(this);
    }  
}
