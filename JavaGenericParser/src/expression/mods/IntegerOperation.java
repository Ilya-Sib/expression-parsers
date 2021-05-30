package expression.mods;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;
import expression.exceptions.SqrtArgumentException;

public class IntegerOperation implements Operation<Integer> {
    private final boolean needCheck;
    public IntegerOperation(boolean needCheck) {
        this.needCheck = needCheck;
    }

    @Override
    public Integer abs(Integer a) {
        if (needCheck) {
            absOverflowChecker(a);
        }
        return a >= 0 ? a : -a;
    }

    @Override
    public Integer add(Integer a, Integer b) {
        if (needCheck) {
            addOverflowChecker(a, b);
        }
        return a + b;
    }

    @Override
    public Integer count(Integer a) {
        return Integer.bitCount(a);
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        if (needCheck) {
            divideOverflowChecker(a, b);
        }
        if (b == 0) {
            throw new DivisionByZeroException(a + " can't be divided by zero");
        }
        return a / b;
    }

    @Override
    public Integer max(Integer a, Integer b) {
        return a > b ? a : b;
    }

    @Override
    public Integer min(Integer a, Integer b) {
        return a < b ? a : b;
    }

    @Override
    public Integer mod(Integer a, Integer b) {
        if (b == 0) {
            throw new DivisionByZeroException(a + " can't be mod by zero");
        }
        return a % b;
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        if (needCheck) {
            multiplyOverflowChecker(a, b);
        }
        return a * b;
    }

    @Override
    public Integer negate(Integer a) {
        if (needCheck) {
            negateOverflowChecker(a);
        }
        return -a;
    }

    @Override
    public Integer sqrt(Integer a) {
        if (a < 0) {
            throw new SqrtArgumentException("sqrt must have not negative argument: " + a);
        }
        return (int) Math.sqrt(a);
    }

    @Override
    public Integer square(Integer a) {
        if (needCheck) {
            squareOverflowChecker(a);
        }
        return a * a;
    }

    @Override
    public Integer subtract(Integer a, Integer b) {
        if (needCheck) {
            subtractOverflowChecker(a, b);
        }
        return a - b;
    }

    @Override
    public Integer parse(String s) {
        return Integer.parseInt(s);
    }

    private void absOverflowChecker(Integer a) {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException("abs " + a + " need more than 32 bits");
        }
    }

    private void addOverflowChecker(Integer a, Integer b) {
        if (b > 0 && a > Integer.MAX_VALUE - b
                || b < 0 && a < Integer.MIN_VALUE - b) {
            throw new OverflowException(a + " + " + b + " need more than 32 bits");
        }
    }

    private void divideOverflowChecker(Integer a, Integer b) {
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new OverflowException(a + " / " + b + " need more than 32 bits");
        }
    }

    private void multiplyOverflowChecker(Integer a, Integer b) {
        int max = Integer.signum(a) == Integer.signum(b) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        if ((a == -1 && b == Integer.MIN_VALUE)
                || (a != -1 && a != 0 && ((b > 0 && b > max / a)
                || (b < 0 && b < max / a )))) {
            throw new OverflowException(a + " * " + b + " need more than 32 bits");
        }
    }

    private void negateOverflowChecker(Integer a) {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException("-" + a + " need more than 32 bits");
        }
    }

    private void squareOverflowChecker(Integer a) {
        if (a > Math.sqrt(Integer.MAX_VALUE) || a < -Math.sqrt(Integer.MAX_VALUE)) {
            throw new OverflowException("square " + a + " need more than 32 bits");
        }
    }

    private void subtractOverflowChecker(Integer a, Integer b) {
        if (b > 0 && a < Integer.MIN_VALUE + b
                || b < 0 && a > Integer.MAX_VALUE + b) {
            throw new OverflowException(a + " - " + b + " need more than 32 bits");
        }
    }
}
