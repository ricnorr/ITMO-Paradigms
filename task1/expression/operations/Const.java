package expression.operations;


import expression.CommonExpression;
import expression.exceptions.OverflowException;

public class Const implements CommonExpression {

    private final Number value;
    //private final boolean isDouble;

    public Const(Number value) {
        this.value =  value;
        //isDouble = false;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        check(value);
        return value.intValue();
    }

    private void check(Number x) {
        if (x.longValue() > Integer.MAX_VALUE || x.longValue() < Integer.MIN_VALUE) {
            throw new OverflowException("Integer const overflow: " + x);
        }
    }

    public boolean equals(Object x) {
        if (x != null && this.getClass() == x.getClass() && this.value.equals(((Const) x).value)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        //if (isDouble) {
        return value.toString();
        //} else {
        //return Integer.toString((int) value);
        //}
    }
}
