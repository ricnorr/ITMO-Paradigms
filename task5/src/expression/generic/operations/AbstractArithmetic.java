package expression.generic.operations;

public abstract class AbstractArithmetic<T extends Number> implements CommonExpression<T> {

    private CommonExpression <T> operand1;
    private CommonExpression <T> operand2;
    protected Calculation<T> op;

    public AbstractArithmetic(CommonExpression<T> operand1, CommonExpression<T> operand2, Calculation<T> op) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.op = op;
    }

    protected abstract T calculateOperation(T operand1, T operand2);

    @Override
    public T evaluate(T x, T y, T z) {
        return calculateOperation(operand1.evaluate(x, y, z), operand2.evaluate(x, y, z));
    }


}
