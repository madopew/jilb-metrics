package jilbMetrics;

import lexer.Token;
import lexer.enums.Type;

import java.util.ArrayList;
import java.util.HashSet;

class DanglingElseParser {
    /**
     * Handles dangling else problem by inserting corresponding curly brackets
     * after each if and before each else, accordingly
     * @param tokens list of tokens
     * @return list of tokens without handling else
     */
    static ArrayList<Token> handleDanglingElse(ArrayList<Token> tokens) {
        HashSet<Integer> unmatchedIfSet = new HashSet<>();
        tokens = new ArrayList<>(tokens);
        for(int i = 0; i < tokens.size(); i++) {
            if(tokens.get(i).value.equals("if")) {
                unmatchedIfSet.add(i);
            } else if(tokens.get(i).value.equals("else")) {
                if(tokens.get(i + 1).value.equals("->"))
                    continue;
                int tokenIndex = getCorrespondingToElseToken(tokens, unmatchedIfSet, i);
                unmatchedIfSet.remove(tokenIndex);
                tokenIndex = ParserHelper.getBracketsEndIndex(tokens, tokenIndex+1, "(", ")", 1) + 1;
                if(tokens.get(tokenIndex).value.equals("{"))
                    continue;
                tokens.add(tokenIndex, new Token(Type.PUNC, "{"));
                i++;
                tokens.add(i, new Token(Type.PUNC, "}"));
                i++;
            }
        }
        return tokens;
    }
    private static int getCorrespondingToElseToken(ArrayList<Token> tokens, HashSet<Integer> unmatchedIfSet, int elseIndex) {
        int j = elseIndex - 1;
        while(!unmatchedIfSet.contains(j)) {
            if(tokens.get(j).value.equals("}"))
                j = ParserHelper.getBracketsEndIndex(tokens, j, "{", "}", -1);
            j--;
        }
        return j;
    }
}
