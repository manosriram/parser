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
import java.util.Objects;

class Interpreter {
    List<Token> tokens;
    int current;

    Interpreter(List<Token> tokens) {
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

    void eat(TokenType t, String errorMessage) {
        Token c = this.getCurrentToken();
        if (t != c.type) {
            if (!Objects.equals(errorMessage, "")) throw new RuntimeException(errorMessage);
            else
                throw new RuntimeException("Line " + (c.line + 1) + ": " + "Expected " + t + " have " + this.getCurrentToken().type);
        }
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
            case TokenType.PLUS, TokenType.MINUS, TokenType.NOT_EQUALS, TokenType.EQUALS, TokenType.OR,
                 TokenType.AND, TokenType.LESS_THAN, TokenType.LESS_THAN_OR_EQUAL, TokenType.GREATER_THAN,
                 TokenType.GREATER_THAN_OR_EQUAL, TokenType.BITWISE_OR, TokenType.BITWISE_AND -> {
                this.eat(c.type, "");
                Expr right = Expr();
                this.eat(TokenType.SEMICOLON, "");
                return new Expr.Binary(left, c.type, right);
            }
            case TokenType.ASSIGN -> {
                this.eat(c.type, "");
                Expr right = Expr();
                this.eat(TokenType.SEMICOLON, "");
                return new Expr.Assign((Expr.Identifier) left, c.type, right);
            }
        }
        return left;
    }

    Expr Term() {
        Expr left = Factor();
        Token c = this.getCurrentToken();
        switch (c.type) {
            case TokenType.MULTIPLY, TokenType.DIVIDE -> {
                this.eat(c.type, "");
                Expr right = Factor();
                return new Expr.Binary(left, c.type, right);
            }
            case TokenType.LEFT_BRACE -> {
                this.eat(TokenType.LEFT_BRACE, "");
                Expr e = Expr();
                this.eat(TokenType.RIGHT_BRACE, "");
                this.eat(TokenType.SEMICOLON, "");
                return e;
            }
        }
        return left;
    }

    Expr Factor() {
        Token c = this.getCurrentToken();
        switch (c.type) {
            case TokenType.NUMBER, TokenType.FLOAT -> {
                this.eat(c.type, "");
                return new Expr.Literal(Float.parseFloat((String) c.literal), c.type.toString());
            }
            case TokenType.STRING -> {
                this.eat(c.type, "");
                return new Expr.Literal(c.lexeme, c.type.toString());
            }
            case TokenType.NOT, TokenType.MINUS, TokenType.PLUS -> {
                this.eat(c.type, "");
                Expr right = Expr();
                return new Expr.Unary(c.type, right);
            }
            case TokenType.TRUE -> {
                this.eat(c.type, "");
                return new Expr.Literal(true, "BOOLEAN");
            }
            case TokenType.FALSE -> {
                this.eat(c.type, "");
                return new Expr.Literal(false, "BOOLEAN");
            }
            case TokenType.NIL -> {
                this.eat(c.type, "");
                return new Expr.Literal(null, c.type.toString());
            }
            case TokenType.IDENTIFIER -> {
                this.eat(c.type, "");
                return new Expr.Identifier((String) c.literal);
            }
            case TokenType.RIGHT_BRACE, TokenType.SEMICOLON -> {
                this.eat(c.type, "");
            }
        }
        return null;
    }
}