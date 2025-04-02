package com.jlox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class Lox {
    private static void run(String source) {
        Scanner s = new Scanner(source);
        List<Token> tokens = s.scanTokens();

        Parser p = new Parser(tokens);
        List<Expr> exprs = p.Parse();
//        System.out.println(exprs.size());

//        for (Expr e : exprs) {
//            System.out.println(e);
//        }

        Visitor v = new Visitor(exprs);
        v.Visit();

        System.out.println("a = " + v.getFromSymbolTable("a"));
        System.out.println("b = " + v.getFromSymbolTable("b"));
        System.out.println("c = " + v.getFromSymbolTable("c"));
        System.out.println("d = " + v.getFromSymbolTable("d"));
        System.out.println("e = " + v.getFromSymbolTable("e"));
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