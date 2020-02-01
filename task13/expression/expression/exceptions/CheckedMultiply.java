package expression.exceptions;

import expression.CommonExpression;

public class CheckedMultiply extends CheckedArithmetic {

    public CheckedMultiply(CommonExpression leftOperand, CommonExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    protected void check(int firstOperand, int secondOperand) throws OverflowException {
        if (firstOperand > 0 && secondOperand > 0 && firstOperand > Integer.MAX_VALUE / secondOperand
                || firstOperand < 0 && secondOperand > 0 && firstOperand < Integer.MIN_VALUE / secondOperand
                || firstOperand < 0 && secondOperand < 0 &&  firstOperand < Integer.MAX_VALUE / secondOperand
                || firstOperand > 0 && secondOperand < 0 &&  secondOperand < Integer.MIN_VALUE / firstOperand) {
            throw new OverflowException("overflow");
        }
    }

    @Override
    protected int calculateOperation(int firstOperand, int secondOperand)  {
        check(firstOperand, secondOperand);
        return firstOperand * secondOperand;
    }

    @Override
    protected String getStringOperation() {
        return "*";
    }

}