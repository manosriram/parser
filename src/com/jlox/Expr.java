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

    static class Assign extends Expr {
        final Expr right;
        final Identifier id;
        final TokenType operator;

        Assign(Identifier left, TokenType operator, Expr right) {
            this.right = right;
            this.id = left;
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

    static class Identifier extends Expr {
        final String name;

        Identifier(String name) {
            this.name = name;
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
