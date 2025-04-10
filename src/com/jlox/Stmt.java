package com.jlox;

import java.util.List;

abstract class Stmt {
    interface Visitor<R> {
        R visitExpressionStmt(ExpressionStmt stmt);
        R visitPrintStmt(PrintStmt stmt);
        R visitBlock(Block stmt);
        R visitIfStmt(IfStmt stmt);
    }

    abstract <R> R accept(Stmt.Visitor<R> visitor);

    static class PrintStmt extends Stmt {
        Expr expression;

        PrintStmt(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Stmt.Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }
    }

    static class ExpressionStmt extends Stmt {
        Expr expression;

        ExpressionStmt(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Stmt.Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }
    }

    static class Block extends Stmt {
        List<Stmt> statements;

        Block(List<Stmt> statements) {
            this.statements = statements;
        }

        @Override
        <R> R accept(Stmt.Visitor<R> visitor) {
            return visitor.visitBlock(this);
        }
    }


    static class IfStmt extends Stmt {
        Expr condition;
        Stmt thenBranch;
        Stmt elseBranch;

        IfStmt(Expr condition, Stmt thenBranch, Stmt elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        <R> R accept(Stmt.Visitor<R> visitor) {
            return visitor.visitIfStmt(this);
        }
    }
}