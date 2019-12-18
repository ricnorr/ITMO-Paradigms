package expression;

import java.util.Map;

public class translateExpression {

    public static CommonExpression parse (final String source) {return new CommonExpressionParser(new StringSource(source)).parseElement('\0', 0);}

    private static class CommonExpressionParser extends BaseParser{

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
                result = parseValue(lvl, result);
            }

            return result;
        }

        private CommonExpression isLegalOperand(CommonExpression out) {
            if (out == null) {
                throw new ExpressionException("Illegal expression");
            }
            return out;
        }

        private CommonExpression nextToken() { //deadCode probably
            skipWhitespace();
            if (nextIsDigit(ch)) {
                return getConst();
            }
            if (test('(')) {
                return parseElement(')', 0);
            } else if (ch != '\0') {
                return parseVariable();
            }
            return null;
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

        private boolean leaveLevel(int curLvl, int nextLvl, CommonExpression leftOperand) {
            return curLvl >= nextLvl && leftOperand != null;
        }



        private CommonExpression parseSumSub(CommonExpression leftOperand, int lvl, char ch) {
            CommonExpression temp = leftOperand;
            if (leftOperand == null) {
                return new Subtract(new Const(0), parseValue(4, null));
            }
            if (lvl >= 1) {
                backChar();
                return leftOperand;
            }
            if (ch == '-') {
                return new Subtract(isLegalOperand(temp), parseValue(1, null));
            } else {
                return new Add(isLegalOperand(temp), parseValue(1, null));}
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
                return new Multiply(isLegalOperand(leftOperand), parseValue(2, null));
            }  else {
                return new Divide(isLegalOperand(leftOperand), parseValue(2, null));
            }
        }

        private Variable parseVariable() {
            StringBuilder variableName = new StringBuilder();
            while (!Character.isWhitespace(ch) && ch != '\0' && ch != '\n'&& ch != ')' && ch != '(' && ch != '+' && ch != '-' && ch != '/' && ch!='*') {
                variableName.append(ch);
                nextChar();
            }
            return new Variable(variableName.toString());
        }

        private void copyDigits(final StringBuilder sb) {
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
