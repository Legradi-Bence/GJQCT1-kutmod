package roboticoffee.utils.Nodes;

public class WhileStatementNode extends Node {
    private final Node condition;
    private final Node body;

    public WhileStatementNode(Node condition, Node body) {
        this.condition = condition;
        this.body = body;
    }

    public Node getCondition() {
        return condition;
    }

    public Node getBody() {
        return body;
    }

}
