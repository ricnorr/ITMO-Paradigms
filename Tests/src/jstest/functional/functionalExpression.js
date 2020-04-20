// :NOTE: copy-paste code for operation declaration (at least taking arguments [*])
// :NOTE: for hard modification each operation should be function of any arity

// :NOTE: why it's not a const?
"use strict";
const operation = (func) => (...args) => (...args2) => {
    let tmp = [];
    for (let i = 0; i < args.length; i++)
    {
        tmp.push(args[i].apply(null, args2));
    }
    return func.apply(null, tmp);
}

const negate =  operation((a) => -(a));
const add =  operation((a, b) => (a + b));
const subtract =  operation((a,b) => (a - b));
const multiply =  operation((a ,b) => (a * b));
const divide = operation((a, b) => (a / b));
const variable = (name) => (...args) => (args[varNames[name]]);
const cnst = (data) => () => (data);
const cube = operation((a) => (Math.pow(a, 3)));
const cuberoot = operation((a)=> (Math.cbrt(a)));
const avg5 = operation((a, b, c, d ,e) => ((a + b + c + d + e) / 5));
const med3 = operation((a, b, c) => (Math.min(Math.max(a, b), Math.max(b, c), Math.max(a, c))));

const pi = cnst(Math.PI);
const e = cnst(Math.E);

const varNames = {"x" : 0, "y" : 1, "z" : 2};
const constNames = {"e" : e, "pi" : pi};

const operTokens = {
    "*" : multiply,
    "/" : divide,
    "-" : subtract,
    "+" : add,
    "med3" : med3,
    "avg5" : avg5,
    "negate" : negate,
    "cube" : cube,
    "cuberoot" : cuberoot
};

const cntOperands = {
    "*" : 2,
    "/" : 2,
    "-" : 2,
    "+" : 2,
    "med3" : 3,
    "avg5" : 5,
    "negate" : 1,
    "cube" : 1,
    "cuberoot" : 1
};

function skipToken(s, i, beSkiped) {
    while (i < s.length && beSkiped(s[i])) {
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
        if (token[i] in operTokens) {
            let tmp = [];
            let lim = cntOperands[token[i]];
            for (let i = 0; i < lim; i++) {
                tmp.push(stack.pop());
            }
            tmp = tmp.reverse();
            let v = operTokens[token[i]];
            stack.push(operTokens[token[i]].apply(this, tmp));
        } else {
            if (token[i] in constNames) {
                stack.push(constNames[token[i]]);
            } else if (!isNaN(token[i])) {
                stack.push(cnst(parseInt(token[i])));
            } else {
                stack.push(variable(token[i]));
            }
        }
    }
    return stack[0];
}

//x^2−2*x+1
let exp = add(subtract(multiply(variable("x") , variable("x")), multiply(cnst(2), variable("x"))), cnst(1));
for (let i = 0; i < 10; i++) {
    console.log(exp(i));
}