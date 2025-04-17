package roboticoffee.utils.Nodes;

public class PostfixIncrementDecrementNode extends Node {
    private Node variable;
    private String operator;

    public PostfixIncrementDecrementNode(Node variable, String operator) {
        this.variable = variable;
        this.operator = operator;
    }

    public Node getVariable() {
        return variable;
    }

    public String getOperator() {
        return operator;
    }

}
