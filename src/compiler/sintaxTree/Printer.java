package compiler.sintaxTree;

// import compiler.lexicalAnalyzer.Token;

public class Printer implements Visitor {
    public int i = 0;

    public void print(NodoProgram nodoProgram) {
        System.out.println("---> Imprimindo a arvore\n");
        nodoProgram.visit(this);
    }

    public void indent() {
        for (int j = 0; j < i; j++) {
            System.out.print("|");
        }
    }

    @Override
    public void visitNodoProgram (NodoProgram nodoProgram) {
        if (nodoProgram != null) {
            indent();
            if (nodoProgram.id != null) {
                System.out.print("programa ");
                nodoProgram.id.visit(this);
                System.out.println("");
            }
            i++;
            if (nodoProgram.corpo != null) {
                nodoProgram.corpo.visit(this);
            }
            i--;
        }
    }

    @Override
    public void visitNodoComando(NodoComando nodoComando) {
        if (nodoComando != null) {
            nodoComando.visit(this);
        }
    }

    @Override
    public void visitNodoComandoAtrib (NodoComandoAtrib nodoComandoAtrib) {
        if (nodoComandoAtrib != null) {
            if (nodoComandoAtrib.var != null) {
                nodoComandoAtrib.var.visit(this);
            }
            i++;
            indent();
            System.out.println("=");
            if (nodoComandoAtrib.expressao != null) {
                nodoComandoAtrib.expressao.visit(this);
            }
            i--;
        }
    }

    @Override
    public void visitNodoComandoComposto(NodoComandoComposto nodoComandoComposto) {
        if (nodoComandoComposto != null) {
            if (nodoComandoComposto.listComands != null) {
                nodoComandoComposto.listComands.visit(this);
            }
        }
    }

    @Override
    public void visitNodoComandoCond(NodoComandoCond nodoComandoCond) {
        if (nodoComandoCond != null) {
            if (nodoComandoCond.expressao != null) {
                nodoComandoCond.expressao.visit(this);
            }
            i++;
            if (nodoComandoCond.comandoIf != null) {
                nodoComandoCond.comandoIf.visit(this);
                System.out.println("");
            }
            if (nodoComandoCond.comandoElse != null) {
                nodoComandoCond.comandoElse.visit(this);
                System.out.println("");
            }
            i--;
        }
    }

    @Override
    public void visitNodoComandoIterativo(NodoComandoIterativo nodoComandoIterativo) {
        if (nodoComandoIterativo != null) {
            if (nodoComandoIterativo.expressao != null) {
                nodoComandoIterativo.expressao.visit(this);
                System.out.println("");
            }
            if (nodoComandoIterativo.comando != null) {
                nodoComandoIterativo.comando.visit(this);
            }
        }
    }

    @Override
    public void visitNodoCorpo(NodoCorpo nodoCorpo) {
        if (nodoCorpo != null) {
            if (nodoCorpo.decs != null) {
                nodoCorpo.decs.visit(this);
                System.out.println("");
            }
            if (nodoCorpo.comComp != null) {
                nodoCorpo.comComp.visit(this);
            }
        }
    }

    @Override
    public void visitNodoDeclaracao(NodoDeclaracao nodoDeclaracao) {
        if (nodoDeclaracao != null) {
            nodoDeclaracao.declaracoesDeVariaveis.visit(this);
        }
    }

    @Override
    public void visitNodoDeclaracaoVar(NodoDeclaracaoVar nodoDeclaracaoVar) {
        if (nodoDeclaracaoVar != null) {
            if (nodoDeclaracaoVar.tipo != null) {
                nodoDeclaracaoVar.tipo.visit(this);
            }
            i++;
            if (nodoDeclaracaoVar.listIds != null) {
                nodoDeclaracaoVar.listIds.visit(this);
            }
            i--;
        }
    }

    @Override
    public void visitNodoDeclaracoes(NodoDeclaracoes nodoDeclaracoes) {
        if (nodoDeclaracoes != null) {
            if (nodoDeclaracoes.dec != null) {
                System.out.println("");
                nodoDeclaracoes.dec.visit(this);
            }
            if (nodoDeclaracoes.next != null) {
                i++;
                nodoDeclaracoes.next.visit(this);
                i--;
            }
        }
    }

