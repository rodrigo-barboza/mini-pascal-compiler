package compiler.sintaxTree;

import compiler.lexicalAnalyzer.Token;

public class NodoProgram extends Ast {
    //public NodoId id;
    public Token id;
    public NodoCorpo corpo;

    public void visit (Visitor v) {
        v.visitNodoProgram(this);
    }    
}
