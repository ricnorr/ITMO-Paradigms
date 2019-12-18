package expression;

public class Main {
    public static void main(String[] args) {
        TripleExpression x = translateExpression.parse("" +
                "-2049603535 - - -65884846 >> 585622315 - - " +
                "-38471179 * - - z + - (-1385008614 >> 2011718220) *" +
                " (x << - - 1937840685 >> z)"
        );
        //System.out.println(INTEGER.M)//умеет в +, умеет в минус
        //TripleExpression x = translateExpression.parse("x*y + (z-1) / 10");
        //TripleExpression x = translateExpression.parse("5 + (2 * 3 * 4)");
        //TripleExpression x = translateExpression.parse("1 + 2 + 3");
        //System.out.println(x.toString());
        //System.out.println(1 << );
        System.out.println(x.evaluate(1, 1, 1));
    }
}
