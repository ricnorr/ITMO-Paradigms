package operations;

public class Const<T extends Number> implements CommonExpression<T> {

    private final T value;

    public Const(T value) {
        this.value =  value;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }

    @Override
    public boolean equals(Object x) {
        if (x != null && this.getClass() == x.getClass() && this.value.equals(((Const) x).value)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
