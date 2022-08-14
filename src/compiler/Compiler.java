package compiler;

import compiler.parser.Parser;
import java.io.IOException;

public class Compiler {

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser();
        parser.parse(args[0]);
        System.out.println("parser completed.");
    }
    
}
