package roboticoffee.utils.Nodes;

public class FunctionNode extends Node {
    private String functionName;
    
    public FunctionNode(String functionName) {
        this.functionName = functionName;
    }
    public String getFunctionName() {
        return functionName;
    }    
}
