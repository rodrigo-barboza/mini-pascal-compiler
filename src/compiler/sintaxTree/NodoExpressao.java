package compiler.sintaxTree;

import compiler.lexicalAnalyzer.Token;

public class NodoExpressao extends Ast {
    public NodoExpressaoSimples expSimp1;
    //Aqui temos que expressão é seguido de VAZIO ou OP-REL + expSimp
    //Então ambos abaixo podem ser null, mas se um não for null, ambos devem existir
    public Token oprel;
    public NodoExpressaoSimples expSimp2;

    public void visit (Visitor v) {
        v.visitNodoExpressao(this);
    }  
}
