package compiler;

import compiler.parser.Parser;
import java.io.IOException;
import compiler.sintaxTree.NodoProgram;
import compiler.sintaxTree.Printer;

public class Compiler {

    public static void main(String[] args) throws IOException {
        NodoProgram p;
        Parser parser = new Parser();
        Printer printer = new Printer();

        p = parser.parse(args[0]);
        printer.print(p);
    }
}
