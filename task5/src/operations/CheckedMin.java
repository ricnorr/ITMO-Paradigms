package operations;

public class CheckedMin<T extends Number> extends CheckedArithmetic<T> {

    public CheckedMin(CommonExpression operand1, CommonExpression operand2, Calculation<T> op) {
        super(operand1, operand2, op);
    }

    @Override
    protected T calculateOperation(T operand1, T operand2) {
        return op.min(operand1, operand2);
    }
}
