package compiler.absSintTree;

public class NodoComandoCond extends NodoComando{
    public NodoExpressao expressao;
    // Qualquer um desses 2 abaixo pode ser null, dependendo das especificações de projeto e
    // Da maneira que a análise de contexto for implementada
    public NodoComando comandoIf;
    public NodoComando comandoElse;
}
