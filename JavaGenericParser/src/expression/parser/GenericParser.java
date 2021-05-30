package expression.parser;

import expression.GenericTripleExpression;
import expression.exceptions.ParseExpressionException;

public interface GenericParser<T> {
    GenericTripleExpression<T> parse(String expression) throws ParseExpressionException;
}