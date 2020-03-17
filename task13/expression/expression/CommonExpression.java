package expression;

import expression.TripleExpression;

public interface CommonExpression extends TripleExpression {
    @Override
    int evaluate(int x, int y, int z);
}
