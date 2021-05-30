package expression;

import expression.mods.Operation;

public abstract class AbstractUnaryArithmeticOperation<T> implements ArithmeticExpression<T> {
    private final ArithmeticExpression<T> expression;
    protected final Operation<T> operation;

    public AbstractUnaryArithmeticOperation(ArithmeticExpression<T> expression,
                                            Operation<T> operation) {
        this.expression = expression;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return getOperationSymbol() + "(" + expression.toString() + ")";
    }

    @Override
    public T evaluate(T x) {
        return computation(expression.evaluate(x));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return computation(expression.evaluate(x, y, z));
    }

    @Override
    public int hashCode() {
        return 31 * (31 * expression.hashCode() + 31) + this.getClass().hashCode();
    }

    protected abstract T computation(T a);
    protected abstract String getOperationSymbol();
}
