package roboticoffee.utils.Nodes;

public class AssignmentNode extends Node {

    private String identifier;
    private String operator;
    private Node value;

    public AssignmentNode(String identifier, String operator, Node value) {
        this.identifier = identifier;
        this.operator = operator;
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getOperator() {
        return operator;
    }

    public Node getValue() {
        return value;
    }

}
