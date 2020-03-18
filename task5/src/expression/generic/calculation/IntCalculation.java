package expression.generic.operations;

import expression.generic.exceptions.DivisionByZeroException;
import expression.generic.exceptions.OverflowException;
import expression.generic.exceptions.UnderflowException;

public class IntCalculation implements Calculation<Integer> {

    protected void checkSubtractOverflow(Integer leftOperand, Integer rightOperand) throws OverflowException {
        if (leftOperand >= 0 && rightOperand < 0 && leftOperand - rightOperand < 0) {
            throw new OverflowException("overflow subtract");
        }
        if (leftOperand < 0 && rightOperand > 0 && leftOperand - rightOperand >= 0) {
            throw new UnderflowException("underflow subtract");
        }
    }

    private void checkConstOverflow(Number x) {
        if (x.longValue() > Integer.MAX_VALUE) {
            throw new OverflowException("Integer const overflow: " + x);
        }
        if (x.longValue() < Integer.MIN_VALUE) {
            throw new OverflowException("Integer const underflow " + x);
        }
    }

    private void checkMultOverflow(Integer firstOperand, Integer secondOperand) {
        if (firstOperand > 0 && secondOperand > 0 && firstOperand > Integer.MAX_VALUE / secondOperand
                || firstOperand < 0 && secondOperand > 0 && firstOperand < Integer.MIN_VALUE / secondOperand
                || firstOperand < 0 && secondOperand < 0 && firstOperand < Integer.MAX_VALUE / secondOperand
                || firstOperand > 0 && secondOperand < 0 && secondOperand < Integer.MIN_VALUE / firstOperand) {
            throw new OverflowException("overflow multiply");
        }
    }

    protected void checkSumOverflow(int leftOperand, int rightOperand) {
        if (leftOperand > 0 && rightOperand > 0 && leftOperand + rightOperand < 0
                || leftOperand < 0 && rightOperand < 0 && leftOperand + rightOperand >= 0) {
            throw new OverflowException("overflow sum");
        }
    }

    private void checkNegateOverflow(Integer x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("overflow negate");
        }
    }

    private void checkDVZ(Integer a, Integer b) {
        if (b == 0) {
            throw new DivisionByZeroException("DVZ");
        }
    }

    @Override
    public Integer transform(String s) {
        checkConstOverflow(Long.parseLong(s));
        return Integer.parseInt(s);
    }

    @Override
    public Integer min(Integer a, Integer b) {
        return a < b ? a : b;
    }

    @Override
    public Integer max(Integer a, Integer b) {
        return a > b ? a : b;
    }

    @Override
    public Integer cast(Number a) {
        return a.intValue();
    }

    @Override
    public Integer add(Integer a, Integer b) {
        checkSumOverflow(a, b);
        return a + b;
    }

    @Override
    public Integer count(Integer a) {
        return Integer.bitCount(a);
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        checkMultOverflow(a, b);
        return a * b;
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        checkDVZ(a, b);
        return a/b;
    }

    @Override
    public Integer negate(Integer a) {
        checkNegateOverflow(a);
        return -a;
    }

    @Override
    public Integer subtract(Integer a, Integer b) {
        checkSubtractOverflow(a, b);
        return a - b;
    }

}
