package roboticoffee.utils.Nodes;

public class IfStatementNode extends Node {

    private Node condition;
    private Node thenBlock;

    public IfStatementNode(Node condition, Node thenNode) {
        this.condition = condition;
        this.thenBlock = thenNode;
    }

    public Node getCondition() {
        return condition;
    }

    public Node getThenBlock() {
        return thenBlock;
    }

}