    @Override
    public void visitNodoExpressao(NodoExpressao nodoExpressao) {
        if (nodoExpressao != null) {
            if (nodoExpressao.expSimp1 != null) {
                nodoExpressao.expSimp1.visit(this);
            }
            if (nodoExpressao.oprel != null) {
                nodoExpressao.oprel.visit(this);
            }
            if (nodoExpressao.expSimp2 != null) {
                nodoExpressao.expSimp2.visit(this);
            }
        }
    }

    @Override
    public void visitNodoExpressaoSimples(NodoExpressaoSimples nodoExpressaoSimples) {
        if (nodoExpressaoSimples != null) {
            if (nodoExpressaoSimples.termo1 != null) {
                nodoExpressaoSimples.termo1.visit(this);
            }
            if (nodoExpressaoSimples.possibleNext != null) {
                i++;
                nodoExpressaoSimples.possibleNext.visit(this);
                i--;
            }
        }
    }

    @Override
    public void visitNodoExpressaoSimplesEstrela(NodoExpressaoSimplesEstrela nodoExpressaoSimplesEstrela) {
        if (nodoExpressaoSimplesEstrela != null) {
            if (nodoExpressaoSimplesEstrela.opAd != null) {
                nodoExpressaoSimplesEstrela.opAd.visit(this);
            }
            if (nodoExpressaoSimplesEstrela.termo2 != null) {
                nodoExpressaoSimplesEstrela.termo2.visit(this);
            }
            if (nodoExpressaoSimplesEstrela.next != null) {
                i++;
                nodoExpressaoSimplesEstrela.next.visit(this);
                i--;
            }
        }
    }

    @Override
    public void visitNodoFator(NodoFator nodoFator) {
        if (nodoFator != null) {
            nodoFator.visit(this);
        }
    }

    @Override
    public void visitNodoListaComandos(NodoListaComandos nodoListaDeComandos) {
        if (nodoListaDeComandos != null) {
            if (nodoListaDeComandos.comando != null) {
                nodoListaDeComandos.comando.visit(this);
                System.out.println("");
            }
            if (nodoListaDeComandos.next != null) {
                nodoListaDeComandos.next.visit(this);
            }
        }
    }

    @Override
    public void visitNodoListaDeIds(NodoListaDeIds nodoListaDeIds) {
        if (nodoListaDeIds != null) {
            if (nodoListaDeIds.id != null) {
                indent();
                System.out.print(nodoListaDeIds.id.spelling);
                System.out.println("");
            }
            if (nodoListaDeIds.next != null) {
                i++;
                nodoListaDeIds.next.visit(this);
                i--;
            }
        }
    }

    @Override
    public void visitNodoTermo(NodoTermo nodoTermo) {
        if (nodoTermo != null) {
            if (nodoTermo.fator != null) {
                nodoTermo.fator.visit(this);
            }
            if (nodoTermo.possibleNext != null) {
                i++;
                nodoTermo.possibleNext.visit(this);
                i--;
            }
        }
    }

    @Override
    public void visitNodoTermoEstrela(NodoTermoEstrela nodoTermoEstrela) {
        if (nodoTermoEstrela != null) {
            if (nodoTermoEstrela.opMul != null) {
                nodoTermoEstrela.opMul.visit(this);
            }
            if (nodoTermoEstrela.fator2 != null) {
                nodoTermoEstrela.fator2.visit(this);
            }
            if (nodoTermoEstrela.next != null) {
                i++;
                nodoTermoEstrela.next.visit(this);
                i--;
            }
        }
    }

    @Override
    public void visitNodoTipo(NodoTipo nodoTipo) {
        if (nodoTipo != null) {
            nodoTipo.visit(this);
        }
    }

    // @Override
    // public void visitNodoTipo(Token nodoTipo) {
    //     if (nodoTipo != null) {
    //         nodoTipo.visit(this);
    //     }
    // }
}
