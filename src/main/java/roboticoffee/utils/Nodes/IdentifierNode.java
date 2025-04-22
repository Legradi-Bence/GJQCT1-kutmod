package roboticoffee.utils.Nodes;

public class IdentifierNode extends Node {

    private String identifier;

    public IdentifierNode(String identifier, int lineNumber) {
        super(lineNumber);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }


}
