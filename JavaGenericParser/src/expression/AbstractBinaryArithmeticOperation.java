package expression;

import expression.mods.Operation;

public abstract class AbstractBinaryArithmeticOperation<T> implements ArithmeticExpression<T> {
    private final ArithmeticExpression<T> firstExpression, secondExpression;
    protected final Operation<T> operation;

    public AbstractBinaryArithmeticOperation(ArithmeticExpression<T> firstExpression,
                                             ArithmeticExpression<T> secondExpression,
                                             Operation<T> operation) {
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return '(' + firstExpression.toString() + ' ' + this.getOperationSymbol() + ' '
                + secondExpression.toString() + ')';
    }

    @Override
    public T evaluate(T x) {
        return computation(firstExpression.evaluate(x), secondExpression.evaluate(x));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return computation(firstExpression.evaluate(x, y, z), secondExpression.evaluate(x, y, z));
    }

    @Override
    public int hashCode() {
        return 31 * (31 * (31 * firstExpression.hashCode() + 31) + secondExpression.hashCode()) + this.getClass().hashCode();
    }

    protected abstract T computation(T a, T b);
    protected abstract String getOperationSymbol();
}
