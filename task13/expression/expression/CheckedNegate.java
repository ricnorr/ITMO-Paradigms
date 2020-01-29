package expression;

import expression.exceptions.OverflowException;

public class CheckedNegate implements CommonExpression {

    private TripleExpression operand;

    public CheckedNegate(TripleExpression operand) {
        this.operand = operand;
    }

    public int evaluate(int x, int y, int z) {
        return -1 * operand.evaluate(x,y,z);
    }

    public String toString() {
        return "(-" + operand.toString() + ")";
    }

    public void check(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("overflow negate");
        }
    }
}
