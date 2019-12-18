package expression;

public class Subtract extends Arithmetic {

    public Subtract(MyExpression leftOperand, MyExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getStringOperation() {
        return "-";
    }

    @Override
    protected int calculateOperation(int firstOperand, int secondOperand) {
        return firstOperand - secondOperand;
    }

    @Override
    protected double calculateOperation(double firstOperand, double secondOperand) {return firstOperand - secondOperand;}
}
