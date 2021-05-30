package expression.parser;

import expression.exceptions.ParseExpressionException;

public interface ExpressionSource {
    boolean hasNext();
    char next();
    ParseExpressionException error(final String message);
    int getPos();
}
