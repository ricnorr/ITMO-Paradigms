package expression;

import expression.exceptions.ParsingException;
import expression.exceptions.TranslateExpression;

public class Main {
    public static void main(String[] args) throws ParsingException {
        CommonExpression x = TranslateExpression.parse("1024 >> 5 + 3");
        System.out.println(x.evaluate(1,2,3));
    }

}
