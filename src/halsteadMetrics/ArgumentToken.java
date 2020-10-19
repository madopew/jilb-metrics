package halsteadMetrics;

import halsteadMetrics.enums.Op;

class ArgumentToken {
    public Op op;
    public String value;
    public ArgumentToken(Op op, String value) {
        this.op = op;
        this.value = value;
    }

    public String toString() {
        return "{type: " + op + ", value: " + value + "}";
    }
}
