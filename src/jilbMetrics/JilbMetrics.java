package jilbMetrics;

import halsteadMetrics.HalsteadMetricsCondensed;
import lexer.Lexer;
import lexer.Token;

import java.util.ArrayList;

public class JilbMetrics {
    Parser jilbParser;
    ArrayList<Token> tokens;
    public JilbMetrics(String rawText) {
        tokens = new Lexer(rawText).getTokens();
        jilbParser = new Parser(tokens);
    }

    public int getAbsoluteDifficulty() {
        return jilbParser.conditionalOperatorsAmount;
    }

    public double getRelativeDifficulty() {
        int conditionalOperatorAmount = getAbsoluteDifficulty();
        int operatorAmount = HalsteadMetricsCondensed.getOperatorsAmount(tokens);
        return (double) conditionalOperatorAmount / (double) operatorAmount;
    }

    public int getMaxNestingLevel() {
        return jilbParser.maxNestingLevel;
    }
}
