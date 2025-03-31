package com.jlox;

/*
    Grammar
        expr ::= expr | expr ("+" | "-") term
        term ::= term | term ("*" | "/") factor
        factor ::= INTEGER | STRING

    Statements
        - FOR
        - WHILE
        - IF
        - LEFT_BRACE
        - PRINT
        - RETURN

    Expressions
        -
 */

import java.util.ArrayList;
import java.util.List;

class Parser {
    List<Token> tokens;
    int current;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
    }

    boolean check(Token token) {
        if (isAtEnd()) return false;
        return this.tokens.get(this.current) == token;
    }

    boolean match(Token... tokens) {
        for (Token token : tokens) {
            if (check(token)) {
                return true;
            }
        }
        return false;
    }

    Token getCurrentToken() {
        if (this.current >= this.tokens.size()) {
            return null;
        }
        return this.tokens.get(this.current);
    }

    boolean isAtEnd() {
        return this.current >= this.tokens.size() || (this.getCurrentToken() != null && this.getCurrentToken().type == TokenType.EOF);
    }

    void eat(Token t) {
        this.current++;
    }

    Token peek() {
        if (isAtEnd()) return null;
        return this.tokens.get(this.current);
    }

    List<Expr> Parse() {
        List<Expr> x = new ArrayList<>();
        while (!isAtEnd()) {
            x.add(Expr());
        }
        return x;
    }

    Expr Expr() {
        Expr left = Term();
        Token c = this.getCurrentToken();
        switch (c.type) {
            case TokenType.PLUS -> {
                Token op = this.getCurrentToken();
                this.eat(c);
                Expr right = Expr();
                return new Expr.Binary(left, op.type, right);
            }
        }
        return left;
    }

    Expr Term() {
        Expr left = Factor();
        Token c = this.getCurrentToken();
        switch (c.type) {
            case TokenType.MULTIPLY, TokenType.DIVIDE -> {
                Token op = this.getCurrentToken();
                this.eat(c);
                Expr right = Factor();
                return new Expr.Binary(left, op.type, right);
            }
        }

        return left;
    }

    Expr Factor() {
        Token c = this.getCurrentToken();
        switch (c.type) {
            case TokenType.NUMBER -> {
                this.eat(c);
                return new Expr.Literal(c.literal);
            }
            case TokenType.LEFT_BRACE -> {
                this.eat(c);
                Expr e = Expr();
                this.eat(this.getCurrentToken());
                return e;
            }
        }
        return null;
    }
}