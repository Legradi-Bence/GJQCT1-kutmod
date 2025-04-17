package roboticoffee.utils.Nodes;

public class ExpressionStatementNode extends Node {
    private final Node expression;

    public ExpressionStatementNode(Node expression) {
        this.expression = expression;
    }

    public Node getExpression() {
        return expression;
    }
}
