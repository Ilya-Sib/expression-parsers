package expression;

public class Variable<T> implements ArithmeticExpression<T> {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public T evaluate(T x) {
        return x;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        T result;
        switch (name) {
            case "x":
                result = x;
                break;
            case "y":
                result = y;
                break;
            case "z":
                result = z;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode();
    }
}
