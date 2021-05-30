package expression;


import expression.mods.Operation;

public class Square<T> extends AbstractUnaryArithmeticOperation<T> {
    public Square(ArithmeticExpression<T> expression, Operation<T> operation) {
        super(expression, operation);
    }

    @Override
    protected T computation(T a) {
        return operation.square(a);
    }

    @Override
    protected String getOperationSymbol() {
        return "square";
    }
}
