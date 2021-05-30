package expression;

import expression.mods.Operation;

public class Sqrt<T> extends AbstractUnaryArithmeticOperation<T> {
    public Sqrt(ArithmeticExpression<T> expression,
                Operation<T> operation) {
        super(expression, operation);
    }

    @Override
    protected T computation(T a) {
        return operation.sqrt(a);
    }

    @Override
    protected String getOperationSymbol() {
        return "sqrt";
    }
}
