package expression.exceptions;

import expression.CommonExpression;

public class ExpressionParser implements Parser {
    public CommonExpression parse(String source) {
        return TranslateExpression.parse(source);
    }
}
