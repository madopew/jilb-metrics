package halsteadMetrics;

import halsteadMetrics.enums.Op;
import lexer.Lexer;
import lexer.Token;

import java.util.ArrayList;

public class HalsteadMetricsCondensed {

    private Lexer lexer;
    private Parser parser;

    public HalsteadMetricsCondensed(String rawText) {
        this.lexer = new Lexer(rawText);
        this.parser = new Parser(lexer.getTokens());
    }

    public int getOperatorsAmount() {
        ArrayList<Argument> argOperators = getOperators();
        int n1 = 0;
        for(Argument a : argOperators)
            n1 += a.amount;
        return n1;
    }

    public ArrayList<Token> getTokens() {
        return lexer.getTokens();
    }

    public ArrayList<Argument> getOperators() {
        ArrayList<ArgumentToken> argTokens = parser.getArgTokens();
        ArrayList<Argument> argOperators = new ArrayList<>();
        argTokens.forEach(at -> {
            if(at.op != Op.OPERATOR)
                return;
            int index = getIndexOf(argOperators, at);
            if(index == -1) {
                argOperators.add(new Argument(at.value));
            } else {
                argOperators.get(index).incAmount();
            }
        });
        return argOperators;
    }

    private int getIndexOf(ArrayList<Argument> args, ArgumentToken at) {
        for(int i = 0; i < args.size(); i++) {
            if(at.value.equals(args.get(i).value))
                return i;
        }
        return -1;
    }

}
