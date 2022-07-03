package compiler;

import java.io.IOException;
import java.nio.file.Path;

public class Compiler {

    public static void main(String[] args) throws IOException {
        //Endereço do arquivo a ser lido:
        Path testFile = Path.of("C:\\Users\\pcesa\\Desktop\\semestre 2021.2\\compiladores\\teste.txt");
        CompilerScanner teste = new CompilerScanner(testFile);
        teste.lexScan();
    }
    
}
