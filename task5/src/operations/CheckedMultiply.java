package operations;

public class CheckedMultiply<T extends Number> extends CheckedArithmetic<T> {

    public CheckedMultiply(CommonExpression<T> leftOperand, CommonExpression<T> rightOperand, Calculation<T> op) {
        super(leftOperand, rightOperand, op);
    }

    @Override
    protected T calculateOperation(T firstOperand, T secondOperand)  {
        return op.multiply(firstOperand, secondOperand);
    }


}