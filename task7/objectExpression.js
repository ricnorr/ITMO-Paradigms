 "strict mode";

let variableTable = {"x" : 0, "y" : 1, "z" : 2};

function Operation(func, strOp) {
    this.func = func;
    this.strOp = strOp;
    return function (...args) {
        this.ops = args;
    }
}

function makeOperation(...args1) {
    let args = arguments;
    let x = function(...args2) {
        return Operation.apply(this, args).apply(this, args2);
    }
    x.prototype = Object.create(Operation.prototype);
    return x;
}

Operation.prototype.evaluate = function() {
    let res = [];
    for (let i = 0; i < this.ops.length; i++) {
        res.push(this.ops[i].evaluate.apply(this.ops[i], arguments));
    }
    return this.func(...res);
}

Operation.prototype.toString = function() {
    return this.ops.join(" ") + " " + this.strOp;
}


let Add = makeOperation((a,b) => (a + b), "+");
let Subtract = makeOperation((a, b) => (a - b), "-");
let Multiply = makeOperation((a , b) => (a * b), "*");
let Divide = makeOperation((a , b) => (a /b), "/");
let Negate = makeOperation((a) => (-a), "negate");
let Log = makeOperation((a,b) => (Math.log(Math.abs(b))/Math.log(Math.abs(a))), "log");
let Power = makeOperation((a,b) => (Math.pow(a, b)), "pow");
let Min3 = makeOperation((a,b,c)=> (Math.min(a,b,c)), "min");
let Max5 = makeOperation((a,b,c,d,e) => (Math.max(a,b,c,d,e)), "max");

function Variable() {
    this.name = arguments[0];
}

Variable.prototype.evaluate = function() {
    return arguments[variableTable[this.name]];
}

Variable.prototype.toString = function() {
    return this.name;
}

function Const() {
    this.op1 = arguments[0];
}

Const.prototype.evaluate = function() {
    return this.op1;
}

Const.prototype.toString = function() {
    return this.op1.toString();
}

