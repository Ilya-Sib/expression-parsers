package expression.exceptions;

public class UnexpectedCharException extends ParseExpressionException {
    public UnexpectedCharException(char expected, char found, int pos) {
        super("Expected at " + pos + " '" + expected + "', found "
               + (found == '\0' ? "end of expression" : "'" + found + "'"));
    }

    public UnexpectedCharException(final String message) {
        super(message);
    }
}
