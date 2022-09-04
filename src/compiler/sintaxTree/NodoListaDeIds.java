package compiler.sintaxTree;

import compiler.lexicalAnalyzer.Token;

public class NodoListaDeIds extends Ast {
    public Token id;
    public NodoListaDeIds next; // representação do id* que segue

    public void visit (Visitor v) {
        v.visitNodoListaDeIds(this);
    }  
}
