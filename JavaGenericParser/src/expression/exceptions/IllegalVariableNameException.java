package expression.exceptions;

public class IllegalVariableNameException extends ParseExpressionException {
    public IllegalVariableNameException(final String message) {
        super(message);
    }
}
