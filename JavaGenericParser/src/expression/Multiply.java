package expression;


import expression.mods.Operation;

public class Multiply<T> extends AbstractBinaryArithmeticOperation<T> {
    public Multiply(ArithmeticExpression<T> firstExpression,
                    ArithmeticExpression<T> secondExpression,
                    Operation<T> operation) {
        super(firstExpression, secondExpression, operation);
    }

    @Override
    protected T computation(T a, T b) {
        return operation.multiply(a, b);
    }

    @Override
    protected String getOperationSymbol() {
        return "*";
    }
}