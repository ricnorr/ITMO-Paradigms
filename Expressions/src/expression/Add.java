package expression;

public class Add extends Arithmetic {

    public Add(MyExpression leftOperand, MyExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected int calculateOperation(int leftOperand, int rightOperand) {
        return leftOperand + rightOperand;
    }
    @Override
    protected double calculateOperation(double leftOperand, double rightOperand) {return leftOperand + rightOperand;}
    @Override
    protected String getStringOperation() {
        return "+";
    }

}
