package lexer;

import lexer.enums.Type;

public class Token {
    public Type type;
    public String value;
    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return "{type: " + type + ", value: " + value + "}";
    }
}
