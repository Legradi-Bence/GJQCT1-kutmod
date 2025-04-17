package roboticoffee.utils.Nodes;

public class PrefixIncrementDecrementNode extends Node {

    private Node variable;
    private String operator;

    public PrefixIncrementDecrementNode(String operator, Node variable) {
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
