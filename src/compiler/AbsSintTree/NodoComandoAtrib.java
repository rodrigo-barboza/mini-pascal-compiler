package compiler.AbsSintTree;

import compiler.Token;

public class NodoComandoAtrib extends NodoComando{
    public Token var; // Pode ser token mesmo, pois é um terminal
    public NodoExpressao expressao;
}
