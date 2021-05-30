"use strict";

const operations = new Map();

function expressionFactory(toString, prefix, postfix, evaluate, diff) {
    function Expression(...data) {
        this._data = data;
    }

    Expression.prototype.toString = toString;
    Expression.prototype.prefix = prefix;
    Expression.prototype.postfix = postfix;
    Expression.prototype.evaluate = evaluate;
    Expression.prototype.diff = diff;
    Expression.prototype.constructor = Expression;

    return Expression;
};

const Const = expressionFactory(
    function () {
        return this._data.toString();
    },
    function () {
        return this._data.toString();
    },
    function () {
        return this._data.toString();
    },
    function (...values) {
        return this._data[0];
    },
    function (diffName) {
        return Const.ZERO;
    });
Const.ZERO = new Const(0);
Const.ONE = new Const(1);
Const.TWO = new Const(2);
Const.THREE = new Const(3);

const Variable = expressionFactory(
    function () {
        return this._data[0];
    },
    function () {
        return this._data[0];
    },
    function () {
        return this._data[0];
    },
    function (...values) {
        let index = this.variables.indexOf(this._data[0]);
        return values[index];
    },
    function (diffName) {
        return this._data[0] === diffName ? Const.ONE : Const.ZERO;
    });
Variable.prototype.variables = ["x", "y", "z"];

const AbstractArithmeticOperation = expressionFactory(
    function () {
        return this._data.join(" ") + " " + this._operaionSymbol;
    },
    function () {
        return "(" + this._operaionSymbol + " " + this._data.map(ex => ex.prefix()).join(" ") + ")";
    },
    function () {
        return "(" + this._data.map(ex => ex.postfix()).join(" ") + " " + this._operaionSymbol + ")";
    },
    function (...values) {
        return this._computation(...this._data.map(expression => expression.evaluate(...values)));
    },
    function (diffName) {
        return this._diffRule(...this._data)(...this._data.map(expr => expr.diff(diffName)));
    });

function operationFactory(computation, operaionSymbol, diffRule) {
    function Operation(...args) {
        AbstractArithmeticOperation.call(this, ...args);
    };

    Operation.prototype = Object.create(AbstractArithmeticOperation.prototype);
    Operation.prototype._operaionSymbol = operaionSymbol;
    Operation.prototype._computation = computation;
    Operation.prototype._diffRule = diffRule;
    Operation.prototype.constructor = Operation;
    operations.set(operaionSymbol, Operation);

    return Operation;
};

const Add = operationFactory((x, y) => x + y, "+",
    (x, y) => (dx, dy) => new Add(dx, dy));

const Subtract = operationFactory((x, y) => x - y, "-",
    (x, y) => (dx, dy) => new Subtract(dx, dy));

const Multiply = operationFactory((x, y) => x * y, "*",
    (x, y) => (dx, dy) => new Add(new Multiply(dx, y), new Multiply(x, dy)));

const Divide = operationFactory((x, y) => x / y, "/",
    (x, y) => (dx, dy) => new Divide(new Subtract(new Multiply(dx, y), new Multiply(x, dy)),
        new Multiply(y, y)));

const Negate = operationFactory(x => -x, "negate",
    x => dx => new Negate(dx));

const Cube = operationFactory(x => Math.pow(x, 3), "cube",
    x => dx => new Multiply(Const.THREE, new Multiply(new Multiply(x, x), dx)));

const Cbrt = operationFactory(Math.cbrt, "cbrt",
    x => dx => new Divide(dx, new Multiply(Const.THREE, new Multiply(new Cbrt(x), new Cbrt(x)))));

const diffSumsq = (...args) => (...dargs) => {
    let res = Const.ZERO;
    for (let i = 0; i < args.length; i++) {
        res = new Add(res, new Multiply(args[i], dargs[i]));
    }
    return res;
}

const sumsq = (...args) => args.reduce((x, y) => x + y * y, 0);

const Sumsq = operationFactory(sumsq, "sumsq",
    (...args) => (...dargs) => new Multiply(diffSumsq(...args)(...dargs), Const.TWO));

const Length = operationFactory((...args) => Math.sqrt(sumsq(...args)), "length",
    (...args) => (...dargs) => args.length === 0 ? Const.ZERO :
        new Divide(diffSumsq(...args)(...dargs), new Length(...args)));

const parse = expression => {
    let expr = [];
    for (const curr of expression.trim().split(/\s+/)) {
        if (operations.has(curr)) {
            let operation = operations.get(curr);
            let arity = operation.prototype._computation.length;
            expr.push(new operation(...expr.splice(-arity)));
        } else if (Variable.prototype.variables.includes(curr)) {
            expr.push(new Variable(curr));
        } else {
            expr.push(new Const(Number.parseInt(curr)));
        }
    }
    return expr.pop();
};

function exceptionFactory(superLink, name, messageFunc) {
    function Exception(...values) {
        this.message = messageFunc(...values);
    }

    Exception.prototype = Object.create(superLink.prototype);
    Exception.prototype.name = name;
    Exception.prototype.constructor = Exception;

    return Exception;
};

