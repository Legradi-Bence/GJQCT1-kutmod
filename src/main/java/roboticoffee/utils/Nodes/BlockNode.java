package roboticoffee.utils.Nodes;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends Node {
    private final List<Node> statements;

    public BlockNode(int lineNumber) {
        super(lineNumber);
        this.statements = new ArrayList<>();
    }

    public void addStatement(Node statement) {
        statements.add(statement);
    }
    
    public List<Node> getStatements() {
        return statements;
    }

}
