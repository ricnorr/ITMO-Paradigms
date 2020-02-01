package expression.exceptions;

import expression.*;
import expression.exceptions.*;
import expression.operations.*;
import expression.parser.BaseParser;
import expression.parser.StringSource;

import java.util.Map;
import java.util.Set;

public class TranslateExpression {

    public static CommonExpression parse (final String source) {
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

        private CommonExpression parseSource(char ban, int lvl) {
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
            if (test(')') || test('\0')) {
                throw new ParsingException("Illegal expression");
            }
        }

        private CommonExpression parseToken() {
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

        private CommonExpression parseOperation(int lvl, CommonExpression leftOperand) {
            skipWhitespace();
            while (ch != '\0' && ch != ')') {
                if (test('<') || test('>')) {
                    if (ch == prevCh && (test('<') || test('<'))) {
                        if (lvl != 0) {
                            backChar();
                            backChar();
                            return leftOperand;
                        }
                        leftOperand = parseShift(leftOperand, lvl, prevCh);
                    } else {
                        throw new ParsingException("Expected '>'");
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
                    throw new ParsingException("Illegal symbol " + ch);
                }
            }
            return leftOperand;
        }

        private CommonExpression parseOperandExpression(int lvl, CommonExpression leftOperand) {
             if (leftOperand == null) {
                 leftOperand = parseToken();
             }
             return parseOperation(lvl, leftOperand);
        }


        private CommonExpression parseSumSub(CommonExpression leftOperand, int lvl, char ch) {
            CommonExpression temp = leftOperand;
            if (leftOperand == null) {
                return new CheckedNegate(parseToken());
            }
            if (ch == '-') {
                return new CheckedSubtract(temp, parseOperandExpression(1, null));
            } else {
                return new CheckedAdd(temp, parseOperandExpression(1, null));}
        }

        private CommonExpression parseShift(CommonExpression leftOperand, int lvl, char ch) {
            if (ch == '>') {
                return new BinaryRightSdvig(leftOperand, parseOperandExpression(-1, null));
            } else {
                return new BinaryLeftSdvig(leftOperand, parseOperandExpression(-1, null));
            }

        }

        private CommonExpression parseMultDev(CommonExpression leftOperand, int lvl, char ch) {
            if (ch == '*') {
                return new CheckedMultiply(leftOperand, parseOperandExpression(2, null));
            }  else {
                return new CheckedDivide(leftOperand, parseOperandExpression(2, null));
            }
        }

        private Variable parseVariable() {
            StringBuilder variableName = new StringBuilder();
            while (VARIABLE_ALPHABET.contains(ch)) {
                variableName.append(ch);
                nextChar();
            }
            if (variableName.length() == 0 || !AFTER_VARIABLE.contains(ch)) {
                throw new ParsingException("Illegal variable name"); //no variable
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
            if (x.longValue() > Integer.MAX_VALUE || x.longValue() < Integer.MIN_VALUE) {
                throw new OverflowException("Const overflow");
            }
        }

        private void skipWhitespace() {
            while (test(' ') || test('\r') || test('\n') || test('\t')) {
                // skip
            }
        }
    }
}
