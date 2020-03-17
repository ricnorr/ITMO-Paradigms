package expression.exceptions;

import expression.CommonExpression;

public class CheckedAdd extends CheckedArithmetic {

    public CheckedAdd(CommonExpression leftOperand, CommonExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected int calculateOperation(int leftOperand, int rightOperand)  {
        check(leftOperand, rightOperand);
        return leftOperand + rightOperand;
    }

    @Override
    protected String getStringOperation() {
        return "+";
    }

    protected void check(int leftOperand, int rightOperand) throws OverflowException {
        if (leftOperand > 0 && rightOperand > 0 && leftOperand + rightOperand < 0
                || leftOperand < 0 && rightOperand < 0 && leftOperand + rightOperand >= 0) {
            throw new OverflowException("overflow");
        }
    }
}
