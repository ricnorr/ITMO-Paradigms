package expression.generic.parser;

import expression.generic.operations.CommonExpression;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Parser {
    CommonExpression parse(String expression);
}
