package com.jlox;

import java.util.List;

class Visitor {
    List<Expr> e;

    Visitor(List<Expr> e) {
        this.e = e;
    }

    int v(Expr x) {
        if (x instanceof Expr.Binary) {
            int l = v(((Expr.Binary) x).left);
            int r = v(((Expr.Binary) x).right);
            System.out.println("l " + l);
            System.out.println("r " + r);
            switch (((Expr.Binary) x).operator) {
                case TokenType.PLUS -> {
                    System.out.println("Result " + (l + r));
                    return l + r;
                }
                case TokenType.MULTIPLY -> {
                    System.out.println("Result " + (l * r));
                    return l * r;
                }
            }
        } else if (x instanceof Expr.Literal) {
            return Integer.parseInt((String) ((Expr.Literal) x).value);
        }
        return 0;
    }

    void Visit() {
        for (Expr e : this.e) {
            v(e);
        }
    }
}
