package roboticoffee.utils.Nodes;

public class NumberNode extends Node {
    private final int value;

    public NumberNode(String value, int lineNumber) {
        super(lineNumber);
        this.value = Integer.parseInt(value);
    }

    public NumberNode(int value, int lineNumber) {
        super(lineNumber);
        this.value = value;
    }
    public int getValue() {
        return value;
    }

}
