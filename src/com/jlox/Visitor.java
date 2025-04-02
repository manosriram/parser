package com.jlox;

import java.util.List;
import java.util.Objects;

class Visitor {
    List<Expr> e;

    Visitor(List<Expr> e) {
        this.e = e;
    }

    Object v(Expr x) {
        if (x instanceof Expr.Binary) {
            Object l = v(((Expr.Binary) x).left);
            Object r = v(((Expr.Binary) x).right);
            switch (((Expr.Binary) x).operator) {
                case TokenType.PLUS -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l + (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l + (float) r;
                    }
                }
                case TokenType.MINUS -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l - (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l - (float) r;
                    }
                }
                case TokenType.MULTIPLY -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l * (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l * (float) r;
                    }
                }
                case TokenType.DIVIDE -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l / (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l / (float) r;
                    }
                }
                case TokenType.EQUALS -> {
                    return l.equals(r);
                }
                case TokenType.NOT_EQUALS -> {
                    return !l.equals(r);
                }
                case TokenType.OR -> {
                    return (boolean) l || (boolean) r;
                }
                case TokenType.AND -> {
                    return (boolean) l && (boolean) r;
                }
                case TokenType.BITWISE_OR -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l | (int) r;
                    } else if (l instanceof Boolean && r instanceof Boolean) {
                        return (boolean) l | (boolean) r;
                    }
                }
                case TokenType.BITWISE_AND -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l & (int) r;
                    } else if (l instanceof Boolean && r instanceof Boolean) {
                        return (boolean) l & (boolean) r;
                    }
                }
                case TokenType.LESS_THAN -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l < (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l < (float) r;
                    }
                }
                case TokenType.GREATER_THAN -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l > (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l > (float) r;
                    }
                }
                case TokenType.LESS_THAN_OR_EQUAL -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l <= (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l <= (float) r;
                    }
                }
                case TokenType.GREATER_THAN_OR_EQUAL -> {
                    if (l instanceof Integer && r instanceof Integer) {
                        return (int) l >= (int) r;
                    } else if (l instanceof Float && r instanceof Float) {
                        return (float) l >= (float) r;
                    }
                }
            }
        } else if (x instanceof Expr.Unary) {
            Object r = v(((Expr.Unary) x).right);
            switch (((Expr.Unary) x).operator) {
                case TokenType.TRUE, TokenType.FALSE -> {
                    return r;
                }
                case TokenType.NOT -> {
                    return !(boolean) r;
                }
                case TokenType.MINUS -> {
                    if (r instanceof Integer) {
                        return -(int) r;
                    } else if (r instanceof Float) {
                        return -(float) r;
                    }
                }
            }
        } else if (x instanceof Expr.Literal) {
            if (((Expr.Literal) x).value == null) return null;

            return ((Expr.Literal) x).value;
        }
        return 0;
    }

    void Visit() {
        for (Expr e : this.e) {
            System.out.println(v(e));
        }
    }
}
