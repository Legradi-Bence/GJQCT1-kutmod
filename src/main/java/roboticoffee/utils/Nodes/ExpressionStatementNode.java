package roboticoffee.utils.Nodes;

public class ExpressionStatementNode extends Node {
    private final Node expression;

    public ExpressionStatementNode(Node expression, int lineNumber) {
        super(lineNumber);
        this.expression = expression;
    }

    public Node getExpression() {
        return expression;
    }
}
