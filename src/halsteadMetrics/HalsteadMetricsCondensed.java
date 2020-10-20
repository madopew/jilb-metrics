package halsteadMetrics;

import halsteadMetrics.enums.Op;
import lexer.Token;

import java.util.ArrayList;

public class HalsteadMetricsCondensed {
    public static int getOperatorsAmount(ArrayList<Token> tokens) {
        Parser p = new Parser(tokens);
        ArrayList<ArgumentToken> argTokens = p.getArgTokens();
        ArrayList<Argument> argOperators = new ArrayList<>();
        int n1 = 0;
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
        for(Argument a : argOperators)
            n1 += a.amount;
        return n1;
    }

    private static int getIndexOf(ArrayList<Argument> args, ArgumentToken at) {
        for(int i = 0; i < args.size(); i++) {
            if(at.value.equals(args.get(i).value))
                return i;
        }
        return -1;
    }
}
