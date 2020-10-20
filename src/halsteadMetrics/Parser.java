package halsteadMetrics;

import halsteadMetrics.enums.Op;
import lexer.Token;
import lexer.enums.Type;

import java.util.ArrayList;

class Parser {
    private final ArrayList<Token> tokens;
    private final ArrayList<ArgumentToken> args;
    private int currentIndex = 0;
    public Parser(ArrayList<Token> tokens) {
        args = new ArrayList<>();
        this.tokens = new ArrayList<>(tokens);
        parse();
    }

    public ArrayList<ArgumentToken> getArgTokens() {
        return args;
    }

    private void parse() {
        while(currentIndex < tokens.size()) {
            readNext();
            currentIndex++;
        }
    }

    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        for (ArgumentToken arg : args) toReturn.append(arg.toString()).append("\n");
        return toReturn.toString();
    }

    public void readNext() {
        Token t = tokens.get(currentIndex);
        if(t.type == Type.HKW) {
            readHKW();
            return;
        }
        if(t.type == Type.SKW) {
            readSKW();
            return;
        }
        if(t.type == Type.VAR) {
            args.add(new ArgumentToken(Op.OPERAND, t.value));
            return;
        }
        if(t.type == Type.OP) {
            readOP();
            return;
        }
        if(t.type == Type.NUM || t.type == Type.CHAR || t.type == Type.STR) {
            args.add(new ArgumentToken(Op.OPERAND, t.value));
            return;
        }
        if(t.type == Type.PUNC) {
            readPunctuation();
            return;
        }
        System.err.printf("Cannot read character '%s' [%s]\n",t.type,t.value);
    }

    private void readHKW() {
        String value = tokens.get(currentIndex).value;
        if(value.equals("true") || value.equals("false") || value.equals("null")) {
            args.add(new ArgumentToken(Op.OPERAND, tokens.get(currentIndex).value));
            return;
        }
        if(value.equals("do")) {
            readDo();
            return;
        }
        if(value.equals("if")) {
            boolean isReturn = readIf();
            if(isReturn)
                return;
        }
        if(value.equals("else")) {
            readElse();
            return;
        }
        args.add(new ArgumentToken(Op.OPERATOR, tokens.get(currentIndex).value));
    }

    private void readDo() {
        int amount = 1;
        int temp = currentIndex;
        temp += 2;
        while(amount != 0) {
            if(tokens.get(temp).value.equals("{"))
                amount++;
            else if(tokens.get(temp).value.equals("}"))
                amount--;
            temp++;
        }
        tokens.remove(temp);
        tokens.remove(temp);
        args.add(new ArgumentToken(Op.OPERATOR, "do..while()"));
    }

    private boolean readIf() {
        int temp = currentIndex + 2;
        while(!tokens.get(temp).value.equals(")"))
            temp++;
        temp++;

        boolean isReturn = false;
        if(tokens.get(temp).value.equals("{"))
            isReturn = readIfElse(temp);
        return isReturn;
    }

    private boolean readIfElse(int index) {
        int temp = index+1;
        int amount = 1;
        while(amount != 0) {
            if(tokens.get(temp).value.equals("{"))
                amount++;
            else if(tokens.get(temp).value.equals("}"))
                amount--;
            temp++;
        }

        if(tokens.get(temp).value.equals("else")) {
            tokens.remove(temp);
            args.add(new ArgumentToken(Op.OPERATOR, "if()..else"));
            currentIndex++;
            return true;
        }
        return false;
    }

    private void readElse() {
        if(tokens.get(currentIndex + 1).value.equals("->")) {
            args.add(new ArgumentToken(Op.OPERATOR, "else"));
        } else {
            int temp = args.size() - 1;
            while(!args.get(temp).value.equals("if()"))
                temp--;
            args.get(temp).value += "..else";
        }
    }

    private void readSKW() {
        if(tokens.get(currentIndex).value.equals("catch")) {
            int index = args.size() - 1;
            while(!args.get(index).value.equals("try"))
                index--;
            args.get(index).value = "try..catch()";
            currentIndex++;
            return;
        }
        if(tokens.get(currentIndex).value.equals("finally")) {
            int index = args.size() - 1;
            while(!args.get(index).value.equals("try..catch()"))
                index--;
            args.get(index).value = "try..catch()..finally";
            return;
        }
        args.add(new ArgumentToken(Op.OPERATOR, tokens.get(currentIndex).value));
    }

    private void readOP() {
        if(tokens.get(currentIndex).value.equals(".") && tokens.get(currentIndex + 1).type == Type.NUM) {
            tokens.get(currentIndex + 1).value = '.' + tokens.get(currentIndex + 1).value;
            return;
        }
		/*if (tokens.get(currentIndex).value.equals(".")) {
			args.get(args.size() - 1).value += "." +  tokens.get(currentIndex + 1).value;
			currentIndex++;
			return;
		} */
        if (tokens.get(currentIndex).value.equals(">")) {
            int index = args.size() - 1;
            while(args.get(index).op != Op.OPERATOR || args.get(index).value.equals("<>"))
                index--;
            if(args.get(index).value.equals("<")) {
                args.get(index).value = "<>";
                return;
            }
        }
        args.add(new ArgumentToken(Op.OPERATOR, tokens.get(currentIndex).value));
    }

    private void readPunctuation() {
        if(tokens.get(currentIndex).value.equals("(") && (tokens.get(currentIndex - 1).type == Type.HKW || tokens.get(currentIndex - 1).type == Type.SKW)) {
            args.get(args.size() - 1).value += "()" ;
            return;
        }
        if(tokens.get(currentIndex).value.equals("(") && tokens.get(currentIndex - 1).type == Type.VAR) {
            args.get(args.size() - 1).value += "()" ;
            args.get(args.size() - 1).op = Op.OPERATOR ;
            return;
        }
        if(tokens.get(currentIndex).value.equals("(")) {
            args.add(new ArgumentToken(Op.OPERATOR, "()"));
            return;
        }
        if(tokens.get(currentIndex).value.equals("{")) {
            args.add(new ArgumentToken(Op.OPERATOR, "{}"));
            return;
        }
        if(tokens.get(currentIndex).value.equals("[")) {
            args.add(new ArgumentToken(Op.OPERATOR, "[]"));
        }
    }
}
