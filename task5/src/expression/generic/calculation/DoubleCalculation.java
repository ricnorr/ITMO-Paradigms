package expression.generic.operations;

public class DoubleCalculation implements Calculation<Double> {
    @Override
    public Double cast(Number a) {
        return a.doubleValue();
    }

    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double multiply(Double a, Double b) {
        return a * b;
    }

    @Override
    public Double transform(String s) {
        return Double.parseDouble(s);
    }

    @Override
    public Double max(Double a, Double b) {
        return a > b ? a : b;
    }

    @Override
    public Double min(Double a, Double b) {
        return a < b ? a : b;
    }

    @Override
    public Double count (Double a) {
        return (double)Long.bitCount(Double.doubleToLongBits(a));
    }
    @Override
    public Double divide(Double a, Double b) {
        return a / b;
    }

    @Override
    public Double negate(Double a) {
        return -a;
    }

    @Override
    public Double subtract(Double a, Double b) {
        return a - b;
    }
}
