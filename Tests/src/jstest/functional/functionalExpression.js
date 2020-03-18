"use strict";

let varNames = {"x" : 0, "y" : 1, "z" : 2};
let constNames = {"e" : Math.E, "pi" : Math.PI};

function binOperation(a, b, func) {
    function evaluate() {
        return func(a.apply(this, arguments), b.apply(this, arguments));
    }
    return evaluate;
}

function negate(x) {
    function evaluate() {
        return -x;
    }
    return evaluate;
}

function cube(x) {
    function evaluate() {
        return Math.pow(x.apply(this, arguments), 3);
    }
    return evaluate;
}

function cuberoot(x) {
    function evaluate() {
        return Math.pow(x.apply(this, arguments), 1/3);
    }
    return evaluate;
}

function variable(a) {
    let name = a;
    function evaluate() {
        for (let i = 0; i < arguments.length; i++)
        {
            if (varNames[name] == i) {
                return arguments[i];
            }
        }
    }
    return evaluate;
}

function cnst(a) {
    function evaluate() {
        if (a in constNames) {
            return constNames[a];
        } else {
            return a;
        }
    }
    return evaluate;
}

function add(a, b) {
    return binOperation (a, b, (x, y) => x + y);
}

function sin(a) {
    function evaluate() {
        return Math.sin(a.apply(this, arguments));
    }
    return evaluate;
}

function cos(a) {
    function evaluate() {
        return Math.cos(a.apply(this, arguments));
    }
    return evaluate;
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
            case("sin"):
                stack.push(sin(stack.pop()));
                break;
            case("cos"):
                stack.push(cos(stack.pop()));
                break;
            case("cuberoot"):
                stack.push(cuberoot(stack.pop()));
                break;
            case("cube"):
                stack.push(cube(stack.pop()));
                break;
            default:
                if (token[i] in constNames) {
                    stack.push(cnst(token[i]));
                } else if (!isNaN(token[i])) {
                    stack.push(cnst(parseInt(token[i])));
                } else {
                    stack.push(variable(token[i]));
                }
        }
    }
    return stack[0];
}

let exp = parse("x y +");
for (let i = 0; i++; i < 10) {
    console.log(exp(i));
}
