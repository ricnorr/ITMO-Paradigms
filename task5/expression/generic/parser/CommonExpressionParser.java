package expression.generic.parser;

import expression.generic.exceptions.ParsingException;
import expression.generic.operations.*;

import java.util.Map;
import java.util.Set;

    public class CommonExpressionParser<T extends Number> extends BaseParser {
        private final Calculation<T> calc;

        private final Map<String, Integer> PRIORITY = Map.of(
                "*", 3,
                "/", 3,
                "+", 2,
                "-", 2,
                "m" , 1
        );
        
        private final Set<Character> VARIABLE_ALPHABET = Set.of('x', 'y', 'z');

        private final Set<Character> AFTER_VARIABLE = Set.of('*','-', ')', ' ', '+', '/', '\0', '\t', '\r');

        public CommonExpressionParser(String data, Calculation<T> op) {
            super(new StringSource(data));
            nextChar();
            this.calc = op;
        }

        public CommonExpression<T> parse() throws ParsingException {
            return this.parseSource('\0', 0);
        }

         private CommonExpression<T> parseSource(char ban, int lvl) throws ParsingException {
            skipWhitespace();
            CommonExpression<T> result = null;
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

        private CommonExpression<T> parseToken() throws ParsingException {
            skipWhitespace();
            if (test('-')) {
                if (nextIsDigit(ch)) {
                    backChar();
                    return getConst();
                }
                return new Negate<>(parseToken(), calc);
            }
            if (test('c')) {
                if (test('o') && test('u') && test('n') && test('t')) {
                    return new Count<>(parseToken(), calc);
                } else {
                    throw new ParsingException("Expected count");
                }
            }
            if (nextIsDigit(ch)) {
                return getConst();
            }
            if (test('(')) {
                return parseSource(')', 0);
            }
            return parseVariable();
        }


        private CommonExpression<T> parseOperation(int lvl, CommonExpression<T> leftOperand) throws ParsingException {
            skipWhitespace();
            while (ch != '\0' && ch != ')') {
                skipWhitespace();
                if (lvl >= PRIORITY.getOrDefault(Character.toString(ch), Integer.MAX_VALUE)) {
                    return leftOperand;
                }
                switch (ch) {
                    case '+':
                    case '-':
                        leftOperand = parseSumSub(leftOperand, ch);
                        break;
                    case '*':
                    case '/':
                        leftOperand = parseMultDev(leftOperand, ch);
                        break;
                    case 'm':
                        test('m');
                        if (test('i') && test('n')) {
                            leftOperand = parseMin(leftOperand);
                            break;
                        } else if (test('a') && test('x')) {
                            leftOperand = parseMax(leftOperand);
                            break;
                        }
                    default:
                        throw new ParsingException("Illegal symbol " + "\'" + ch + "\'" + " at position " + getPointer());
                }
            }
            return leftOperand;
        }

        private CommonExpression<T> parseOperandExpression(int lvl, CommonExpression<T> leftOperand)  throws ParsingException  {
             if (leftOperand == null) {
                 leftOperand = parseToken();
             }
             return parseOperation(lvl, leftOperand);
        }


        private CommonExpression<T> parseMax(CommonExpression<T> leftOperand) throws ParsingException {
            return new Max<>(leftOperand, parseOperandExpression(PRIORITY.get("m"), null), calc);
        }

        private CommonExpression<T> parseMin(CommonExpression<T> leftOperand) throws ParsingException {
            return new Min<>(leftOperand, parseOperandExpression(PRIORITY.get("m"), null), calc);
        }

        private CommonExpression<T> parseSumSub(CommonExpression<T> leftOperand, char tempCh) throws ParsingException {
            test(tempCh);
            if (tempCh == '-') {
                return new Subtract<>(leftOperand, parseOperandExpression(1, null), calc);
            } else {
                return new Add<>(leftOperand, parseOperandExpression(1, null), calc);}
        }

        private CommonExpression<T> parseMultDev(CommonExpression<T> leftOperand, char tempCh) throws ParsingException {
            test(tempCh);
            if (tempCh == '*') {
                return new Multiply<>(leftOperand, parseOperandExpression(PRIORITY.get("*"), null), calc);
            }  else {
                return new Divide<>(leftOperand, parseOperandExpression(PRIORITY.get("/"), null), calc);
            }
        }

        private Variable<T> parseVariable() throws ParsingException {
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

            return new Variable<>(variableName.toString(), calc);
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

        private void parseConst(StringBuilder sb) {
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

        private Const<T> getConst() throws ParsingException {
            StringBuilder sb = new StringBuilder();
            parseConst(sb);
            return new Const<>(calc.transform(sb.toString()));
        }

        private void skipWhitespace() {
            while (test(' ') || test('\r') || test('\n') || test('\t')) {
                // skip
            }
        }
    }
