package expression.exceptions;

public class OverflowException extends CalculatingExpressionException {
    public OverflowException(String message) {
        super(message);
    }
}
