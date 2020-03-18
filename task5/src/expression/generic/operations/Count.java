package operations;

public class Count<T extends Number> implements CommonExpression<T> {

    private final CommonExpression<T> data;
    private final Calculation<T> op;

    public Count(CommonExpression operand1, Calculation<T> op) {
        this.data = operand1;
        this.op = op;
    }

    @Override
    public T evaluate(T x, T y, T z) {
       return op.count(data.evaluate(x, y, z));
    }
}
