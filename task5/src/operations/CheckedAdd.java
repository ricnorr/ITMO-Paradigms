package operations;

public class CheckedAdd <T extends Number> extends CheckedArithmetic<T> {

    public CheckedAdd(CommonExpression leftOperand, CommonExpression rightOperand,Calculation<T> op) {
        super(leftOperand, rightOperand, op);
    }

    @Override
    protected T calculateOperation(T leftOperand, T rightOperand)  {
        return op.add(leftOperand, rightOperand);
    }


}
