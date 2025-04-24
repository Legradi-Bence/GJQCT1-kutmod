package roboticoffee.utils.Nodes;

public class PrintNode extends Node {
    private Node expression;

    public PrintNode(Node expression, int lineNumber) {
        super(lineNumber);
        this.expression = expression;
    }

    public Node getExpression() {
        return expression;
    }
}
