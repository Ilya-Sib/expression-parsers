package expression;


import expression.mods.Operation;

public class Subtract<T> extends AbstractBinaryArithmeticOperation<T> {
    public Subtract(ArithmeticExpression<T> firstExpression,
                    ArithmeticExpression<T> secondExpression,
                    Operation<T> operation) {
        super(firstExpression, secondExpression, operation);
    }

    @Override
    protected T computation(T a, T b) {
        return operation.subtract(a, b);
    }

    @Override
    protected String getOperationSymbol() {
        return "-";
    }
}
