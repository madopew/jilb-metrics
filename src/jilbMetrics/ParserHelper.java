package jilbMetrics;

import jilbMetrics.exceptions.DirectionNotCorrectException;
import lexer.Token;

import java.util.ArrayList;

public class ParserHelper {
    /**
     * Returns index of corresponding bracket.
     * If {@code direction} is equal to +1, searches corresponding bracket <b>after</b> {@code openingBracketIndex},
     * else if {@code direction} is equal to +1, searches corresponding bracket <b>before</b> {@code openingBracketIndex}
     * @param tokens list of tokens to search brackets in
     * @param openingBracketIndex index of bracket, which corresponding bracket should be found
     * @param openingBracket opening bracket of a type
     * @param closingBracket closing bracket of a type
     * @param direction direction: +1 - forward; -1 - backwards
     * @return index of corresponding bracket
     */
    static int getBracketsEndIndex(ArrayList<Token> tokens, int openingBracketIndex,
                                           String openingBracket, String closingBracket,
                                           int direction) {
        if(direction != 1 && direction != -1)
            throw new DirectionNotCorrectException();
        int bracketsAmount = 0;
        do {
            String tokenValue = tokens.get(openingBracketIndex).value;
            openingBracketIndex += direction;
            if(tokenValue.equals(openingBracket))
                bracketsAmount++;
            else if(tokenValue.equals(closingBracket))
                bracketsAmount--;
        } while(bracketsAmount != 0);
        openingBracketIndex -= direction;
        return openingBracketIndex;
    }

    /**
     * Returns {@code true} if token at index is conditional
     * @param tokens list of tokens
     * @param index index of the token
     * @return {@code true} if token is conditional
     */
    static boolean isConditional(ArrayList<Token> tokens, int index) {
        String token = tokens.get(index).value;
        return " if else for do while when ".contains(" " + token + " ");
    }

    /**
     * Returns {@code true} if it's lambda and it corresponds to {@code when}
     * @param tokens list of tokens
     * @param lambdaIndex index of lambda symbol
     * @return {@code true} if lambda corresponds to {@code when}
     */
    static boolean isLambdaInWhen(ArrayList<Token> tokens, int lambdaIndex) {
        if(!tokens.get(lambdaIndex).value.equals("->"))
            return false;
        while(!tokens.get(lambdaIndex).value.equals("when")) {
            lambdaIndex--;
            switch (tokens.get(lambdaIndex).value) {
                case ")":
                    lambdaIndex = getBracketsEndIndex(tokens, lambdaIndex, "(", ")", -1);
                    break;
                case "}":
                    lambdaIndex = getBracketsEndIndex(tokens, lambdaIndex, "{", "}", -1);
                    break;
                case "(":
                    return false;
            }
        }
        return true;
    }
}
