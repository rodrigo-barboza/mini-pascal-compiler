package compiler.parser;

import java.io.FileNotFoundException;

import compiler.lexicalAnalyzer.Token;

public class ParserException extends FileNotFoundException {
    public ParserException(Token currentToken, String expected){
        super("sintax error: " + currentToken.line + ":"
               + currentToken.column + ": " 
               + "expected '" + expected  + "' but received '"
               + Token.spellings[currentToken.token]  + "'");
    }
}
