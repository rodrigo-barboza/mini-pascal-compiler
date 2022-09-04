package compiler.sintaxTree;

import compiler.lexicalAnalyzer.Token;

public class NodoTipo extends Ast {
    public Token tipoSimp;

    public void visit (Visitor v) {
        v.visitNodoTipo(this);
    }
}
