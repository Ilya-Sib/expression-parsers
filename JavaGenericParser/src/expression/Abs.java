package expression;


import expression.mods.Operation;

public class Abs<T> extends AbstractUnaryArithmeticOperation<T> {
    public Abs(ArithmeticExpression<T> expression, Operation<T> operation) {
        super(expression, operation);
    }

    @Override
    protected T computation(T a) {
        return operation.abs(a);
    }

    @Override
    protected String getOperationSymbol() {
        return "abs";
    }
}
