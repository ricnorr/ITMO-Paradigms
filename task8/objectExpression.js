"use strict";

let expression = (function() {
    const variableTable = {"x" : 0, "y" : 1, "z" : 2};

    const exception = (function() {
        function makeError(func)
        {
            let error = function()
            {
                this.message = func(...arguments);
            }
            error.prototype = Object.create(Error.prototype);
            error.prototype.constructor = error;
            return error;
        }

        const IllegalOperandNameError = makeError((a, b) => ("Illegal operand name \"" + a +  "\" at position: " + b));
        const MissingClosingBracketError = makeError((a) => ("Expected \')\' at postion: " + a));
        const MissingOperandError = makeError((a) => ("Expected operand at position: " + a));
        const IllegalExpressionError = makeError((a) => ("Expected the end of expression after position: " + a));
        const IllegalOperatorError = makeError((a, b) => ("Illegal operator name \"" + a +  "\" at position: " + b));

        return {
            IllegalOperandNameError : IllegalOperandNameError,
            MissingClosingBracketError : MissingClosingBracketError,
            MissingOperandError : MissingOperandError,
            IllegalExpressionError : IllegalExpressionError,
            IllegalOperatorError: IllegalOperatorError
        };
    })();

    function Operation(func, strOp) {
        this.func = func;
        this.strOp = strOp;
        return function (...args) {
            this.ops = args;
        }
    }

    function makeOperation(...args1) {
        let args = arguments;
        let x = function (...args2) {
            return Operation.apply(this, args).apply(this, args2);
        }
        x.prototype = Object.create(Operation.prototype);
        return x;
    }


    // :NOTE: why it's declared inside abstract object?
    Operation.prototype.evaluate = function () {
        let res = [];
        for (let i = 0; i < this.ops.length; i++) {
            res.push(this.ops[i].evaluate.apply(this.ops[i], arguments));
        }
        return this.func.apply(null, res);
    }

    Operation.prototype.toString = function () {
        let res = this.ops.join(" ");
        return res + " " + this.strOp;
    }

    Operation.prototype.prefix = function () {
        let tmp = [];
        for (let j = 0; j < this.ops.length; j++) {
            tmp.push(this.ops[j].prefix());
        }
        return "(" + this.strOp +" "+ tmp.join(" ") + ")";
    }


    const Add = makeOperation((a, b) => (a + b), "+");
    const Subtract = makeOperation((a, b) => (a - b), "-");
    const Multiply = makeOperation((a, b) => (a * b), "*");
    const Divide = makeOperation((a, b) => (a / b), "/");
    const Negate = makeOperation((a) => (-a), "negate");
    const Sinh = makeOperation((a) => (Math.sinh(a)), "sinh");
    const Cosh = makeOperation((a) => (Math.cosh(a)), "cosh");

    function Variable() {
        this.name = arguments[0];
    }

    Variable.prototype.evaluate = function () {
        return arguments[variableTable[this.name]];
    }

    Variable.prototype.toString = function () {
        return this.name;
    }

    Variable.prototype.prefix = Variable.prototype.toString;


    function Const() {
        this.op1 = arguments[0];
    }

    Const.prototype.evaluate = function () {
        return this.op1;
    }

    Const.prototype.toString = function () {
        return this.op1.toString();
    }

    Const.prototype.prefix = Const.prototype.toString;

    const MapOperations = {"+": Add, "*": Multiply, "-": Subtract, "/": Divide, "negate" : Negate, "sinh" : Sinh, "cosh" : Cosh};
    const cntOperands = {"+": 2, "*": 2, "-": 2, "/": 2, "negate" : 1, "sinh" : 1, "cosh" : 1};

    const parsePrefix = function(source) {
        let s = source;
        let i = 0;

        function skipWhitespace() {
            while (i < s.length && s[i] == " ") {
                i++;
            }
        }

        function getToken() {
            skipWhitespace();
            let l = i;
            if (s[i] === ")" || s[i] === "(") {
                i++;
            } else {
                while (i < s.length && s[i] !== "(" && s[i] !== ")" && s[i] !== " ") {
                    i++;
                }
            }
            return s.substring(l, i);
        }

        function parseOperator(token) {
            if (token in MapOperations) {
                let cnt = cntOperands[token];
                let args = [];
                for (let j = 0; j < cnt; j++) {
                    args.push(parseOperand(getToken()));
                }
                return new MapOperations[token](...args);
            }
            throw new exception.IllegalOperatorError(token, i - token.length);
        }

        function parseBracket() {
            let ans = parseOperator(getToken());
            let tmpi = i;
            if (getToken() != ")") {
                throw new exception.MissingClosingBracketError(tmpi + 1);
            }
            return ans;
        }

        function parseExpression() {
            let token = getToken();
            if (token === "(") {
                return parseBracket();
            } else if (token in MapOperations) {
                return parseOperator(token);
            }
            return parseOperand(token);
        }

        function parseOperand(token) {
            if (token === "") {
                throw new exception.MissingOperandError(i + 1);
            }
            if (token === "(") {
                return parseBracket();
            }
            if (!isNaN(token)) {
                return new Const(Number.parseInt(token));
            }
            if (token in variableTable) {
                return new Variable(token);
            }
            throw new exception.IllegalOperandNameError(token, (i - token.length + 1));
        }

        let x =  parseExpression();
        let tmpi = i;
        if (getToken() != "")
        {
            throw new exception.IllegalExpressionError(tmpi + 1);
        }
        return x;
    };

    return {
        Add : Add,
        Const: Const,
        Variable: Variable,
        Multiply: Multiply,
        Divide: Divide,
        Negate: Negate,
        Subtract: Subtract,
        Sinh : Sinh,
        Cosh : Cosh,
        parsePrefix: parsePrefix,
    };

}());

const Const = expression.Const;
const Sinh = expression.Sinh;
const Cosh = expression.Cosh;
const Divide = expression.Divide;
const Add = expression.Add;
const Negate = expression.Negate;
const Variable = expression.Variable;
const Subtract = expression.Subtract;
const Multiply = expression.Multiply;
const parsePrefix = expression.parsePrefix;

let x = parsePrefix('10');





