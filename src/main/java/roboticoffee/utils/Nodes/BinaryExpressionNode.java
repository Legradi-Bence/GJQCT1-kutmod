package roboticoffee.utils.Nodes;

public class BinaryExpressionNode extends Node {
    private Node left;
    private Node right;
    private String operator;

    public BinaryExpressionNode(Node left, String operator, Node right, int lineNumber) {
        super(lineNumber);
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }
    public String getOperator() {
        return operator;
    }

}
