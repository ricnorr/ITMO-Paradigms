package expression.parser;

import expression.CommonExpression;
import expression.exceptions.Parser;
import expression.exceptions.ParsingException;
import expression.exceptions.TranslateExpression;

public class ExpressionParser implements Parser {
    public CommonExpression parse(String source) throws ParsingException {
        return TranslateExpression.parse(source);
    }
}
