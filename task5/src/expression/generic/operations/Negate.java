package expression.generic.operations;

public class Negate<T extends Number> implements CommonExpression<T> {

    private CommonExpression<T> operand;
    private Calculation<T> op;

    public Negate(CommonExpression<T> operand, Calculation<T> op) {
        this.op = op;
        this.operand = operand;
    }

    public T evaluate(T x, T y, T z) {
        return op.negate(operand.evaluate(x,y,z));
    }
}
