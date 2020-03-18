package expression.generic.parser;

public interface ExpressionSource {
    char nextChar();
    char backChar();
    boolean hasNext();
    int getPointer();
}
