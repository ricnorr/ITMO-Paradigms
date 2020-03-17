package operations;

import expression.generic.GenericTabulator;

public class Main {
    public static Calculation<Integer> hello = new IntCalculation();
    public static void main(String[] args) {
        GenericTabulator x = new GenericTabulator();
        try {
            Object[][][] t = x.tabulate("bi", "10 + 4 / 2 - 7", -5, 13, -5, 16, -5, 16);
        } catch(Exception e) {
            System.out.println("upalo");
        }
    }
}
