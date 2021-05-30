package expression.parser;

import expression.*;
import expression.exceptions.*;
import expression.exceptions.ParseExpressionException;
import expression.mods.Operation;

import java.util.Set;

public class ExpressionParser<T> implements GenericParser<T> {
    private final Operation<T> operation;

    public ExpressionParser(final Operation<T> operation) {
        this.operation = operation;
    }

    @Override
    public GenericTripleExpression<T> parse(String expression) throws ParseExpressionException {
        return parse(new StringSource(expression));
    }

    public GenericTripleExpression<T> parse(ExpressionSource source) throws ParseExpressionException {
        return new TripleExpressionParser<T>(source, operation).parseTriple();
    }

    private static class TripleExpressionParser<T> extends BaseParser {
        private final Set<String> OPERATIONS = Set.of(
                "sqrt", "square", "abs", "min", "max", "count", "mod"
        );

        private final Operation<T> operation;

        public TripleExpressionParser(final ExpressionSource source,
                                      final Operation<T> operation) {
            super(source);
            this.operation = operation;
            nextChar();
        }

        public GenericTripleExpression<T> parseTriple() throws ParseExpressionException {
            ArithmeticExpression<T> result = parseMinMax();
            if (eof()) {
                return result;
            }
            unsupportedCharChecker();
            varNameChecker();
            throw error("End of expression expected");
        }

        private ArithmeticExpression<T> parseMinMax() throws expression.exceptions.ParseExpressionException {
            ArithmeticExpression<T> result = parseAddSubtract();
            while (true) {
                if (test("max")) {
                    result = new Max<>(result, parseAddSubtract(), operation);
                } else if (test("min")) {
                    result = new Min<>(result, parseAddSubtract(), operation);
                } else {
                    return result;
                }
            }
        }

        private ArithmeticExpression<T> parseAddSubtract() throws expression.exceptions.ParseExpressionException {
            ArithmeticExpression<T> result = parseMultiplyDivideMod();
            while (true) {
                if (test('+')) {
                    result = new Add<>(result, parseMultiplyDivideMod(), operation);
                } else if (test('-')) {
                    result = new Subtract<>(result, parseMultiplyDivideMod(), operation);
                } else {
                    return result;
                }
            }
        }

        private ArithmeticExpression<T> parseMultiplyDivideMod() throws expression.exceptions.ParseExpressionException {
            ArithmeticExpression<T> result = parseConstVariableUnary();
            while (true) {
                if (test('*')) {
                    result = new Multiply<>(result, parseConstVariableUnary(), operation);
                } else if (test('/')) {
                    result = new Divide<>(result, parseConstVariableUnary(), operation);
                } else if (test("mod")) {
                    result = new Mod<>(result, parseConstVariableUnary(), operation);
                } else {
                    return result;
                }
            }
        }

        private ArithmeticExpression<T> parseConstVariableUnary() throws expression.exceptions.ParseExpressionException {
            ArithmeticExpression<T> result;
            skipWhitespace();
            if (test('(')) {
                result = parseMinMax();
                expect(')');
            } else if (test("sqrt")) {
                result = new Sqrt<>(parseConstVariableUnary(), operation);
            } else if (test("abs")) {
                result = new Abs<>(parseConstVariableUnary(), operation);
            } else if (test("count")) {
                result = new Count<>(parseConstVariableUnary(), operation);
            } else if (test("square")) {
                result = new Square<>(parseConstVariableUnary(), operation);
            } else if (test('-')) {
                result = between('0', '9') ? parseConst("-") :
                        new Negate<>(parseConstVariableUnary(), operation);
            } else if (isNotOperationToken()) {
                varNameChecker();
                result = new Variable<>(getToken());
                nextToken();
            } else if (between('0', '9')) {
                result = parseConst("");
            } else {
                unsupportedCharChecker();
                throw new LostArgumentException("Lost argument", getSource().getPos());
            }
            skipWhitespace();
            return result;
        }

        private ArithmeticExpression<T> parseConst(String prefix) throws expression.exceptions.ParseExpressionException {
            StringBuilder constNumber = new StringBuilder(prefix);
            while (between('0', '9')) {
                constNumber.append(getChar());
                nextChar();
            }
            try {
                return new Const<>(operation.parse(constNumber.toString()));
            } catch (NumberFormatException e) {
                throw new ConstFormatException("Wrong const number: " + constNumber.toString());
            }
        }

        private boolean isNotOperationToken() {
            return hasToken() && !OPERATIONS.contains(getToken());
        }

        private void varNameChecker() throws expression.exceptions.ParseExpressionException {
            if (getToken().length() > 0 && !(getToken().equals("x")
                    || getToken().equals("y") || getToken().equals("z"))) {
                throw new IllegalVariableNameException("Variable can't have name: " + getToken());
            }
        }

        private void unsupportedCharChecker() throws ParseExpressionException {
            skipWhitespace();
            if (getChar() != END && !(Character.isLetterOrDigit(getChar()) || getChar() == '(' || getChar() == ')'
                    || getChar() == '+' || getChar() == '/' || getChar() == '-' || getChar() == '*')) {
                throw new UnsupportedCharException(getChar(), getSource().getPos());
            }
        }
    }
}
