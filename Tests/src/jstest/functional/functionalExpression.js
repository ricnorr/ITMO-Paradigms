"use strict"

function binOperation(a, b, func) {
    function evaluate(x) {
        return func(a(x), b(x));
    }
    return evaluate;
}

function negate(x) {
    function evaluate(x) {
        return -x;
    }
    return evaluate;
}

function variable(a) {
    function evaluate(x) {
        return x;
    }
    return evaluate;
}

function cnst(a) {
    function evaluate(x) {
        return a;
    }
    return evaluate;
}

function add(a, b) {
    return binOperation (a, b, (x, y) => x + y);
}

function subtract(a, b) {
    return binOperation(a , b, (x, y) => x- y);
}

function multiply(a, b) {
    return binOperation(a, b, (x,y) => x * y);
}

function devide(a, b) {
    return binOperation(a, b, (x,y) => x / y);
}

let expr = subtract(
    multiply(
        cnst(2),
        variable("x")
    ),
    cnst(3)
)
console.log(expr(5));
