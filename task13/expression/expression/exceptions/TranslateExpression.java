package expression.exceptions;

import expression.*;
import expression.operations.*;
import expression.parser.BaseParser;
import expression.parser.StringSource;

import java.util.Map;
import java.util.Set;

public class TranslateExpression {

    public static CommonExpression parse (final String source) throws ParsingException {
        return new CommonExpressionParser(new StringSource(source)).parseSource('\0', 0);
    }

    private static class CommonExpressionParser extends BaseParser {

        private final Map<Character, Integer> PRIORITY = Map.of(
                '*', 2,
                '/', 2,
                '+', 1,
                '-', 1,
                '<', -1,
                '>', -1
        );
        private final Set<Character> VARIABLE_ALPHABET = Set.of('x', 'y', 'z');

        private final Set<Character> AFTER_VARIABLE = Set.of('*','-', ')', ' ', '+', '/', '\0', '\t', '\r');

        public CommonExpressionParser(StringSource data) {
            super(data);
            nextChar();
        }

        private CommonExpression parseSource(char ban, int lvl) throws ParsingException {
            skipWhitespace();
            CommonExpression result = null;
            while (!test(ban)) {
                checkIllegalBrackets();
                result = parseOperandExpression(lvl, result);
            }
            if (result == null) {
                throw new ParsingException("Empty expression");
            }
            return result;
        }

        private void checkIllegalBrackets() throws ParsingException {
            if (test(')')) {
                throw new ParsingException("Bracket closed at " + getPointer() + " but not opened before");
            }
            if (test('\0')) {
                throw new ParsingException("Waited closing bracket, bracket not closed at position " + getPointer());
            }

        }

        private CommonExpression parseToken() throws ParsingException {
            skipWhitespace();
            if (test('-')) {
                if (nextIsDigit(ch)) {
                    backChar();
                    return getConst();
                }
                return new CheckedNegate(parseToken());
            }
            if (nextIsDigit(ch)) {
                return getConst();
            }
            if (test('(')) {
                return parseSource(')', 0);
            }
            return parseVariable();
        }


        private CommonExpression parseOperation(int lvl, CommonExpression leftOperand) throws ParsingException {
            skipWhitespace();
            while (ch != '\0' && ch != ')') {
                if (test('<') || test('>')) {
                    if (ch == prevCh && (test('>') || test('<'))) {
                        if (lvl != 0) {
                            backChar();
                            backChar();
                            return leftOperand;
                        }
                        leftOperand = parseShift(leftOperand, lvl, prevCh);
                    } else {
                        throw new ParsingException("Expected double '>' or '<'");
                    }
                } else if (test('+') || test('-')) {
                    if (lvl >= PRIORITY.get('+') && leftOperand != null) { // != null checking:for negate
                        backChar();
                        return leftOperand;
                    }
                    leftOperand = parseSumSub(leftOperand, lvl, prevCh);
                } else if (test('*') || test('/')) {
                    if (lvl >= PRIORITY.get('*')) {
                        backChar();
                        return leftOperand;
                    }
                    leftOperand = parseMultDev(leftOperand, lvl, prevCh);
                } else {
                    throw new ParsingException("Illegal symbol " + "\'" + ch+ "\'" + " at position " + getPointer());
                }
            }
            return leftOperand;
        }

        private CommonExpression parseOperandExpression(int lvl, CommonExpression leftOperand) throws ParsingException {
             if (leftOperand == null) {
                 leftOperand = parseToken();
             }
             return parseOperation(lvl, leftOperand);
        }


        private CommonExpression parseSumSub(CommonExpression leftOperand, int lvl, char ch) throws ParsingException {
            CommonExpression temp = leftOperand;
            if (ch == '-') {
                return new CheckedSubtract(temp, parseOperandExpression(1, null));
            } else {
                return new CheckedAdd(temp, parseOperandExpression(1, null));}
        }

        private CommonExpression parseShift(CommonExpression leftOperand, int lvl, char ch) throws ParsingException {
            if (ch == '>') {
                return new BinaryRightShift(leftOperand, parseOperandExpression(-1, null));
            } else {
                return new BinaryLeftShift(leftOperand, parseOperandExpression(-1, null));
            }

        }

        private CommonExpression parseMultDev(CommonExpression leftOperand, int lvl, char ch) throws ParsingException {
            if (ch == '*') {
                return new CheckedMultiply(leftOperand, parseOperandExpression(2, null));
            }  else {
                return new CheckedDivide(leftOperand, parseOperandExpression(2, null));
            }
        }

        private Variable parseVariable() throws ParsingException {
            StringBuilder variableName = new StringBuilder();
            while (VARIABLE_ALPHABET.contains(ch)) {
                variableName.append(ch);
                nextChar();
            }
            if (variableName.length() == 0) {
                throw new ParsingException("Waited argument, no legal argument beginning at position " + getPointer()); //no variable
            }
            if (!AFTER_VARIABLE.contains(ch)) {
                throw new ParsingException("Illegal symbol in variable name " + "\'" + ch + "\' " + "at position " + getPointer() );
            }
            return new Variable(variableName.toString());
        }

        private void copyDigits(final StringBuilder sb) {
            if (ch == '-') {
                sb.append(ch);
                nextChar();
            }
            while (between('0', '9')) {
                sb.append(ch);
                nextChar();
            }
        }

        private void copyInteger(StringBuilder sb) {
            skipWhitespace();
            if (test('-')) {
                sb.append('-');
            }
            if (between('0', '9')) {
                copyDigits(sb);
                skipWhitespace();
            } else {
                throw new IllegalStateException();
            }
        }

        private Const getConst() {
            StringBuilder sb = new StringBuilder();
            copyInteger(sb);
            checkConstOverflow(Long.parseLong(sb.toString()));
            return new Const(Long.parseLong(sb.toString()));
        }

        private void checkConstOverflow(Number x) {
            if (x.longValue() > Integer.MAX_VALUE) {
                throw new OverflowException("Integer const overflow: " + x);
            }
            if (x.longValue() < Integer.MIN_VALUE) {
                throw new OverflowException("Integer const underflow " + x);
            }
        }

        private void skipWhitespace() {
            while (test(' ') || test('\r') || test('\n') || test('\t')) {
                // skip
            }
        }
    }
}