const ParseExpressionException = exceptionFactory(Error,
    "ParseExpressionException",
    pos => "Parse exception at " + pos);

const UnexpectedCharException = exceptionFactory(ParseExpressionException,
    "UnexpectedCharException",
    (expected, found, pos) => "Expected at " + pos + " " +
        (expected == '\0' ? "end of expression" : "'" + expected + "'") + ", found " +
        (found == '\0' ? "end of expression" : "'" + found + "'"));

const UnknownOperationException = exceptionFactory(ParseExpressionException,
    "UnknownOperationException",
    (pos, op) => "Unknown operator '" + op + "' at " + pos);

const LostArgumentException = exceptionFactory(ParseExpressionException,
    "LostArgumentException",
    pos => "Lost argument at " + pos);

const IllegalVariableNameException = exceptionFactory(ParseExpressionException,
    "IllegalVariableNameException",
    (pos, name) => "Illegal variable name '" + name + "' at " + pos);

const IllegalArgumentException = exceptionFactory(ParseExpressionException,
    "IllegalArgumentException",
    pos => "Illegal argument at " + pos);

class BaseParser {
    constructor(expression) {
        this.expression = expression;
        this.pos = 0;
        this.nextChar();
    }
    nextChar() {
        this.ch = this.pos < this.expression.length ? this.expression[this.pos++] : '\0';
    }
    test(expected) {
        for (const c of expected) {
            if (c === this.ch) {
                this.nextChar();
            } else {
                return false;
            };
        };
        return true;
    }
    expect(expected) {
        if (this.test(expected)) {
            return true;
        }
        throw new UnexpectedCharException(expected,
            this.expression.substring(this.pos, this.pos + expected.length), this.pos);
    }
    skipWhitespace() {
        while (this.test(" "));
    }
    eof() {
        return this.test('\0');
    }
    between(from, to) {
        return from <= this.ch && this.ch <= to;
    }
    isDigit() {
        return this.between('0', '9') || this.ch === '-';
    }
    isLetter() {
        return this.between('a', 'z');
    }
    expectLetterOrDigit() {
        if (this.isDigit() || this.isLetter()) {
            throw new UnexpectedCharException("whitespace", this.ch, this.pos);
        };
    }
}

class ExpressionParser {
    constructor(expression, mode) {
        this.mode = mode;
        this.base = new BaseParser(this.reverseIfNeed(expression));
        this.openBracket = mode[0];
        this.closeBracket = mode[1];
    };
    parse() {
        let result = this.parseElement();
        if (!this.base.eof()) {
            throw new UnexpectedCharException('\0', this.base.ch, this.base.pos);
        }
        return result;
    }
    parseElement() {
        this.base.skipWhitespace();
        let result = this.parseExpression();
        this.base.skipWhitespace();
        return result;
    }
    parseExpression() {
        if (this.base.test(this.openBracket)) {
            this.base.skipWhitespace();
            return this.parseOperation();
        }
        return this.parseValues();
    }
    parseOperation() {
        for (const op of operations.keys()) {
            if (this.base.test(this.reverseIfNeed(op))) {
                let constructor = operations.get(op);
                this.base.expectLetterOrDigit();
                return new constructor(...this.parseArguments(constructor.prototype._computation.length));
            }
        }
        throw new UnknownOperationException(this.base.pos, this.base.ch);
    }
    parseArguments(arity) {
        let args = [];
        this.base.skipWhitespace();
        while (!this.base.test(this.closeBracket)) {
            args.push(this.parseValues());
            this.base.skipWhitespace();
        }
        if (arity !== 0 && arity !== args.length) {
            throw new IllegalArgumentException(this.base.pos);
        }
        return this.mode === Mods.PREFIX ? args : args.reverse();
    }
    parseValues() {
        this.base.skipWhitespace();
        if (this.base.isDigit()) {
            return this.parseConst();
        } else if (this.base.isLetter()) {
            return this.parseVariable();
        } else if (this.base.test(this.openBracket)) {
            this.base.skipWhitespace();
            return this.parseOperation();
        } else {
            throw new LostArgumentException(this.base.pos);
        }
    }
    parseConst() {
        let number = '';
        while (this.base.isDigit()) {
            number += this.base.ch;
            this.base.nextChar();
        }
        return new Const(Number.parseInt(this.reverseIfNeed(number)));
    }
    parseVariable() {
        let varName = '';
        while (this.base.isLetter()) {
            varName += this.base.ch;
            this.base.nextChar();
        }
        if (Variable.prototype.variables.includes(varName)) {
            return new Variable(varName);
        } else {
            throw new IllegalVariableNameException(this.base.pos, varName);
        }
    }
    reverseIfNeed(str) {
        return this.mode === Mods.PREFIX ? str : str.split('').reverse().join('');
    }
}

const Mods = {
    PREFIX : ['(', ')'],
    POSTFIX : [')', '(']
}

const parsePrefix = (expression) => new ExpressionParser(expression, Mods.PREFIX).parse();

const parsePostfix = (expression) => new ExpressionParser(expression, Mods.POSTFIX).parse();