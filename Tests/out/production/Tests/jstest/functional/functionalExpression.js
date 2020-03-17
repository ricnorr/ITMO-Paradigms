"use strict";

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

function divide(a, b) {
    return binOperation(a, b, (x,y) => x / y);
}

function skipWhitespace(s, i) {
    while (a[i] == " ") {
        i++;
    }
    return i;
}

function reverse(a, b) {
    return b,a;
}

function skipToken(s, i, beSkiped) {
    while (beSkiped(s[i]) && i < s.length) {
        i++;
    }
    return i;
}


function split(s) {
    let i = 0;
    let tokens = [];
    i = skipToken(s, i, (s) => (s == " "));
    while (i < s.length) {
        i = skipToken(s, i, (s) => (s == " "));
        let l = i;
        i = skipToken(s, i, (s) => (s != " "));
        tokens.push(s.substring(l, i));
        i = skipToken(s, i, (s) => (s == " "));
    }
    return tokens;
}

function parse(a) {
    let stack = [];
    let token = split(a);
    let t;
    for (let i = 0; i < token.length; i++) {
        switch(token[i]) {
            case("*"):
                stack.push(multiply(stack.pop(), stack.pop()));
                break;
            case("/"):
                t = stack.pop();
                stack.push(divide(stack.pop(), t));
                break;
            case("+"):
                stack.push(add(stack.pop(), stack.pop()));
                break;
            case("-"):
                t = stack.pop();
                stack.push(subtract(stack.pop(), t));
                break;
            default:
                if (!isNaN(token[i])) {
                    stack.push(cnst(parseInt(token[i])));
                } else {
                    stack.push(variable(token[i]));
                }
        }
    }
    return stack[0];
}