package expression.exceptions;

public class IllegalExpressionException extends ParseExpressionException {
    public IllegalExpressionException(final String message, int pos) {
        super(message, pos);
    }

}
