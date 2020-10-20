package jilbMetrics;

import lexer.Token;

import java.util.ArrayList;

class Parser {
    private final ArrayList<Token> tokens;
    int conditionalOperatorsAmount;
    int maxNestingLevel;
    int currentIndex;
    int currentNestingLevel;

    {
        conditionalOperatorsAmount = 0;
        maxNestingLevel = 0;
        currentNestingLevel = 0;
        currentIndex = 0;
    }

    public Parser(ArrayList<Token> tokens) {
        this.tokens = new ArrayList<>(tokens);
        parseBlock(tokens.size());
    }

    private void parseBlock(int endIndex) {
        if(currentNestingLevel > maxNestingLevel)
            maxNestingLevel = currentNestingLevel;
        while(currentIndex < endIndex) {
            if(currentTokenEquals("do")) {
                currentNestingLevel++;
                conditionalOperatorsAmount++;
                currentIndex++;
                parseBlock(getBracketsEnd("{", "}"));
            } else if(currentTokenEquals("else")) {
                currentNestingLevel++;
                currentIndex++;
                if(currentTokenEquals("{")) {
                    parseBlock(getBracketsEnd("{", "}"));
                } else {
                    parseBlock(currentIndex + 1);
                    currentIndex--;
                }
            } else if(currentTokenEquals("for") ||
                    currentTokenEquals("while") ||
                    currentTokenEquals("if")) {
                currentNestingLevel++;
                conditionalOperatorsAmount++;
                currentIndex++;
                currentIndex = getBracketsEnd("(", ")");
                currentIndex++;
                if(currentTokenEquals("{")) {
                    parseBlock(getBracketsEnd("{", "}"));
                } else {
                    parseBlock(currentIndex + 1);
                    currentIndex--;
                }
            }
            currentIndex++;
        }
        currentNestingLevel--;
    }

    private boolean currentTokenEquals(String check) {
        return tokens.get(currentIndex).value.equals(check);
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

//    private int getIfEnd(int ifIndex) {
//        int index = ifIndex;
//        int ifElseAmount = 0;
//        do {
//            index++;
//            switch (tokens.get(index).value) {
//                case "if":
//                    ifElseAmount++;
//                    break;
//                case "else":
//                    ifElseAmount--;
//                    break;
//                case "{":
//                    index = getBracketsEnd(index, "{", "}");
//            }
//        } while (!tokens.get(index).value.equals("else") || ifElseAmount != 0);
//        return index;
//    }
}
