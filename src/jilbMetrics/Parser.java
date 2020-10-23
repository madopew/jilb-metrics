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
        //parseBlock(this.tokens.size(), 0);
        parse(this.tokens.size(), 0, ParserHelper::isConditional, this::prepareBlock);
    }

    @FunctionalInterface
    private interface BlockProcessingCondition {
        boolean verify(ArrayList<Token> tokens, int currentIndex);
    }

    @FunctionalInterface
    private interface BlockProcessingPreparation {
        int prepare(int currentIndex, int currentNestingLevel);
    }

    private void parse(int endIndex, int currentNestingLevel,
                       BlockProcessingCondition bpc, BlockProcessingPreparation bpp) {
        updateMaxNestingLevel(currentNestingLevel);
        while(currentIndex < endIndex) {
            String currentToken = tokens.get(currentIndex).value;
            if(bpc.verify(tokens, currentIndex)) {
                currentNestingLevel = bpp.prepare(currentIndex, currentNestingLevel);
                int blockEnd = getTokenBlockEnd(currentIndex);
                currentIndex++;
                if (currentToken.equals("when")) {
                    parse(blockEnd, currentNestingLevel, ParserHelper::isLambdaInWhen, this::prepareWhen);
                } else {
                    parse(blockEnd, currentNestingLevel, ParserHelper::isConditional, this::prepareBlock);
                    adjustDoBlock(currentToken);
                }
                continue;
            }
            currentIndex++;
        }
    }
    private void adjustDoBlock(String currentToken) {
        if(currentToken.equals("do"))
            currentIndex += 2;
    }

    /*
     * Parses single block in curly brackets recursively,
     * changing global variables {@code maxNestingLevel} and
     * {@code conditionalOperatorsAmount}
     * @param endIndex index of the end of the block
     * @param currentNestingLevel nesting level of a block
     */
   /* private void parseBlock(int endIndex, int currentNestingLevel) {
        updateMaxNestingLevel(currentNestingLevel);
        while(currentIndex < endIndex) {
            String currentToken = tokens.get(currentIndex).value;
            if(ParserHelper.isConditional(tokens, currentIndex)) {
                updateConditionalOperatorAmount(currentIndex);
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
                if (!tokens.get(currentIndex - 1).value.equals("else")) {
                    conditionalOperatorsAmount++;
                    currentNestingLevel++;
                }
                int blockEnd = getTokenBlockEnd(currentIndex);
                currentIndex++;
                parseBlock(blockEnd, currentNestingLevel);
                continue;
            }
            currentIndex++;
        }
    }*/

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
     * Updates {@code conditionalOperatorsAmount} if token at {@code index} is
     * conditional (except for else and when)
     * @param index index of token to check
     */
    private int prepareBlock(int index, int currentNestingLevel) {
        String token = tokens.get(index).value;
        if(!token.equals("else") && !token.equals("when"))
            conditionalOperatorsAmount++;
        if(!token.equals("when"))
            currentNestingLevel++;
        return currentNestingLevel;
    }
    private int prepareWhen(int index, int currentNestingLevel) {
        if (!tokens.get(index - 1).value.equals("else")) {
            conditionalOperatorsAmount++;
            currentNestingLevel++;
        }
        return currentNestingLevel;
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
