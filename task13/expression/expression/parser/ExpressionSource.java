package expression.parser;

public interface ExpressionSource {
    char nextChar();
    char backChar();
    boolean hasNext();
}
