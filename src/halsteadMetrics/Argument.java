package halsteadMetrics;

public class Argument {
    public String value;
    public int amount;
    public Argument(String value) {
        this.value = value;
        this.amount = 1;
    }

    public void incAmount() {
        amount++;
    }
}
