package expression.exceptions;

import expression.CommonExpression;
import expression.TripleExpression;
import expression.exceptions.OverflowException;

public class CheckedNegate implements CommonExpression {

    private TripleExpression operand;

    public CheckedNegate(TripleExpression operand) {
        this.operand = operand;
    }

    public int evaluate(int x, int y, int z) {
        int data = operand.evaluate(x,y,z);
        check(data);
        return -data;
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
