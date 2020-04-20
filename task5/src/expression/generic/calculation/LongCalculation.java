package expression.generic.calculation;

import expression.generic.exceptions.DivisionByZeroException;
import expression.generic.exceptions.ParsingException;
import expression.generic.operations.Calculation;

public class LongCalculation implements Calculation<Long> {

    @Override
    public Long add(Long a, Long b) {
        return a + b;
    }

    @Override
    public Long multiply(Long a, Long b) {
        return a * b;
    }

    @Override
    public Long negate(Long a) {
        return -a;
    }

    @Override
    public Long divide(Long a, Long b) {
        if (b == 0) {
            throw new DivisionByZeroException("Long DVZ");
        }
        return a / b;
    }

    @Override
    public Long subtract(Long a, Long b) {
        return a - b;
    }

    @Override
    public Long transform(String s) {
        return Long.parseLong(s);
    }

    @Override
    public Long cast(Number a) {
        return a.longValue();
    }

    @Override
    public Long max(Long a, Long b) {
        return Long.max(a, b);
    }

    @Override
    public Long min(Long a, Long b) {
        return Long.min(a,b);
    }

    @Override
    public Long count(Long a) {
        return (long) Long.bitCount(a);
    }
}
