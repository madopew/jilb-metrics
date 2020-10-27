package jilbMetrics;

import halsteadMetrics.HalsteadMetricsCondensed;
import lexer.Token;

import java.util.ArrayList;

public class JilbMetrics {
    Parser jilbParser;
    ArrayList<Token> tokens;
    HalsteadMetricsCondensed halsteadMetricsCondensed;

    public JilbMetrics(String rawText) {
        halsteadMetricsCondensed = new HalsteadMetricsCondensed(rawText);
        tokens = halsteadMetricsCondensed.getTokens();
        jilbParser = new Parser(tokens);
    }

    public JilbMetrics(HalsteadMetricsCondensed hmc) {
        this.halsteadMetricsCondensed = hmc;
        tokens = hmc.getTokens();
        jilbParser = new Parser(tokens);
    }

    public int getAbsoluteDifficulty() {
        return jilbParser.conditionalOperatorsAmount;
    }

    public int getTotalOperatorAmount() {
        return halsteadMetricsCondensed.getOperatorsAmount();
    }

    public double getRelativeDifficulty() {
        int conditionalOperatorAmount = getAbsoluteDifficulty();
        return (double) conditionalOperatorAmount / getTotalOperatorAmount();
    }

    public int getMaxNestingLevel() {
        return jilbParser.maxNestingLevel - 1;
    }
}
