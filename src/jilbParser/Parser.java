package jilbParser;

import lexer.Lexer;
import lexer.Token;

import java.util.ArrayList;

public class Parser {
    private int conditionalOperatorsAmount;

    public Parser(ArrayList<Token> tokens) {
    }

    public Parser(String rawText) {
        this(new Lexer(rawText).getTokens());
    }
}
