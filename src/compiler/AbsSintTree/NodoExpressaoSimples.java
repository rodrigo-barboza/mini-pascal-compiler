package compiler.AbsSintTree;

public class NodoExpressaoSimples extends NodoExpressao{
    public NodoTermo termo1;
    public NodoExpressaoSimplesEstrela possibleNext;

    //Então ambos abaixo podem ser null, mas se um não for null, ambos devem existir
    //Como ambos podem existir e ter uma "estrela", indicando possibilidade de vários, então foi criada outra classe,
    //A classe NodoExpressaoSimplesEstrela.java, que contém os campos a seguir:
    /* public Token opAd; 
    public NodoTermo termo2;
    public NodoExpressaoSimples next; */
}
