package expression;

import expression.mods.Operation;

public class Add<T> extends AbstractBinaryArithmeticOperation<T> {
    public Add(ArithmeticExpression<T> firstExpression,
               ArithmeticExpression<T> secondExpression,
               Operation<T> operation) {
        super(firstExpression, secondExpression, operation);
    }

    @Override
    protected T computation(T a, T b) {
        return operation.add(a, b);
    }

    @Override
    protected String getOperationSymbol() {
        return "+";
    }
}
