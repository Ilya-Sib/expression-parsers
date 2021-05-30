package expression;

import expression.mods.Operation;

public class Mod<T> extends AbstractBinaryArithmeticOperation<T> {
    public Mod(ArithmeticExpression<T> firstExpression,
               ArithmeticExpression<T> secondExpression,
               Operation<T> operation) {
        super(firstExpression, secondExpression, operation);
    }

    @Override
    protected T computation(T a, T b) {
        return operation.mod(a, b);
    }

    @Override
    protected String getOperationSymbol() {
        return "mod";
    }
}
