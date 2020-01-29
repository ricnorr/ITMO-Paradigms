package expression;

import expression.parser.translateExpression;

public class ExpressionParser implements Parser {
    public CommonExpression parse(String source) {
        return translateExpression.parse(source);
    }
}
