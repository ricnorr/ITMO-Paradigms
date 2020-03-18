package expression.generic.operations;

public class Variable<T extends Number> implements CommonExpression<T> {

    private String name;
    private Calculation<T> op;

    public Variable(String name, Calculation<T> op) {
        this.op = op;
        this.name = name;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        switch (name) {
            case "x":
                return op.cast(x);
            case "y":
                return op.cast(y);
            default:
                return op.cast(z);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object x) {
        return (x instanceof Variable && this.name.equals(((Variable) x).name));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
