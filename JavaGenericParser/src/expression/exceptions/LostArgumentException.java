package expression.exceptions;

public class LostArgumentException extends ParseExpressionException {
    public LostArgumentException(final String message, final int pos) {
        super(message + " at: " + pos);
    }
}
