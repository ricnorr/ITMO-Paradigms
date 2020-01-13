package expression;

import expression.exceptions.OverflowException;
import expression.operations.Const;
import expression.parser.BaseParser;
import expression.parser.StringSource;

import expression.parser.translateExpression;
public class Main {
    public static void main(String[] args) {
        CommonExpression x = translateExpression.parse("-5 + 16 * 2 * 3");
        System.out.println(x);
    }
}
