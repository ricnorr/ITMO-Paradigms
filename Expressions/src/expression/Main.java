package expression;

public class Main {
    public static void main(String[] args) {
        System.out.println(new Multiply(new Variable("x"), new Const(-1_000_000_000)).equals(new Multiply(new Variable("x"), new Const(-1_000_000_000))));
    }
}
