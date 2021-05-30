package expression;

public interface GenericExpression<T> {
    T evaluate(T x);
}
