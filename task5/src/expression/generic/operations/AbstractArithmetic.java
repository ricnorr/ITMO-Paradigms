package expression.generic.operations;

public abstract class AbstractArithmetic<T extends Number> implements CommonExpression<T> {

    private CommonExpression <T> operand1;
    private CommonExpression <T> operand2;
    protected Calculation<T> op;

    public AbstractArithmetic(CommonExpression operand1, CommonExpression operand2, Calculation<T> op) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.op = op;
    }

    protected abstract T calculateOperation(T operand1, T operand2);

    @Override
    public T evaluate(T x, T y, T z) {
        return calculateOperation(operand1.evaluate(x, y, z), operand2.evaluate(x, y, z));
    }

    @Override
    public boolean equals(Object x) {
        if (x != null && this.getClass() == x.getClass()) {
            AbstractArithmetic temp = (AbstractArithmetic) x;
            return operand1.equals(temp.operand1) && operand2.equals(temp.operand2);
        }
        return false;
    }

}
