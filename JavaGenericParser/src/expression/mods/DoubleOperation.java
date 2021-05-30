package expression.mods;

public class DoubleOperation implements Operation<Double> {
    @Override
    public Double abs(Double a) {
        return a >= 0 ? a : -a;
    }

    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double count(Double a) {
        return (double) Long.bitCount(Double.doubleToLongBits(a));
    }

    @Override
    public Double divide(Double a, Double b) {
        return a / b;
    }

    @Override
    public Double max(Double a, Double b) {
        return a > b ? a : b;
    }

    @Override
    public Double min(Double a, Double b) {
        return a < b ? a : b;
    }

    @Override
    public Double mod(Double a, Double b) {
        return a % b;
    }

    @Override
    public Double multiply(Double a, Double b) {
        return a * b;
    }

    @Override
    public Double negate(Double a) {
        return -a;
    }

    @Override
    public Double sqrt(Double a) {
        return Math.sqrt(a);
    }

    @Override
    public Double square(Double a) {
        return a * a;
    }

    @Override
    public Double subtract(Double a, Double b) {
        return a - b;
    }

    @Override
    public Double parse(String s) {
        return Double.parseDouble(s);
    }
}
