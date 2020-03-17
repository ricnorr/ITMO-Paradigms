package operations;

public interface CommonExpression<T extends Number> {
    T evaluate(T x, T y, T z);
}
