package compiler.absSintTree;

import compiler.Token;

public class NodoFator extends Ast {
    //Um dos dois PRECISA ser null: 
    public Token terminal; 
    // Esse terminal pode ser um id, bool-int, float-int ou int-lit
    // A análise individual do tipo que esse terminal PRECISA ser é trabalho da análise de contexto;
    public NodoExpressao expressao;
}
