package expression.generic.operations;

public class Add<T extends Number> extends AbstractArithmetic<T> {

    public Add(CommonExpression leftOperand, CommonExpression rightOperand, Calculation<T> op) {
        super(leftOperand, rightOperand, op);
    }

    @Override
    protected T calculateOperation(T leftOperand, T rightOperand)  {
        return op.add(leftOperand, rightOperand);
    }


}
