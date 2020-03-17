package expression.generic;

import exceptions.CalculatingExpressionException;
import exceptions.ParsingException;
import expression.generic.Tabulator;
import operations.*;
import parser.CommonExpressionParser;
import parser.StringSource;
import java.math.BigInteger;


public class GenericTabulator implements Tabulator {

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
       return tabulateRealization(mode, expression, x1, x2, y1, y2, z1, z2);
    }

    private <T extends Number> Object[][][] tabulateRealization(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
        Object[][][] ans = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        CommonExpressionParser<T> parser;
        if (mode.equals("i")) {
            parser = new CommonExpressionParser<>(expression , new IntCalculation());
        } else if (mode.equals("d")) {
            parser =  new CommonExpressionParser<>(expression, new DoubleCalculation());
        } else if (mode.equals("bi")) {
            parser = new CommonExpressionParser<>(expression, new BigIntegerCalculation());
        } else if (mode.equals("l")) {
            parser = new CommonExpressionParser<>(expression, new LongCalculation());
        } else if (mode.equals("s")) {
            parser =  new CommonExpressionParser<>(expression, new ShortCalculation());
        } else {
            throw new IllegalStateException("illegal mode");
        }
        CommonExpression<T> exp = parser.parse();
        int deltaX = x2 - x1;
        int deltaY = y2 - y1;

        int deltaZ = z2 - z1;
        for (int i = 0; i <= deltaX; i++) {
            for (int j = 0; j <= deltaY; j++) {
                for (int k = 0; k <= deltaZ; k++) {
                    try {
                        Calculation<T> calc = parser.getCalc();
                        ans[i][j][k] = exp.evaluate(calc.cast(x1 + i), calc.cast(y1 + j), calc.cast(z1 + k));
                    } catch (CalculatingExpressionException e) {
                        ans[i][j][k] = null;
                    }
                }
            }
        }
        return ans;
    }
}
