package roboticoffee.utils.Nodes;

public  class BooleanNode extends Node{
    private boolean value;

    public BooleanNode(boolean value, int lineNumber) {
        super(lineNumber);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    
}
