package expression;

import expression.mods.Operation;

public class Min<T> extends AbstractBinaryArithmeticOperation<T> {
    public Min(ArithmeticExpression<T> firstExpression,
               ArithmeticExpression<T> secondExpression,
               Operation<T> operation) {
        super(firstExpression, secondExpression, operation);
    }

    @Override
    protected T computation(T a, T b) {
        return operation.min(a, b);
    }

    @Override
    protected String getOperationSymbol() {
        return "min";
    }
}
