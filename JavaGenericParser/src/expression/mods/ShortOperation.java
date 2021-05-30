package expression.mods;

import expression.exceptions.DivisionByZeroException;

public class ShortOperation implements Operation<Short> {
    @Override
    public Short abs(Short a) {
        return (short) (a >= 0 ? a : -a);
    }

    @Override
    public Short add(Short a, Short b) {
        return (short) (a + b);
    }

    @Override
    public Short count(Short a) {
        return (short) Integer.bitCount(Short.toUnsignedInt(a));
    }

    @Override
    public Short divide(Short a, Short b) {
        if (b == 0) {
            throw new DivisionByZeroException(a + " can't be divided by zero");
        }
        return (short) (a / b);
    }

    @Override
    public Short max(Short a, Short b) {
        return a > b ? a : b;
    }

    @Override
    public Short min(Short a, Short b) {
        return a < b ? a : b;
    }

    @Override
    public Short mod(Short a, Short b) {
        return (short) (a % b);
    }

    @Override
    public Short multiply(Short a, Short b) {
        return (short) (a * b);
    }

    @Override
    public Short negate(Short a) {
        return (short) -a;
    }

    @Override
    public Short sqrt(Short a) {
        return (short) Math.sqrt(a);
    }

    @Override
    public Short square(Short a) {
        return (short) (a * a);
    }

    @Override
    public Short subtract(Short a, Short b) {
        return (short) (a - b);
    }

    @Override
    public Short parse(String s) {
        return (short) Integer.parseInt(s);
    }
}
