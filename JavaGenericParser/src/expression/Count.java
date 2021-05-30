package expression;


import expression.mods.Operation;

public class Count<T> extends AbstractUnaryArithmeticOperation<T> {
    public Count(ArithmeticExpression<T> expression, Operation<T> operation) {
        super(expression, operation);
    }

    @Override
    protected T computation(T a) {
        return operation.count(a);
    }

    @Override
    protected String getOperationSymbol() {
        return "count";
    }
}
