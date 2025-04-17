package roboticoffee.utils.Nodes;

import java.util.ArrayList;
import java.util.List;

public class ProgramNode extends Node {

    private List<Node> statements;

    public ProgramNode() {
        this.statements = new ArrayList<>();
    }

    public void addStatement(Node statement) {
        statements.add(statement);
    }

    public List<Node> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        return "ProgramNode{" +
                "statements=" + statements +
                '}';
    }
}
