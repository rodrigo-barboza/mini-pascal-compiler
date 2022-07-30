package compiler;

import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class Compiler {

    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        // isso deve acontecer no sintático
        Scanner scanner = new Scanner(file);
        int currentLine = 0, currentColumn = -1;
        String line = "";
        
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            
            while (currentColumn < (line.length()-1)) {
                CompilerScanner scan = new CompilerScanner(
                    line, 
                    currentLine,
                    currentColumn
                );
                
                scan.scan();
                currentLine = scan.getCurrentLine();
                currentColumn = scan.getCurrentColumn();
            }
        }
        
    }
    
}
