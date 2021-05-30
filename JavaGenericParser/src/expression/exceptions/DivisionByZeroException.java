package expression.exceptions;

public class DivisionByZeroException extends MathException {
    public DivisionByZeroException(final String message) {
        super(message);
    }
}
