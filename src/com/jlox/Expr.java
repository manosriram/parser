package com.jlox;

abstract class Expr {
    static class Binary extends Expr {
        final Expr right;
        final Expr left;
        final TokenType operator;

        Binary(Expr left, TokenType operator, Expr right) {
            this.right = right;
            this.left = left;
            this.operator = operator;
        }
    }

    static class Literal extends Expr {
        final Object value;
        final String type;

        Literal(Object value, String type) {
            this.value = value;
            this.type = type;
        }
    }

    static class Unary extends Expr {
        final TokenType operator;
        final Expr right;

        Unary(TokenType operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }
    }
}
