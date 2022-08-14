package compiler.parser;

import compiler.Token;
import java.io.FileNotFoundException;

public class ParserException extends FileNotFoundException {
    public ParserException(Token currentToken, String expected){
        super("sintax error: " + currentToken.line + ":"
               + currentToken.column + ": " 
               + "expected '" + expected  + "' but received '"
               + Token.spellings[currentToken.token]  + "'");
    }
}
