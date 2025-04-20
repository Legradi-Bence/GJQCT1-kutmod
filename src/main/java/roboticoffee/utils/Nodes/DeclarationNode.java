package roboticoffee.utils.Nodes;

public class DeclarationNode extends Node {

    private String type;
    private String identifier;
    private String operator;
    private Node value;

    public DeclarationNode(String type, String identifier, String operator, Node value) {
        this.type = type;
        this.identifier = identifier;
        this.operator = operator;
        this.value = value;
    }

    public String getType() {
        return type;
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
