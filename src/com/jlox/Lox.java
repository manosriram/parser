package com.jlox;

import com.jlox.Stmt.ExpressionStmt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class Lox implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    Map<Object, Object> symbolTable;

    Lox() {
        this.symbolTable = new HashMap<>();
    }

    private void run(String source) {
        Scanner s = new Scanner(source);
        List<Token> tokens = s.scanTokens();

        Interpreter p = new Interpreter(tokens);
        List<Stmt> stmts = p.Parse();

        for (Stmt stmt : stmts) {
            stmt.accept(this);
        }

//        this.printSymbolTable();
    }

    void printSymbolTable() {
        System.out.println(this.symbolTable.toString());
    }

    private static void runFile(String path) throws IOException {
        byte[] f = Files.readAllBytes(Paths.get(path));
        new Lox().run(new String(f));
    }

    public static void main(String[] args) throws IOException {
        String path = "/Users/manosriram/dev/jlox/src/com/jlox/source.jlox";
        Lox.runFile(path);
    }

    Object eval(Expr expr) {
        return expr.accept(this);
    }

    void execute(Stmt stmt) {
        stmt.accept(this);
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object l = eval(expr.left);
        Object r = eval(expr.right);
        String message = "";
        if (l != null && r != null) message = "Cannot evaluate types " + l.getClass() + " and " + r.getClass();
        switch (expr.operator.type) {
            case TokenType.PLUS -> {
                if (l instanceof String && r instanceof String) {
                    return (String) l + (String) r;
                } else if (l instanceof Double && r instanceof Double) {
                    return (double) l + (double) r;
                } else {
                    throw new RuntimeException(message);
                }
            }
            case TokenType.MINUS -> {
                if (l instanceof Double && r instanceof Double) {
                    return (double) l - (double) r;
                } else {
                    throw new RuntimeException(message);
                }
            }
            case TokenType.MULTIPLY -> {
                if (l instanceof Double && r instanceof Double) {
                    return (double) l * (double) r;
                } else {
                    throw new RuntimeException(message);
                }
            }
            case TokenType.DIVIDE -> {
                if (l instanceof Double && r instanceof Double) {
                    return (double) l / (double) r;
                } else {
                    throw new RuntimeException(message);
                }
            }
            case TokenType.EQUALS -> {
                return Objects.equals(l, r);
            }
            case TokenType.NOT_EQUALS -> {
                return !Objects.equals(l, r);
            }
            case TokenType.LESS_THAN -> {
                if (l instanceof Double && r instanceof Double) {
                    return (double) l < (double) r;
                } else {
                    throw new RuntimeException(message);
                }
            }
            case TokenType.GREATER_THAN -> {
                if (l instanceof Double && r instanceof Double) {
                    return (double) l > (double) r;
                } else {
                    throw new RuntimeException(message);
                }
            }
            case TokenType.LESS_THAN_OR_EQUAL -> {
                if (l instanceof Double && r instanceof Double) {
                    return (double) l <= (double) r;
                } else {
                    throw new RuntimeException(message);
                }
            }
            case TokenType.GREATER_THAN_OR_EQUAL -> {
                if (l instanceof Double && r instanceof Double) {
                    return (double) l >= (double) r;
                } else {
                    throw new RuntimeException(message);
                }
            }
            case TokenType.IDENTIFIER -> {
                break;
            }
        }
        return null;
    }

    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        Object r = eval(expr.value);
        this.symbolTable.put(expr.name.literal, r);
        return r;
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return eval(expr.expression);
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    boolean isTruth(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Boolean) return (boolean) obj;
        return true;
    }

    @Override
    public Object visitLogicalExpr(Expr.Logical expr) {
        Object l = eval(expr.left);
        Object r = eval(expr.right);

        switch (expr.op.type) {
            case TokenType.OR -> {
                return isTruth(l) || isTruth(r);
            }
            case TokenType.AND -> {
                return isTruth(l) && isTruth(r);
            }
            default -> {
                return isTruth(l) && isTruth(r);
            }
        }
    }

    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
        return this.symbolTable.get(expr.var.literal);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object r = eval(expr.right);
        if (this.symbolTable.get(r) != null) {
            r = this.symbolTable.get(r);
        }
        switch (expr.operator.type) {
            case TokenType.TRUE, TokenType.FALSE -> {
                return r;
            }
            case TokenType.NOT -> {
                if (r.getClass() != Boolean.class) {
                    throw new RuntimeException("Cannot evaluate type " + r.getClass());
                }
                return !(boolean) r;
            }
            case TokenType.PLUS -> {
                if (r instanceof Integer) {
                    return r;
                } else if (r instanceof Float) {
                    return r;
                } else {
                    throw new RuntimeException("Cannot evaluate type " + r.getClass());
                }
            }
            case TokenType.MINUS -> {
                if (r instanceof Integer) {
                    return -(int) r;
                } else if (r instanceof Float) {
                    return -(float) r;
                } else {
                    throw new RuntimeException("Cannot evaluate type " + r.getClass());
                }
            }
        }
        return null;
    }

    @Override
    public Void visitExpressionStmt(Stmt.ExpressionStmt stmt) {
        eval(stmt.expression);
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.PrintStmt stmt) {
        Object o = eval(stmt.expression);
        System.out.println(o);
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.IfStmt stmt) {
        Object condition = eval(stmt.condition);
        if (isTruth(condition)) {
            execute(stmt.thenBranch);
        } else {
            execute(stmt.elseBranch);
        }

        return null;
    }

    @Override
    public Void visitBlock(Stmt.Block stmt) {
        for (Stmt s: stmt.statements) {
            execute(s);
        }
        return null;
    }
}