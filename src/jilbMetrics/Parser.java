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

    /**
     * Parses single block in curly brackets recursively,
     * changing global variables {@code maxNestingLevel} and
     * {@code conditionalOperatorsAmount}
     * @param endIndex index of the end of the block
     * @param currentNestingLevel nesting level of a block
     */
    private void parseBlock(int endIndex, int currentNestingLevel) {
        updateMaxNestingLevel(currentNestingLevel);
        while(currentIndex < endIndex) {
            String currentToken = tokens.get(currentIndex).value;
            if(ParserHelper.isConditional(tokens, currentIndex)) {
                updateConditionalOperatorAmount(currentToken);
                int blockEnd = getTokenBlockEnd(currentIndex);
                currentIndex++;
                if ("when".equals(currentToken))
                    parseWhenBlock(blockEnd, currentNestingLevel);
                else
                    parseBlock(blockEnd, currentNestingLevel + 1);
                adjustDoBlock(currentToken);
                continue;
            }
            currentIndex++;
        }
    }
    private void parseWhenBlock(int endIndex, int currentNestingLevel) {
        updateMaxNestingLevel(currentNestingLevel);
        while(currentIndex < endIndex) {
            if(ParserHelper.isLambdaInWhen(tokens, currentIndex)) {
                int blockEnd = getTokenBlockEnd(currentIndex);
                if (!tokens.get(currentIndex - 1).value.equals("else")) {
                    conditionalOperatorsAmount++;
                    currentNestingLevel++;
                }
                currentIndex++;
                parseBlock(blockEnd, currentNestingLevel);
                continue;
            }
            currentIndex++;
        }
    }
    private void adjustDoBlock(String currentToken) {
        if(currentToken.equals("do"))
            currentIndex += 2;
    }

    /**
     * Updates {@code maxNestingLevel} if {@code nestingLevel} is
     * higher
     * @param nestingLevel nestingLevel to check
     */
    private void updateMaxNestingLevel(int nestingLevel) {
        if(nestingLevel > maxNestingLevel)
            maxNestingLevel = nestingLevel;
    }

    /**
     * Updates {@code conditionalOperatorsAmount} if {@code token} is
     * conditional (except for else and when)
     * @param token token to check
     */
    private void updateConditionalOperatorAmount(String token) {
        if(!token.equals("else") && !token.equals("when"))
            conditionalOperatorsAmount++;
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
        if(ParserHelper.isConditional(tokens, tokenIndex))
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
