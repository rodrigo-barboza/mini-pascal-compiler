package compiler;

import compiler.parser.Parser;
import java.io.IOException;

public class Compiler {

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser(args[0]);
    }
    
}
