package expression.generic.operations;

public class Multiply<T extends Number> extends AbstractArithmetic<T> {

    public Multiply(CommonExpression<T> leftOperand, CommonExpression<T> rightOperand, Calculation<T> op) {
        super(leftOperand, rightOperand, op);
    }

    @Override
    protected T calculateOperation(T firstOperand, T secondOperand)  {
        return op.multiply(firstOperand, secondOperand);
    }


}