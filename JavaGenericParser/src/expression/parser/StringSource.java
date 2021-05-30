package expression.parser;

import expression.exceptions.ParseExpressionException;

public class StringSource implements ExpressionSource {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public ParseExpressionException error(final String message) {
        return new ParseExpressionException(message, pos);
    }

    @Override
    public int getPos() {
        return pos;
    }
}