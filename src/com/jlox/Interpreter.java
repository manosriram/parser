package com.jlox;

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

    boolean check(TokenType token) {
        if (isAtEnd()) return false;
        return this.tokens.get(this.current).type == token;
    }

    boolean match(TokenType... tokens) {
        for (TokenType token : tokens) {
            if (check(token)) {
                this.eat(token, "");
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

    List<Stmt> Parse() {
        List<Stmt> x = new ArrayList<>();
        while (!isAtEnd()) {
            x.add(declaration());
        }
        return x;
    }

    Stmt declaration() {
        return statement();
    }

    Stmt statement() {
        if (match(TokenType.PRINT)) return printStatement();
        if (match(TokenType.IF)) return ifStatement();

//        if (match(TokenType.LEFT_BRACE)) {
//            return block();
//        }

        return expressionStmt();
    }

    Stmt block() {
        List<Stmt> stmts = new ArrayList<>();
        this.eat(TokenType.LEFT_BRACE, "");
        while (!match(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            stmts.add(statement());
        }
        return new Stmt.Block(stmts);
    }

    Stmt ifStatement() {
        this.eat(TokenType.LEFT_PAREN, "Missing ( at the start of if-condition");
        Expr e = expression();
        this.eat(TokenType.RIGHT_PAREN, "Missing ) at the end of if-condition");

        Stmt ifBlock = block();
        Stmt elseBlock = null;
        if (match(TokenType.ELSE)) {
           elseBlock = block();
        }

        return new Stmt.IfStmt(e, ifBlock, elseBlock);
    }

    Stmt printStatement() {
        Expr e = expression();
        this.eat(TokenType.SEMICOLON, "");
        return new Stmt.PrintStmt(e);
    }

    Stmt expressionStmt() {
        Expr expr = expression();
        this.eat(TokenType.SEMICOLON, "");
        return new Stmt.ExpressionStmt(expr);
    }


    Expr expression() {
        return assignment();
    }

    Token previous() {
        return this.tokens.get(this.current - 1);
    }

    Expr assignment() {
        Expr left = or();
        if (match(TokenType.ASSIGN)) {
            Token eq = previous();
            Expr right = assignment();
            if (left instanceof Expr.Variable) {
                Token name = ((Expr.Variable) left).var;
                return new Expr.Assign(name, right);
            } else {
                throw new RuntimeException("Invalid assignment target " + eq);
            }
        }
        return left;
    }

    Expr or() {
        Expr expr = and();
        while (match(TokenType.OR)) {
            Token op = previous();
            Expr right = and();
            expr = new Expr.Logical(expr, op, right);
        }
        return expr;
    }

    Expr and() {
        Expr expr = equality();
        while (match(TokenType.AND)) {
            Token op = previous();
            Expr right = equality();
            expr = new Expr.Logical(expr, op, right);
        }
        return expr;
    }

    Expr equality() {
        Expr expr = comparison();
        while (match(TokenType.EQUALS, TokenType.NOT_EQUALS)) {
            Token op = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, op, right);
        }
        return expr;
    }

    Expr comparison() {
        Expr expr = term();
        while (match(TokenType.GREATER_THAN, TokenType.GREATER_THAN_OR_EQUAL, TokenType.LESS_THAN, TokenType.LESS_THAN_OR_EQUAL)) {
            Token op = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, op, right);
        }
        return expr;
    }

    Expr term() {
        Expr expr = factor();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token op = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, op, right);
        }
        return expr;
    }

    Expr factor() {
        Expr expr = unary();
        while (match(TokenType.MULTIPLY, TokenType.DIVIDE)) {
            Token op = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, op, right);
        }
        return expr;
    }

    Expr unary() {
        if (match(TokenType.NOT, TokenType.MINUS)) {
            Token op = previous();
            Expr right = unary();
            return new Expr.Unary(op, right);
        }

        return primary();
    }

    Expr primary() {
        if (match(TokenType.TRUE)) return new Expr.Literal(true);
        if (match(TokenType.FALSE)) return new Expr.Literal(false);
        if (match(TokenType.NIL)) return new Expr.Literal(null);
        if (match(TokenType.NUMBER)) {
            return new Expr.Literal(Double.parseDouble((String) previous().literal));
        }
        if (match(TokenType.STRING)) {
            return new Expr.Literal(previous().lexeme);
        }

        if (match(TokenType.IDENTIFIER)) {
            return new Expr.Variable(previous());
        }
        if (match(TokenType.LEFT_PAREN)) {
            Expr expr = expression();
            this.eat(TokenType.RIGHT_PAREN, "Closing ) expected");
            return new Expr.Grouping(expr);
        }

        throw new RuntimeException("Error parsing source");
    }
}
