package expression.exceptions;

import expression.CommonExpression;

public class CheckedDivide extends CheckedArithmetic {

    public CheckedDivide(CommonExpression leftOperand, CommonExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected int calculateOperation(int leftOperand, int rightOperand) {
        check(leftOperand, rightOperand);
        return leftOperand / rightOperand;
    }

    protected void check(int leftOperand, int rightOperand) {
        if (rightOperand == 0) {
            throw new DivisionByZeroException("division by zero");
        }
        if (leftOperand == Integer.MIN_VALUE && rightOperand == -1) {
            throw new OverflowException("Overflow in division");
        }
    }

    @Override
    protected String getStringOperation() {
        return "/";
    }

}
