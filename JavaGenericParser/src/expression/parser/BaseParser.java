package expression.parser;

import expression.exceptions.UnexpectedCharException;
import expression.exceptions.ParseExpressionException;

public class BaseParser {
    public static final char END = '\0';
    private final ExpressionSource source;
    private char ch = 0xffff;
    private String token;

    protected BaseParser(final ExpressionSource source) {
        this.source = source;
        this.token = "";
    }

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : END;
    }

    protected boolean test(char expected) {
        if (!hasToken() && ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected boolean test(String value) {
        if (!hasToken() && hasNextToken()) {
            nextToken();
        }
        if (value.equals(token)) {
            nextToken();
            return true;
        }
        return false;
    }

    protected void nextToken() {
        StringBuilder stringBuilder = new StringBuilder();
        while (!eof() && Character.isLetterOrDigit(ch)) {
            stringBuilder.append(ch);
            nextChar();
        }
        token = stringBuilder.toString();
    }

    protected boolean hasToken() {
        return token.length() > 0;
    }

    protected String getToken() {
        return token;
    }

    protected boolean hasNextToken() {
        return Character.isLetter(ch);
    }

    protected void expect(final char c) throws ParseExpressionException {
        if (ch != c) {
            throw new UnexpectedCharException(c, ch, source.getPos());
        }
        nextChar();
    }

    protected void expect(final String value) throws ParseExpressionException {
        for (char c : value.toCharArray()) {
            expect(c);
        }
        checkLetterOrDigit(ch);
    }

    private void checkLetterOrDigit(char ch) throws ParseExpressionException {
        if (Character.isLetterOrDigit(ch)) {
            throw new UnexpectedCharException("unexpected " + ch + " found at " + getSource().getPos());
        }
    }

    protected ExpressionSource getSource() {
        return source;
    }

    protected char getChar() {
        return ch;
    }

    protected boolean eof() {
        return test(END);
    }

    protected ParseExpressionException error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return token.length() == 0 && from <= ch && ch <= to;
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(ch)) {
            nextChar();
        }
    }
}

