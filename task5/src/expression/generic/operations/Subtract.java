package operations;


public class CheckedSubtract<T extends Number> extends CheckedArithmetic<T> {
    public CheckedSubtract(CommonExpression leftOperand, CommonExpression rightOperand, Calculation<T> op) {
        super(leftOperand, rightOperand, op);
    }

    @Override
    protected T calculateOperation(T leftOperand, T rightOperand)  {
        return op.subtract(leftOperand, rightOperand);
    }

}
