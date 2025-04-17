package roboticoffee.utils.Nodes;

public class ForStatementNode extends Node {
    private Node initializer;
    private Node condition;
    private Node iterator;
    private Node body;

    public ForStatementNode(Node initializer, Node condition, Node iterator, Node body) {
        this.initializer = initializer;
        this.condition = condition;
        this.iterator = iterator;
        this.body = body;
    }

    public Node getInitializer() {
        return initializer;
    }

    public Node getCondition() {
        return condition;
    }

    public Node getIterator() {
        return iterator;
    }

    public Node getBody() {
        return body;
    }
    

}
