package exceptions;

public class DivisionByZeroException extends CalculatingExpressionException {
    public DivisionByZeroException(String message) {
        super(message);
    }
}
