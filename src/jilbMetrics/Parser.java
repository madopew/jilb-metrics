package jilbMetrics;

import lexer.Token;
import pair.IntegerPair;

import java.util.ArrayList;

class Parser {
    private final ArrayList<Token> tokens;
    int conditionalOperatorsAmount;
    int maxNestingLevel;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = new ArrayList<>(tokens);
        conditionalOperatorsAmount = 0;
        maxNestingLevel = 0;
        parseBlock(findBlockStartEnd(0));
    }

    private int parseBlock(IntegerPair startEnd) {
        while(currentIndex < tokens.size()) {
            if(tokens.get(currentIndex).value.equals("if")) {
                conditionalOperatorsAmount++;
                currentIndex = parseBlock(findBlockStartIndex(currentIndex));
            }
        }
        return 0;
    }
}
