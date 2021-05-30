package expression;

import expression.mods.Operation;

public class Negate<T> extends AbstractUnaryArithmeticOperation<T> {
    public Negate(ArithmeticExpression<T> expression,
                  Operation<T> operation) {
        super(expression, operation);
    }

    @Override
    protected T computation(T a) {
        return operation.negate(a);
    }

    @Override
    protected String getOperationSymbol() {
        return "-";
    }
}
