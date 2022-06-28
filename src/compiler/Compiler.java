package compiler;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Compiler {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("C:\\Users\\Rodrigo\\Desktop\\teste.txt");
        Scanner scan = new Scanner(file);
        
        System.out.println(scan.next());
    }
    
}
