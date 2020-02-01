package expression;

import expression.exceptions.TranslateExpression;

public class Main {
    public static void main(String[] args) {
        CommonExpression x = TranslateExpression.parse("x / y / z");
        System.out.println(x.evaluate(1,2,3));
    }

}
