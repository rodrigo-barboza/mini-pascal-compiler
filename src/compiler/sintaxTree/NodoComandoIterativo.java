package compiler.sintaxTree;

public class NodoComandoIterativo extends NodoComando{
    public NodoExpressao expressao;
    public NodoComando comando;

    public void visit (Visitor v) {
        v.visitNodoComandoIterativo(this);
    } 
}
