package expression.parser;

import expression.CommonExpression;
import expression.Variable;
import expression.CheckedAdd;
import expression.exceptions.ParsingException;
import expression.operations.*;

import java.util.Map;

public class translateExpression {

    public static CommonExpression parse (final String source) {
        return new CommonExpressionParser(new StringSource(source)).parseElement('\0', 0);
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

        public CommonExpressionParser(StringSource data) {
            super(data);
            nextChar();
        }

        private CommonExpression parseElement(char ban, int lvl) {
            skipWhitespace();
            CommonExpression result = null;
            while (!test(ban) && ch != '\0') {
                result = parseSomething(lvl, result);
            }

            return result;
        }


        private void check(char ban) {
            if (ch == '\0' && ban != '\0') {
                throw new ParsingException("Out of close bracket");
            }
        }

        private CommonExpression parseToken() {
            skipWhitespace();
            if (test('-')) {
                return new Negate(parseToken());
            }
            if (nextIsDigit(ch)) {
                return getConst();
            }
            if (test('(')) {
                return parseElement(')', 0);
            }
            return parseVariable();
        }

        private CommonExpression parseOperation(int lvl, CommonExpression leftOperand) {
            while (ch != '\0' && ch != ')') {
                skipWhitespace();
                if (test('<')) {
                    if (test('<')) {
                        leftOperand = parseShift(leftOperand, lvl, '<');
                        if (lvl != 0) {
                            return leftOperand;
                        }
                    }
                } else if (test('>')) {
                    if (test('>')) {
                        leftOperand = parseShift(leftOperand, lvl, '>');
                        if (lvl != 0) {
                            return leftOperand;
                        }
                    }
                } else if (test('+')) {
                    leftOperand = parseSumSub(leftOperand, lvl, '+');
                    if (lvl >= PRIORITY.get('+')) {
                        return leftOperand;
                    }

                } else if (test('-')) {
                    CommonExpression temp = leftOperand;
                    leftOperand = parseSumSub(leftOperand, lvl, '-');
                    if (temp != null && lvl >= PRIORITY.get('-')) {
                        return leftOperand;
                    }

                } else if (test('*')) {
                    leftOperand = parseMultDev(leftOperand, lvl, '*');
                    if (lvl >= PRIORITY.get('*')) {
                        return leftOperand;
                    }

                } else if (test('/')) {
                    leftOperand = parseMultDev(leftOperand, lvl, '/');
                    if (lvl >= PRIORITY.get('/')) {
                        return leftOperand;
                    }
                }
            }
            return leftOperand;
        }

        private CommonExpression parseSomething(int lvl, CommonExpression leftOperand) {
             if (leftOperand == null) {
                 leftOperand = parseToken();
             }
             return parseOperation(lvl, leftOperand);
        }

        private CommonExpression parseValue(int lvl, CommonExpression leftOperand) {

            while (ch != '\0') {

                skipWhitespace();
                if (nextIsDigit(ch)) {
                    leftOperand = getConst();
                    continue;

                }  else if (test('(')) {
                    leftOperand = parseElement(')', 0);
                    continue;

                } else if (ch == ')') {
                    return leftOperand;

                } else if (test('<')) {
                    if (test('<')) {
                        leftOperand = parseShift(leftOperand, lvl, '<');
                        if (lvl != 0) {
                            return leftOperand;
                        }
                    }

                } else if (test('>')) {
                    if (test('>')) {
                        leftOperand = parseShift(leftOperand, lvl, '>');
                        if (lvl != 0) {
                            return leftOperand;
                        }
                    }

                } else if (test('+')) {
                    leftOperand = parseSumSub(leftOperand, lvl, '+');
                    if (lvl >= PRIORITY.get('+')) {
                        return leftOperand;
                    }

                } else if (test('-')) {
                    CommonExpression temp = leftOperand;
                    leftOperand = parseSumSub(leftOperand, lvl, '-');
                    if (temp != null && lvl >= PRIORITY.get('-')) {
                        return leftOperand;
                    }

                } else if (test('*')) {
                    leftOperand = parseMultDev(leftOperand, lvl, '*');
                    if (lvl >= PRIORITY.get('*')) {
                        return leftOperand;
                    }

                } else if (test('/')) {
                    leftOperand = parseMultDev(leftOperand, lvl, '/');
                    if (lvl >= PRIORITY.get('/')) {
                        return leftOperand;
                    }
                } else if (ch != '\0') {
                    leftOperand = parseVariable();
                }
            }
            return leftOperand;
        }

        private CommonExpression parseSumSub(CommonExpression leftOperand, int lvl, char ch) {
            CommonExpression temp = leftOperand;
            if (leftOperand == null) {
                return new Negate(parseToken());
            }

            if (lvl >= 1) {
                backChar();
                return leftOperand;
            }
            if (ch == '-') {
                return new Subtract(temp, parseSomething(1, null));
            } else {
                return new CheckedAdd(temp, parseSomething(1, null));}
        }

        private CommonExpression parseShift(CommonExpression leftOperand, int lvl, char ch) {
            if (lvl != 0) {
                backChar();
                backChar();
                return leftOperand;
            }
            if (ch == '>') {
                return new BinaryRightSdvig(leftOperand, parseValue(-1, null));
            } else {
                return new BinaryLeftSdvig(leftOperand, parseValue(-1, null));
            }

        }

        private CommonExpression parseMultDev(CommonExpression leftOperand, int lvl, char ch) {
            if (lvl >= 2) {
                backChar();
                return leftOperand;
            }
            if (ch == '*') {
                return new Multiply(leftOperand, parseValue(2, null));
            }  else {
                return new Divide(leftOperand, parseValue(2, null));
            }
        }

        private Variable parseVariable() {
            StringBuilder variableName = new StringBuilder();
            while (!Character.isWhitespace(ch) && ch != '\0'
                    && ch != '\n'&& ch != ')'
                    && ch != '(' && ch != '+' && ch != '-'
                    && ch != '/' && ch!='*') {
                variableName.append(ch);
                nextChar();
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
            skipWhitespace();
            return new Const(Long.parseLong(sb.toString()));
        }


        private void skipWhitespace() {
            while (test(' ') || test('\r') || test('\n') || test('\t')) {
                // skip
            }
        }
    }
}
