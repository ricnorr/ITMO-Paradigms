package expression.operations;

import expression.CommonExpression;

public class UnoMinus implements CommonExpression {
    private TripleExpression operand;

    public UnoMinus(TripleExpression operand) {
        this.operand = operand;
    }

    public int evaluate(int x, int y, int z) {
        return -1 * operand.evaluate(x,y,z);
    }

    public String toString() {
        return "(-" + operand.toString() + ")";
    }
}
