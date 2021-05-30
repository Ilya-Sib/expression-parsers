package expression.mods;

import expression.exceptions.DivisionByZeroException;

public class LongOperation implements Operation<Long> {
    @Override
    public Long abs(Long a) {
        return a >= 0 ? a : -a;
    }

    @Override
    public Long add(Long a, Long b) {
        return a + b;
    }

    @Override
    public Long count(Long a) {
        return (long) Long.bitCount(a);
    }

    @Override
    public Long divide(Long a, Long b) {
        if (b == 0) {
            throw new DivisionByZeroException(a + " can't be divided by zero");
        }
        return a / b;
    }

    @Override
    public Long max(Long a, Long b) {
        return a > b ? a : b;
    }

    @Override
    public Long min(Long a, Long b) {
        return a < b ? a : b;
    }

    @Override
    public Long mod(Long a, Long b) {
        return a % b;
    }

    @Override
    public Long multiply(Long a, Long b) {
        return a * b;
    }

    @Override
    public Long negate(Long a) {
        return -a;
    }

    @Override
    public Long sqrt(Long a) {
        return (long) Math.sqrt(a);
    }

    @Override
    public Long square(Long a) {
        return a * a;
    }

    @Override
    public Long subtract(Long a, Long b) {
        return a - b;
    }

    @Override
    public Long parse(String s) {
        return Long.parseLong(s);
    }
}
