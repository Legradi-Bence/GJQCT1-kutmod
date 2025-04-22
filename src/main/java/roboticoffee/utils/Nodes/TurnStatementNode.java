package roboticoffee.utils.Nodes;

public class TurnStatementNode extends Node {
    private final String direction;

    public TurnStatementNode(String direction, int lineNumber) {
        super(lineNumber);
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

}
