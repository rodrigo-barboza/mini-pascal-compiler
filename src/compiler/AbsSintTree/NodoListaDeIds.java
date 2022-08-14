package compiler.absSintTree;

import compiler.Token;

public class NodoListaDeIds extends Ast {
    public Token id;
    public NodoListaDeIds next; // representação do id* que segue
}
