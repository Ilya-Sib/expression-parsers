package expression.mods;

public interface Operation<T> {
    T abs(T a);
    T add(T a, T b);
    T count(T a);
    T divide(T a, T b);
    T max(T a, T b);
    T min(T a, T b);
    T mod(T a, T b);
    T multiply(T a, T b);
    T negate(T a);
    T sqrt(T a);
    T square(T a);
    T subtract(T a, T b);
    T parse(String s);
}
