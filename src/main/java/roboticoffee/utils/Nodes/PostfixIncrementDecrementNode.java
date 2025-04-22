package roboticoffee.utils.Nodes;

public class PostfixIncrementDecrementNode extends Node {
    private String variable;
    private String operator;

    public PostfixIncrementDecrementNode(String variable, String operator, int lineNumber) {
        super(lineNumber);
        this.variable = variable;
        this.operator = operator;
    }

    public String getVariable() {
        return variable;
    }

    public String getOperator() {
        return operator;
    }

}
