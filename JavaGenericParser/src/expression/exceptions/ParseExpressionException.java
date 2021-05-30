package expression.exceptions;

public class ParseExpressionException extends Exception {
    public ParseExpressionException(final String message, int pos) {
        super(pos + ": " + message);
    }

    public ParseExpressionException(final String message) {
        super(message);
    }
}
