package expression;

import expression.mods.Operation;

public class Max<T> extends AbstractBinaryArithmeticOperation<T> {
    public Max(ArithmeticExpression<T> firstExpression,
               ArithmeticExpression<T> secondExpression,
               Operation<T> operation) {
        super(firstExpression, secondExpression, operation);
    }

    @Override
    protected T computation(T a, T b) {
        return operation.max(a, b);
    }

    @Override
    protected String getOperationSymbol() {
        return "max";
    }
}
