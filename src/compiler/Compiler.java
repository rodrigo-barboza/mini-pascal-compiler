package compiler;

import java.io.FileNotFoundException;

public class Compiler {

    public static void main(String[] args) throws FileNotFoundException {
        CompilerScanner teste = new CompilerScanner();
        
        for (int x = 0; x < 5; x++)
            teste.scan();
    }
    
}
