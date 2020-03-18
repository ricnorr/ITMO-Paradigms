package expression.generic.operations;

public class Max<T extends Number> extends AbstractArithmetic<T> {

    public Max(CommonExpression operand1, CommonExpression operand2, Calculation<T> op) {
        super(operand1, operand2, op);
    }

    @Override
    protected T calculateOperation(T operand1, T operand2) {
        return op.max(operand1, operand2);
    }


}
