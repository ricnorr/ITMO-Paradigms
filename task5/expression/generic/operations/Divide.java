package expression.generic.operations;


public class Divide<T extends  Number> extends AbstractArithmetic<T> {

    public Divide(CommonExpression<T> leftOperand, CommonExpression<T> rightOperand, Calculation<T> op) {
        super(leftOperand, rightOperand, op);
    }

    @Override
    protected T calculateOperation(T leftOperand, T rightOperand) {
        return op.divide(leftOperand, rightOperand);
    }
}
