"strict mode";

let variableTable = {"x" : 0, "y" : 1, "z" : 2};

function BinaryOperation(op1, op2, func, strOp) {
    this.op1 = op1;
    this.op2 = op2;
    this.func = func;
    this.strOp = strOp;
    Object.setPrototypeOf(this, BinaryOperation.prototype);
}

BinaryOperation.prototype.getStrOp = function() {
    return this.strOp;
}

BinaryOperation.prototype.evaluate = function() {
    return this.func(this.op1.evaluate.apply(this.op1, arguments), this.op2.evaluate.apply(this.op2, arguments));
}

BinaryOperation.prototype.toString = function() {
    return this.op1.toString() + " " + this.op2.toString() + " " + this.getStrOp();
}

function UnaryOperation(op1, func, str) {
    this.op1 = op1;
    this.func = func;
    this.strOp = str;
    Object.setPrototypeOf(this, UnaryOperation.prototype);
}

UnaryOperation.prototype.getStrOp = function() {
    return this.strOp;
}

UnaryOperation.prototype.evaluate = function() {
    return this.func(this.op1.evaluate.apply(this.op1, arguments));
}

UnaryOperation.prototype.toString = function() {
    return this.op1.toString() + " " + this.getStrOp();
}

function Subtract(op1, op2) {
    BinaryOperation.call(this, op1, op2, (a,b) => (a - b), "-");
}

function Multiply(op1, op2) {
    BinaryOperation.call(this, op1, op2, (a,b) => (a * b), "*");
}

function Divide(op1, op2) {
    BinaryOperation.call(this, op1, op2, (a,b) => (a / b), "/");
}

function Negate(op1) {
    UnaryOperation.call(this, op1, (a) => (-a), "negate");
}

function Variable(name) {
    this.name = name;
}

Variable.prototype.evaluate = function() {
    for (let i = 0; i < arguments.length; i++) {
        if (variableTable[this.name] == i) {
            return arguments[i];
        }
    }
}

Variable.prototype.toString = function() {
    return this.name;
}

function Add(op1, op2) {
    BinaryOperation.call(this, op1, op2, (a, b) => (a + b), "+");
}


function Const(op1) {
    this.op1 = op1;
}

Const.prototype.evaluate = function() {
    return this.op1;
}

Const.prototype.toString = function() {
    return this.op1.toString();
}


