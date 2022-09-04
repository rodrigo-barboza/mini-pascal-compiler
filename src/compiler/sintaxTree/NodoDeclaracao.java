package compiler.sintaxTree;

public class NodoDeclaracao extends Ast {
    public NodoDeclaracaoVar declaracoesDeVariaveis;

    public void visit (Visitor v) {
        v.visitNodoDeclaracao(this);
    }  
}
