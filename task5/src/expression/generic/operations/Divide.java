package operations;


public class CheckedDivide<T extends  Number> extends CheckedArithmetic<T> {

    public CheckedDivide(CommonExpression leftOperand, CommonExpression rightOperand, Calculation<T> op) {
        super(leftOperand, rightOperand, op);
    }

    @Override
    protected T calculateOperation(T leftOperand, T rightOperand) {
        return op.divide(leftOperand, rightOperand);
    }
}
