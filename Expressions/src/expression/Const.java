package expression;

public class Const implements MyExpression {

    private final Number value;

    public Const(Number value) {
        this.value =  value;
    }

    @Override
    public int evaluate(int x) {
        return value.intValue();
    }

    @Override
    public double evaluate(double x) {
        return value.doubleValue();
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
        return value.toString();
    }
}
