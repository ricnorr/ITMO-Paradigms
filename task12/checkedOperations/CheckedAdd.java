package expression.checkedOperations;

import expression.CheckedArithmetic;
import expression.CommonExpression;
import expression.exceptions.OverflowException;

public class CheckedAdd extends CheckedArithmetic {

    public CheckedAdd(CommonExpression leftOperand, CommonExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected int calculateOperation(int leftOperand, int rightOperand)  {
        check(leftOperand, rightOperand);
        return leftOperand + rightOperand;
    }
    //@Override
    //protected double calculateOperation(double leftOperand, double rightOperand) {return leftOperand + rightOperand;}
    @Override
    protected String getStringOperation() {
        return "+";
    }

    protected void check(int leftOperand, int rightOperand) throws OverflowException {
        if (leftOperand > Integer.MAX_VALUE - rightOperand && leftOperand < Integer.MIN_VALUE - rightOperand) {
            throw new OverflowException("overflow");
        }
    }
}
