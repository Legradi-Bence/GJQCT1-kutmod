package roboticoffee.utils.Nodes;

public class PrefixIncrementDecrementNode extends Node {

    private String variable;
    private String operator;

    public PrefixIncrementDecrementNode(String operator, String variable, int lineNumber) {
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
