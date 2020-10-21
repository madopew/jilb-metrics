package jilbMetrics;

import lexer.Token;

import java.util.ArrayList;

class Parser {
    private final ArrayList<Token> tokens;
    private int currentIndex;
    int conditionalOperatorsAmount;
    int maxNestingLevel;

    {
        conditionalOperatorsAmount = 0;
        maxNestingLevel = 0;
        currentIndex = 0;
    }

    public Parser(ArrayList<Token> tokens) {
        this.tokens = DanglingElseParser.handleDanglingElse(tokens);
        parseBlock(this.tokens.size(), 0);
    }

    private void parseBlock(int endIndex, int currentNestingLevel) {
        if(currentNestingLevel > maxNestingLevel)
            maxNestingLevel = currentNestingLevel;
        while(currentIndex < endIndex) {
            if(currentTokenEquals("when")) {
                //TODO when handling
            } else if(ParserHelper.isConditional(tokens.get(currentIndex).value)) {
                if(!currentTokenEquals("else"))
                    conditionalOperatorsAmount++;
                int blockEnd = getTokenBlockEnd(currentIndex);
                currentIndex++;
                parseBlock(blockEnd, currentNestingLevel + 1);
                continue;
            }
            currentIndex++;
        }
    }

    /**
     * Returns index of corresponding closing bracket.
     * {@code openingBracketIndex} should be at opening bracket of a block/piece of code.
     * @param openingBracketIndex index of bracket, which corresponding bracket should be found
     * @param openingBracket opening bracket of a type
     * @param closingBracket closing bracket of a type
     * @return index of closing bracket of the block/piece of code
     */
    int getBracketsEndIndex(int openingBracketIndex, String openingBracket, String closingBracket) {
        return ParserHelper.getBracketsEndIndex(tokens, openingBracketIndex, openingBracket, closingBracket, 1);
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
        if(!tokens.get(tokenIndex).value.equals("{")) {
            return getOptionalBracketTokenEnd(tokenIndex);
        }
        return getBracketsEndIndex(tokenIndex, "{", "}");
    }
    private int getOptionalBracketTokenEnd(int tokenIndex) {
        if(tokens.get(tokenIndex).value.equals("if"))
            return getWholeIfBlockEnd(tokenIndex);
        if(ParserHelper.isConditional(tokens.get(tokenIndex).value))
            return getTokenBlockEnd(tokenIndex);
        return ++tokenIndex;
    }
    private int getWholeIfBlockEnd(int ifIndex) {
        ifIndex = getTokenBlockEnd(ifIndex);
        if(tokens.get(ifIndex + 1).value.equals("else"))
            return getTokenBlockEnd(ifIndex + 1);
        return ifIndex;
    }
}
