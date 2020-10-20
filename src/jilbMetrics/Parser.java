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
            if(isConditional(tokens.get(currentIndex).value)) {
                currentNestingLevel++;
                if(!currentTokenEquals("else"))
                    conditionalOperatorsAmount++;
                //TODO if-else parsing
                int blockEnd = getTokenBlockEnd(currentIndex);
                currentIndex++;
                parseBlock(blockEnd);
            }
           /* if(currentTokenEquals("do")) {
                currentNestingLevel++;
                conditionalOperatorsAmount++;
                parseBlock(getOptionalBracketTokenEnd(currentIndex));
            } else if(currentTokenEquals("else")) {
                currentNestingLevel++;
                currentIndex++;
                if(currentTokenEquals("{")) {
                    parseBlock(getBracketsEndIndex(currentIndex, "{", "}"));
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
                currentIndex = getBracketsEndIndex(currentIndex, "(", ")");
                currentIndex++;
                if(currentTokenEquals("{")) {
                    parseBlock(getBracketsEndIndex(currentIndex, "{", "}"));
                } else {
                    parseBlock(currentIndex + 1);
                    currentIndex--;
                }
            }*/
            currentIndex++;
        }
        currentNestingLevel--;
    }

    /**
     * Returns {@code true} if token value at {@code currentIndex}
     * equals to {@code checkString}
     * @param checkString string to compare current token to
     * @return {@code true} if token equals to string
     */
    private boolean currentTokenEquals(String checkString) {
        return tokens.get(currentIndex).value.equals(checkString);
    }

    /**
     * Returns index of corresponding closing bracket.
     * {@code openingBracketIndex} should be at opening bracket of a block/piece of code.
     * @return index of closing bracket of the block/piece of code
     */
    private int getBracketsEndIndex(int openingBracketIndex, String openingBracket, String closingBracket) {
        int bracketsAmount = 0;
        do {
            String tokenValue = tokens.get(openingBracketIndex++).value;
            if(tokenValue.equals(openingBracket))
                bracketsAmount++;
            else if(tokenValue.equals(closingBracket))
                bracketsAmount--;
        } while(bracketsAmount != 0);
        openingBracketIndex--;
        return openingBracketIndex;
    }

    /**
     * Returns {@code true} if token is conditional
     * @param token token to check
     * @return {@code true} if token is conditional
     */
    private boolean isConditional(String token) {
        return " if else for do while ".contains(" " + token + " ");
    }

    /**
     * Returns index of the end of a block of a specified token.
     * @param tokenIndex token to find end of
     * @return index of the end of a block
     */
    private int getTokenBlockEnd(int tokenIndex) {
        tokenIndex++;
        if(tokens.get(tokenIndex).value.equals("(")) {
            tokenIndex = getBracketsEndIndex(tokenIndex, "(", ")");
            tokenIndex++;
        }
        if(!tokens.get(tokenIndex).value.equals("{"))
            return getOptionalBracketTokenEnd(tokenIndex);
        return getBracketsEndIndex(tokenIndex, "{", "}");
    }
    private int getOptionalBracketTokenEnd(int tokenIndex) {
        if(isConditional(tokens.get(tokenIndex).value))
            return getTokenBlockEnd(tokenIndex);
        return ++tokenIndex;
    }
}
