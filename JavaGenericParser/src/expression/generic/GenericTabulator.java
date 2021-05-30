package expression.generic;

import expression.GenericTripleExpression;
import expression.exceptions.UnknownModeException;
import expression.mods.*;
import expression.parser.ExpressionParser;

import java.util.Arrays;
import java.util.Map;

public class GenericTabulator implements Tabulator {
    Map<String, Operation<?>> MODES = Map.of(
            "i", new IntegerOperation(true),
            "u", new IntegerOperation(false),
            "d", new DoubleOperation(),
            "bi", new BigIntegerOperation(),
            "l", new LongOperation(),
            "s", new ShortOperation()
    );

    @Override
    public Object[][][] tabulate(String mode, String expression,
                                 int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        if (!MODES.containsKey(mode)) {
            throw new UnknownModeException("unknown mode: " + mode);
        }
        return findResult(MODES.get(mode), expression, x1, x2, y1, y2, z1, z2);
    }

    private <T> Object[][][] findResult(Operation<T> operation, String expression,
                                        int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        ExpressionParser<T> parser = new ExpressionParser<>(operation);
        GenericTripleExpression<T> exception = parser.parse(expression);
        int x = x2 - x1 + 1;
        int y = y2 - y1 + 1;
        int z = z2 - z1 + 1;
        Object[][][] result = new Object[x][y][z];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    try {
                        result[i][j][k] = exception.evaluate(operation.parse(Integer.toString(x1 + i)),
                                operation.parse(Integer.toString(y1 + j)),
                                operation.parse(Integer.toString(z1 + k)));
                    } catch (ArithmeticException e) {
                        result[i][j][k] = null;
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("GenericTabulator must have 2 arguments, " +
                    "found:" + Arrays.toString(args));
        }
        try {
            Object[][][] result = new GenericTabulator().tabulate(args[0].substring(1), args[1],
                    -2, 2, -2, 2, -2, 2);
            print(result);
        } catch (Exception e) {
            System.out.println("Something wrong:" + e.getMessage());
        }
    }

    private static void print(Object[][][] result) {
        for (Object[][] objects: result) {
            for (Object[] objs : objects) {
                for (Object obj: objs) {
                    System.out.print(obj + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
