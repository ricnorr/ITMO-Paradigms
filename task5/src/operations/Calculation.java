package operations;

import exceptions.ParsingException;

public interface Calculation<T extends Number> {
    T add(T a, T b);
    T multiply(T a, T b);
    T negate(T a);
    T divide(T a, T b);
    T subtract(T a, T b);
    T transform(String s) throws ParsingException;
    T cast(Number a);
    T max(T a, T b);
    T min(T a, T b);
    T count(T a);
}
