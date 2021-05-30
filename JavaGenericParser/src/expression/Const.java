package expression;

public class Const<T> implements ArithmeticExpression<T> {
    private final T x;

    public Const(T x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return x.toString();
    }

    @Override
    public T evaluate(T x) {
        return this.x;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return this.x;
    }

    @Override
    public int hashCode() {
        return x.hashCode();
    }
}
