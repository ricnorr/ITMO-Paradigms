package expression.generic.exceptions;

public class DivisionByZeroException extends CalculatingExpressionException {
    public DivisionByZeroException(String message) {
        super(message);
    }
}
