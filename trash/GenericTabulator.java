package expression.generic;

import expression.generic.calculation.*;
import expression.generic.exceptions.CalculatingExpressionException;
import expression.generic.exceptions.IllegalParsingModeException;
import expression.generic.exceptions.ParsingException;
import expression.generic.operations.*;
import expression.generic.parser.CommonExpressionParser;


public class GenericTabulator implements Tabulator {

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
       return tabulateRealization(chooseCalc(mode), expression, x1, x2, y1, y2, z1, z2);
    }
    private Calculation<? extends Number> chooseCalc(String mode) {
        switch (mode) {
            case ("i"):
                return new IntCalculation();
            case ("d"):
                 return new DoubleCalculation();
            case ("bi"):
                return new BigIntegerCalculation();
            default:
                throw new IllegalParsingModeException("Illegal parsing mode chosen");
        }
    }

    private <T extends Number> Object[][][] tabulateRealization(Calculation<T> calc, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
        Object[][][] ans = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        CommonExpressionParser<T> parser = new CommonExpressionParser<>(expression, calc);
        CommonExpression<T> exp = parser.parse();
        int deltaX = x2 - x1;
        int deltaY = y2 - y1;
        int deltaZ = z2 - z1;
        for (int i = 0; i <= deltaX; i++) {
            for (int j = 0; j <= deltaY; j++) {
                for (int k = 0; k <= deltaZ; k++) {
                    try {
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
