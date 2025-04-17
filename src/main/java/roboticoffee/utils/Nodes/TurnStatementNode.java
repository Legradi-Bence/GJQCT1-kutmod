package roboticoffee.utils.Nodes;

public class TurnStatementNode extends Node {
    private final Node direction;

    public TurnStatementNode(Node direction) {
        this.direction = direction;
    }

    public Node getDirection() {
        return direction;
    }

}
