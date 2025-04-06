package com.jlox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class Lox {
    private static void run(String source) {
        Scanner s = new Scanner(source);
        List<Token> tokens = s.scanTokens();

        Interpreter p = new Interpreter(tokens);
        List<Expr> exprs = p.Parse();

//        for (Expr e : exprs) {
//            System.out.println(e);
//        }
        Visitor v = new Visitor(exprs);
        v.Visit();

        v.printSymbolTable();
    }

    private static void runFile(String path) throws IOException {
        byte[] f = Files.readAllBytes(Paths.get(path));
        Lox.run(new String(f));
    }

    public static void main(String[] args) throws IOException {
        String path = "/Users/manosriram/dev/jlox/src/com/jlox/source.jlox";
        Lox.runFile(path);
    }
}