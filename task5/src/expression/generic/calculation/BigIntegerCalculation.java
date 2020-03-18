package expression.generic.calculation;

import expression.generic.exceptions.DivisionByZeroException;
import expression.generic.operations.Calculation;

import java.math.BigInteger;

public class BigIntegerCalculation implements Calculation<BigInteger> {

    private void checkDVZ(BigInteger b) {
        if (b.intValue() == 0) {
            throw new DivisionByZeroException("BigInt DVZ");
        }
    }

    @Override
    public BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    @Override
    public BigInteger max(BigInteger a, BigInteger b) {
        if (a.compareTo(b) >= 0) {
            return a;
        } else {
            return b;
        }
    }

    @Override
    public BigInteger min(BigInteger a, BigInteger b) {
        if (a.compareTo(b) >= 0) {
            return b;
        } else {
            return a;
        }
    }

    @Override
    public BigInteger count(BigInteger a) {
        return new BigInteger(String.valueOf(a.bitCount()));
    }

    @Override
    public BigInteger cast(Number a) {
        return new BigInteger(String.valueOf(a));
    }

    @Override
    public BigInteger transform(String s) {
        return new BigInteger(s);
    }

    @Override
    public BigInteger multiply(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    @Override
    public BigInteger negate(BigInteger a) {
        return a.multiply(BigInteger.valueOf(-1));
    }

    @Override
    public BigInteger divide(BigInteger a, BigInteger b) {
        checkDVZ(b);
        return a.divide(b);
    }

    @Override
    public BigInteger subtract(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }
}
