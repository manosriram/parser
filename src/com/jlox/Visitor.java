package com.jlox;

import com.jlox.Expr.Assign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class Visitor {
    List<Expr> e;
    Map<Object, Object> symbolTable;

    Visitor(List<Expr> e) {
        this.e = e;
        this.symbolTable = new HashMap<>();
    }

    Object getFromSymbolTable(String key) {
        return this.symbolTable.get(key);
    }

    void printSymbolTable() {
        System.out.println(this.symbolTable.toString());
    }

    Object v(Expr x) {
        if (x instanceof Expr.Binary) {
            Object l = v(((Expr.Binary) x).left);
            if (this.symbolTable.get(l) != null) {
                l = this.symbolTable.get(l);
            }
            Object r = v(((Expr.Binary) x).right);
            if (this.symbolTable.get(r) != null) {
                r = this.symbolTable.get(r);
            }
            String message = "";
            if (l != null && r != null) message = "Cannot evaluate types " + l.getClass() + " and " + r.getClass();
            switch (((Expr.Binary) x).operator) {
                case TokenType.PLUS -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l + (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l + (float) r;
                    } else if (l instanceof String && r instanceof String) {
                        return (String) l + (String) r;
                    } else if (l instanceof Double && r instanceof Double) {
                        return (Double) l + (Double) r;
                    } else {
                        System.out.println(r);
                        System.out.println(l instanceof String);
                        throw new RuntimeException(message);
                    }
                }
                case TokenType.MINUS -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l - (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l - (float) r;
                    } else {
                        throw new RuntimeException(message);
                    }
                }
                case TokenType.MULTIPLY -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l * (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l * (float) r;
                    } else {
                        throw new RuntimeException(message);
                    }
                }
                case TokenType.DIVIDE -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l / (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l / (float) r;
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
                case TokenType.OR -> {
                    assert l != null;
                    assert r != null;
                    if (l.getClass() != Boolean.class || r.getClass() != Boolean.class) {
                        throw new RuntimeException(message);
                    }
                    return (boolean) l || (boolean) r;
                }
                case TokenType.AND -> {
                    if (l.getClass() != Boolean.class || r.getClass() != Boolean.class) {
                        throw new RuntimeException(message);
                    }
                    return (boolean) l && (boolean) r;
                }
                case TokenType.BITWISE_OR -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l | (int) r;
                    } else if (l instanceof Boolean && r instanceof Boolean) {
                        return (boolean) l | (boolean) r;
                    } else {
                        throw new RuntimeException(message);
                    }
                }
                case TokenType.BITWISE_AND -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l & (int) r;
                    } else if (l instanceof Boolean && r instanceof Boolean) {
                        return (boolean) l & (boolean) r;
                    } else {
                        throw new RuntimeException(message);
                    }
                }
                case TokenType.LESS_THAN -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l < (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l < (float) r;
                    } else {
                        throw new RuntimeException(message);
                    }
                }
                case TokenType.GREATER_THAN -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l > (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l > (float) r;
                    } else {
                        throw new RuntimeException(message);
                    }
                }
                case TokenType.LESS_THAN_OR_EQUAL -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l <= (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l <= (float) r;
                    } else {
                        throw new RuntimeException(message);
                    }
                }
                case TokenType.GREATER_THAN_OR_EQUAL -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l >= (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l >= (float) r;
                    } else {
                        throw new RuntimeException(message);
                    }
                }
                case TokenType.ASSIGN -> {
                    Expr.Identifier id = ((Expr.Assign) l).id;
                    System.out.println("id = " + id);
                    this.symbolTable.put(id, r);
                }
            }
        } else if (x instanceof Expr.Unary) {
            Object r = v(((Expr.Unary) x).right);
            if (this.symbolTable.get(r) != null) {
                r = this.symbolTable.get(r);
            }
            switch (((Expr.Unary) x).operator) {
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
        } else if (x instanceof Expr.Literal) {
            if (((Expr.Literal) x).value == null) return null;
            return ((Expr.Literal) x).value;
        } else if (x instanceof Expr.Identifier) {
            return ((Expr.Identifier) x).name;
        } else if (x instanceof Expr.Assign) {
            Object r = v(((Expr.Assign) x).right);
            Object l = v(((Expr.Assign) x).id);
            this.symbolTable.put(l, r);
        }
        return x;
    }

    void Visit() {
        for (Expr e : this.e) v(e);
    }
}
