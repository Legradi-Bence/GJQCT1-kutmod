package roboticoffee.utils.Nodes;

public class FunctionNode extends Node {
    private String functionName;
    
    public FunctionNode(String functionName, int lineNumber) {
        super(lineNumber);
        this.functionName = functionName;
    }
    public String getFunctionName() {
        return functionName;
    }    
}
