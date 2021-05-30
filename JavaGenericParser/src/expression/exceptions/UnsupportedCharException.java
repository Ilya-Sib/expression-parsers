package expression.exceptions;

public class UnsupportedCharException extends UnexpectedCharException {
    public UnsupportedCharException(char found, int pos) {
        super("Unsupported char at " + pos + ": " + found);
    }
}
