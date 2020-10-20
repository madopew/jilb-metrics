package jilbMetrics;

import lexer.Token;

import java.util.ArrayList;

class Parser {
    private final ArrayList<Token> tokens;
    int conditionalOperatorsAmount;
    int maxNestingLevel;
    int currentIndex;

    {
        conditionalOperatorsAmount = 0;
        maxNestingLevel = 0;
        currentIndex = 0;
    }

    public Parser(ArrayList<Token> tokens) {
        this.tokens = new ArrayList<>(tokens);
        parseBlock(tokens.size(), 0);
    }

    private void parseBlock(int endIndex, int currentNestingLevel) {
        if(currentNestingLevel > maxNestingLevel)
            maxNestingLevel = currentNestingLevel;
        while(currentIndex < endIndex) {
            if(tokens.get(currentIndex).value.equals("do")) {
                conditionalOperatorsAmount++;
                currentIndex++;
                parseBlock(getBracketsEnd("{", "}"), currentNestingLevel + 1);
            }
            if(tokens.get(currentIndex).value.equals("else")) {
                currentIndex++;
                if(tokens.get(currentIndex).value.equals("{")) {
                    parseBlock(getBracketsEnd("{", "}"), currentNestingLevel + 1);
                } else {
                    parseBlock(currentIndex + 1, currentNestingLevel + 1);
                }
            }
            if(tokens.get(currentIndex).value.equals("for") ||
                    tokens.get(currentIndex).value.equals("while") ||
                    tokens.get(currentIndex).value.equals("if")) {
                conditionalOperatorsAmount++;
                currentIndex++;
                currentIndex = getBracketsEnd("(", ")");
                currentIndex++;
                if(tokens.get(currentIndex).value.equals("{")) {
                    parseBlock(getBracketsEnd("{", "}"), currentNestingLevel + 1);
                } else {
                    parseBlock(currentIndex + 1, currentNestingLevel + 1);
                }
            }
            currentIndex++;
        }
    }

    /**
     * currentIndex should be at opening bracket of a block/piece of code
     * @return index of closing bracket of the block/piece of code
     */
    private int getBracketsEnd(String openingBracket, String closingBracket) {
        int index = currentIndex;
        int bracketsAmount = 0;
        do {
            String tokenValue = tokens.get(index++).value;
            if(tokenValue.equals(openingBracket))
                bracketsAmount++;
            else if(tokenValue.equals(closingBracket))
                bracketsAmount--;
        } while(bracketsAmount != 0);
        index--;
        return index;
    }
}
