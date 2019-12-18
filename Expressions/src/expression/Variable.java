package expression;

public class Variable implements MyExpression {

    private String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    public double evaluate(double x) {
        return x;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object x) {
        return x != null && this.getClass() == x.getClass() && this.name.equals(((Variable) x).name);
    }

    public int hashCode() {
        return name.hashCode();
    }

}
