package com.jlox;

enum TokenType {
    LEFT_BRACE,
    RIGHT_BRACE,
    LEFT_PAREN,
    RIGHT_PAREN,
    COMMA,
    DOT,
    SEMICOLON,

    ASSIGN,
    LESS_THAN,
    GREATER_THAN,
    LESS_THAN_OR_EQUAL,
    GREATER_THAN_OR_EQUAL,

    IF,
    ELSE,
    PASS,
    RETURN,

    NOT,
    EQUALS,
    NOT_EQUALS,

    EOF,
    IDENTIFIER,

    MINUS,
    MULTIPLY,
    DIVIDE,
    PLUS,

    STRING,
    NUMBER,
    FLOAT,

    OR,
    AND,
    TRUE,
    FALSE,

    BITWISE_OR,
    BITWISE_AND,

    NIL
}