package roboticoffee.utils.Nodes;

public class StringNode extends Node {
    public String value;

    public StringNode(String value, int lineNumber) {
        super(lineNumber);
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
