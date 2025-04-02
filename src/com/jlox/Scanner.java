package com.jlox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Scanner {
    String source;
    int line;
    int current;
    int start;
    List<Token> tokens;
    Map<String, TokenType> keywords;

    void buildKeywords() {
        this.keywords.put("nil", TokenType.NIL);
        this.keywords.put("and", TokenType.AND);
        this.keywords.put("or", TokenType.OR);
        this.keywords.put("if", TokenType.IF);
        this.keywords.put("else", TokenType.ELSE);
        this.keywords.put("true", TokenType.TRUE);
        this.keywords.put("false", TokenType.FALSE);
        this.keywords.put("pass", TokenType.PASS);
        this.keywords.put("return", TokenType.RETURN);
    }

    Scanner(String source) {
        this.source = source;
        this.line = 0;
        this.current = 0;
        this.start = 0;
        this.tokens = new ArrayList<>();
        this.keywords = new HashMap<String, TokenType>();
        this.buildKeywords();
    }

    boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    void addToken(TokenType tokentype) {
        this.tokens.add(new Token(tokentype, "", "", this.line));
    }

    void addToken(TokenType tokentype, Object literal) {
        String lexeme = literal.toString();
        if (tokentype == TokenType.STRING) {
            lexeme = this.source.substring(this.start + 1, this.current - 1);
        }
        this.tokens.add(new Token(tokentype, lexeme, literal, this.line));
    }

    char advance() {
        return isAtEnd() ? '\0' : this.source.charAt(this.current++);
    }

    boolean isAtEnd() {
        return this.current >= this.source.length();
    }

    boolean match(char expected) {
        if (isAtEnd()) return false;
        if (this.source.charAt(this.current) != expected) return false;

        this.current++;
        return true;
    }

    char peek() {
        if (isAtEnd()) return '\0';
        return this.source.charAt(this.current);
    }

    char peekNext() {
        if (isAtEnd()) return '\0';
        return this.source.charAt(this.current + 1);
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            this.start = this.current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", "", this.line));
        return this.tokens;
    }

    void scanToken() {
        char c = this.advance();
        switch (c) {
            case '\n':
                this.line += 1;
                break;
            case '+':
                this.addToken(TokenType.PLUS);
                break;
            case '*':
                this.addToken(TokenType.MULTIPLY);
                break;
            case '-':
                this.addToken(TokenType.MINUS);
                break;
            case '(':
                this.addToken(TokenType.LEFT_BRACE);
                break;
            case ')':
                this.addToken(TokenType.RIGHT_BRACE);
                break;
            case '{':
                this.addToken(TokenType.LEFT_PAREN);
                break;
            case '}':
                this.addToken(TokenType.RIGHT_PAREN);
                break;
            case ',':
                this.addToken(TokenType.COMMA);
                break;
            case '.':
                this.addToken(TokenType.DOT);
                break;
            case ';':
                this.addToken(TokenType.SEMICOLON);
                break;
            case '!':
                this.addToken(match('=') ? TokenType.NOT_EQUALS : TokenType.NOT);
                break;
            case '=':
                this.addToken(match('=') ? TokenType.EQUALS : TokenType.ASSIGN);
                break;
            case '<':
                this.addToken(match('=') ? TokenType.LESS_THAN_OR_EQUAL : TokenType.LESS_THAN);
                break;
            case '>':
                this.addToken(match('=') ? TokenType.GREATER_THAN_OR_EQUAL : TokenType.GREATER_THAN);
                break;
            case '/':
                if (match('/')) {
                    while (!isAtEnd() && peek() != '\n') {
                        this.advance();
                    }
                } else {
                    this.addToken(TokenType.DIVIDE);
                }
                break;
            case '"':
                while (peek() != '"' && !isAtEnd()) {
                    if (peek() == '\n') {
                        this.line++;
                    }
                    this.advance();
                }
                if (isAtEnd()) {
                    System.out.println("Error ");
                    return;
                }
                this.advance();
                String literal = this.source.substring(this.start, this.current);
                this.addToken(TokenType.STRING, literal);
                break;
            case ' ', '\t', '\r':
                break;
            default:
                if (isDigit(c)) {
                    while (isDigit(peek())) {
                        this.advance();
                    }
                    boolean isFloat = false;
                    if (peek() == '.' && isDigit(peekNext())) {
                        this.advance();
                        isFloat = true;
                    }

                    while (isDigit(peek())) {
                        this.advance();
                    }

                    this.addToken(!isFloat ? TokenType.NUMBER : TokenType.FLOAT, this.source.substring(this.start, this.current));
                } else if (isAlpha(c)) {
                    while (isAlpha(peek())) {
                        this.advance();
                    }
                    String t = this.source.substring(this.start, this.current);
                    TokenType tokentype = this.keywords.get(t);
                    if (tokentype != null) {
                        this.addToken(tokentype);
                    } else {
                        this.addToken(TokenType.IDENTIFIER, t);
                    }
                } else {
                    System.out.printf("Unexpected char at line %d\n", this.line);
                }
        }

    }
}