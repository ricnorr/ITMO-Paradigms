package expression.exceptions;

public class UnderflowException extends CalculatingExpressionException {
    public UnderflowException(String message) {
        super(message);
    }
}
