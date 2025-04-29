package roboticoffee.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javafx.application.Platform;
import roboticoffee.states.PeopleState;
import roboticoffee.states.RobotState;
import roboticoffee.utils.Nodes.ArePeopleWaitingNode;
import roboticoffee.utils.Nodes.AssignmentNode;
import roboticoffee.utils.Nodes.BinaryExpressionNode;
import roboticoffee.utils.Nodes.BlockNode;
import roboticoffee.utils.Nodes.BooleanNode;
import roboticoffee.utils.Nodes.DeclarationNode;
import roboticoffee.utils.Nodes.ExpressionStatementNode;
import roboticoffee.utils.Nodes.ForStatementNode;
import roboticoffee.utils.Nodes.FunctionNode;
import roboticoffee.utils.Nodes.GetFirstOrderCoffeeNameNode;
import roboticoffee.utils.Nodes.GetFirstOrderTableNameNode;
import roboticoffee.utils.Nodes.GetRobotPosXNode;
import roboticoffee.utils.Nodes.GetRobotPosZNode;
import roboticoffee.utils.Nodes.IdentifierNode;
import roboticoffee.utils.Nodes.IfStatementNode;
import roboticoffee.utils.Nodes.MoveStatementNode;
import roboticoffee.utils.Nodes.Node;
import roboticoffee.utils.Nodes.NumberNode;
import roboticoffee.utils.Nodes.PlaceCoffeeNode;
import roboticoffee.utils.Nodes.PostfixIncrementDecrementNode;
import roboticoffee.utils.Nodes.PrefixIncrementDecrementNode;
import roboticoffee.utils.Nodes.PrintNode;
import roboticoffee.utils.Nodes.ProgramNode;
import roboticoffee.utils.Nodes.StringNode;
import roboticoffee.utils.Nodes.TakeCoffeeNode;
import roboticoffee.utils.Nodes.TakeOrderNode;
import roboticoffee.utils.Nodes.TurnStatementNode;
import roboticoffee.utils.Nodes.WhileStatementNode;

public class Interpreter {
    private RobotState robotState;
    private PeopleState peopleState;
    private CodeWindowControl codeWindowControl;
    private Stack<HashMap<String, Object>> scopeStack = new Stack<>();
    private final Map<String, Node> functionCache = new HashMap<>();
    private String functionName;
    private ProgramStatus programState = ProgramStatus.STOPPED;
    private long delay = 700;

    public Interpreter(String functionName, RobotState robotState, PeopleState peopleState, CodeWindowControl codeWindowControl) {
        this.codeWindowControl = codeWindowControl;
        this.functionName = functionName;
        this.robotState = robotState;
        this.peopleState = peopleState;
    }

    public void clearFunctionCache() {
        functionCache.clear();
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public ProgramStatus getProgramStatus() {
        return programState;
    }

    public void setProgramStatus(ProgramStatus state) {
        this.programState = state;
    }

    private void enterScope() {
        scopeStack.push(new HashMap<>());
    }

    private void exitScope() {
        scopeStack.pop();
    }

    private void setVariable(String name, Object value) throws InterpreterException {
        if (!scopeStack.isEmpty()) {
            HashMap<String, Object> currentScope = scopeStack.peek();
            if (currentScope.containsKey(name)) {
                throw new InterpreterException("Variable '" + name + "' already exists in the current scope");
            }
            currentScope.put(name, value);
        } else {
            throw new InterpreterException("No scope available to set variable: " + name);
        }
    }

    private Object getVariable(String name) throws InterpreterException {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            if (scopeStack.get(i).containsKey(name)) {
                return scopeStack.get(i).get(name);
            }
        }
        throw new InterpreterException("Variable not found: " + name);
    }

    private void changeVariable(String name, Object value) throws InterpreterException {
        if (!scopeStack.isEmpty()) {

            for (int i = scopeStack.size() - 1; i >= 0; i--) {
                if (scopeStack.get(i).containsKey(name)) {
                    scopeStack.get(i).put(name, value);
                    return;
                }
            }
            throw new InterpreterException("Variable not found: " + name);
        } else {
            throw new InterpreterException("No scope available to set variable: " + name);
        }
    }

    public void execute(Node node) throws InterpreterException {

        Platform.runLater(() -> codeWindowControl.highlightLine(node.getLineNumber()));

        if (programState == ProgramStatus.STOPPED) {
            scopeStack.clear();
            functionCache.clear();
            Thread.currentThread().interrupt();
            return;
        }

        while (programState == ProgramStatus.PAUSED) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        if (node instanceof ProgramNode) {
            execute((ProgramNode) node);
        } else if (node instanceof BlockNode) {
            execute((BlockNode) node);
        } else if (node instanceof TurnStatementNode) {
            execute((TurnStatementNode) node);
        } else if (node instanceof MoveStatementNode) {
            execute((MoveStatementNode) node);
        } else if (node instanceof ExpressionStatementNode) {
            execute((ExpressionStatementNode) node);
        } else if (node instanceof WhileStatementNode) {
            execute((WhileStatementNode) node);
        } else if (node instanceof ForStatementNode) {
            execute((ForStatementNode) node);
        } else if (node instanceof IfStatementNode) {
            execute((IfStatementNode) node);
        } else if (node instanceof DeclarationNode) {
            execute((DeclarationNode) node);
        } else if (node instanceof AssignmentNode) {
            execute((AssignmentNode) node);
        } else if (node instanceof FunctionNode) {
            execute((FunctionNode) node);
        } else if (node instanceof PlaceCoffeeNode) {
            execute((PlaceCoffeeNode) node);
        } else if (node instanceof PrintNode) {
            execute((PrintNode) node);
        } else if (node instanceof TakeCoffeeNode) {
            execute((TakeCoffeeNode) node);
        } else if (node instanceof TakeOrderNode) {
            execute((TakeOrderNode) node);
        } else {
            evaluate(node);
        }
    }

    private void execute(PlaceCoffeeNode placeOrderNode) {
        if (!peopleState.isAtTable(robotState.getHeadPosition())) {
            return;
        }
        Table table = peopleState.getTableByCoords(robotState.getHeadPosition());
        Order order = robotState.getOrderByTable(table);
        if (order == null) {
            return;
        } else if (order.getOrderedCoffee() == robotState.getInHand()) {
            robotState.setInHand(null);
            robotState.removeOrder(order);
            peopleState.leave(table);
            robotState.OnPrint("Order completed!");
        }
    }

    private void execute(PrintNode printNode) throws InterpreterException {
        String m = evaluate(printNode.getExpression()).toString();
        robotState.OnPrint(m);
    }

    private void execute(TakeCoffeeNode takeCoffeeNode) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        robotState.getCoffeeNumber();

    }

    private void execute(TakeOrderNode takeOrderNode) {
        if (!robotState.isAtCounter()) {
            return;
        }
        Person person;
        if ((person = peopleState.getFirstPerson()) != null) {
            robotState.addOrder(person.order());
            person.setTableDestiantion();
        }
    }

    private void execute(ProgramNode programNode) throws InterpreterException {
        enterScope();
        for (Node statement : programNode.getStatements()) {
            execute(statement);
        }
        exitScope();
    }

    private void execute(BlockNode blockNode) throws InterpreterException {
        enterScope();
        for (Node statement : blockNode.getStatements()) {
            execute(statement);
        }
        exitScope();
    }

    private void execute(FunctionNode functionNode) throws InterpreterException {
        enterScope();
        String functionName = functionNode.getFunctionName();

        if (functionName.equals(this.functionName)) {
            throw new InterpreterException("Function cannot call itself: " + functionName, functionNode.getLineNumber());
        }
        Node functionBody = functionCache.get(functionName);
        if (functionBody == null) {
            if (!CodeWindowNames.getCodeWindowNames().contains(functionName)) {
                throw new InterpreterException("Function not found: " + functionName, functionNode.getLineNumber());
            }

            String code = CodeWindowNames.getCode(functionName);

            ProgramNode programNode = null;
            try {
                Lexer lexer = new Lexer(code);
                List<Token> tokens = lexer.tokenize();
                Parser parser = new Parser(tokens);
                programNode = parser.parse();
            } catch (Exception e) {
                throw new InterpreterException("Failed to parse function: " + functionName, functionNode.getLineNumber());
            }
            if (programNode == null) {
                throw new InterpreterException("Failed to parse function: " + functionName, functionNode.getLineNumber());
            }
            functionCache.put(functionName, programNode);

            functionBody = programNode;
        }

        execute(functionBody);

        exitScope();
    }

    private void execute(AssignmentNode assignmentNode) throws InterpreterException {
        String variableName = assignmentNode.getIdentifier();
        Object value = evaluate(assignmentNode.getValue());
        changeVariable(variableName, value);
    }

    private void execute(DeclarationNode declarationNode) throws InterpreterException {
        String variableName = declarationNode.getIdentifier();
        Object value = evaluate(declarationNode.getValue());
        String operator = declarationNode.getOperator();

        if (operator.equals("=")) {
            setVariable(variableName, value);
            return;
        }
        if (value instanceof Number) {
            Number numberValue = (Number) value;
            Object currentValue = getVariable(variableName);

            if (currentValue instanceof Number) {
                Number currentNumber = (Number) currentValue;

                switch (operator) {
                    case "+=":
                        setVariable(variableName, currentNumber.doubleValue() + numberValue.doubleValue());
                        break;
                    case "-=":
                        setVariable(variableName, currentNumber.doubleValue() - numberValue.doubleValue());
                        break;
                    case "*=":
                        setVariable(variableName, currentNumber.doubleValue() * numberValue.doubleValue());
                        break;
                    case "/=":
                        if (numberValue.doubleValue() == 0) {
                            throw new InterpreterException("Division by zero", declarationNode.getLineNumber());
                        }
                        setVariable(variableName, currentNumber.doubleValue() / numberValue.doubleValue());
                        break;
                    default:
                        throw new InterpreterException("Unsupported assignment operator: " + operator, declarationNode.getLineNumber());
                }
                return;
            } else {
                throw new InterpreterException("Variable is not a number: " + variableName, declarationNode.getLineNumber());
            }
        } else {
            throw new InterpreterException("Invalid assignment value: " + value, declarationNode.getLineNumber());
        }
    }

    private void execute(TurnStatementNode turnStatementNode) {

        robotState.turn(turnStatementNode.getDirection());
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }

    private void execute(MoveStatementNode moveStatementNode) throws InterpreterException {
        if (evaluate(moveStatementNode.getDistance()) instanceof Number) {
            for (int i = 0; i < ((Number) evaluate(moveStatementNode.getDistance())).intValue(); i++) {
                robotState.move(1);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        } else {
            throw new InterpreterException("Distance must be a number: " + moveStatementNode.getDistance(), moveStatementNode.getLineNumber());
        }
    }

    private void execute(ExpressionStatementNode expressionStatementNode) throws InterpreterException {
        execute(expressionStatementNode.getExpression());
    }

    private void execute(WhileStatementNode whileStatementNode) throws InterpreterException {
        enterScope();
        while ((boolean) evaluate(whileStatementNode.getCondition())) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            execute(whileStatementNode.getBody());
        }
        exitScope();
    }

    private void execute(ForStatementNode forStatementNode) throws InterpreterException {
        enterScope();

        try {
            DeclarationNode declarationNode = (DeclarationNode) forStatementNode.getInitializer();
            evaluate(declarationNode);

            Object conditionResult = evaluate(forStatementNode.getCondition());
            if (!(conditionResult instanceof Boolean)) {
                throw new InterpreterException("For loop condition must evaluate to a boolean", forStatementNode.getLineNumber());
            }

            while ((boolean) conditionResult) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                execute(forStatementNode.getBody());
                evaluate(forStatementNode.getIterator());
                conditionResult = evaluate(forStatementNode.getCondition());
            }
        } finally {
            exitScope();
        }
    }

    private void execute(IfStatementNode ifStatementNode) throws InterpreterException {
        enterScope();
        if ((boolean) evaluate(ifStatementNode.getCondition())) {
            execute(ifStatementNode.getThenBlock());
        }
        exitScope();
    }

    private Object evaluate(PostfixIncrementDecrementNode postfixIncrementDecrementNode) throws InterpreterException {
        String variableName = postfixIncrementDecrementNode.getVariable();
        Object currentValue = getVariable(variableName);
        if (currentValue instanceof Number) {
            int oldValue = ((Number) currentValue).intValue();
            int newValue = oldValue;
            if (postfixIncrementDecrementNode.getOperator().equals("++")) {
                newValue++;
            } else if (postfixIncrementDecrementNode.getOperator().equals("--")) {
                newValue--;
            }
            changeVariable(variableName, newValue);
            return oldValue;
        } else {
            throw new InterpreterException("Variable is not a number: " + variableName, postfixIncrementDecrementNode.getLineNumber());
        }
    }

    private Object evaluate(PrefixIncrementDecrementNode prefixIncrementDecrementNode) throws InterpreterException {
        String variableName = prefixIncrementDecrementNode.getVariable();
        Object currentValue = getVariable(variableName);
        if (currentValue instanceof Number) {
            int newValue = ((Number) currentValue).intValue();
            if (prefixIncrementDecrementNode.getOperator().equals("++")) {
                newValue++;
            } else if (prefixIncrementDecrementNode.getOperator().equals("--")) {
                newValue--;
            }
            changeVariable(variableName, newValue);
            return newValue;
        } else {
            throw new InterpreterException("Variable is not a number: " + variableName, prefixIncrementDecrementNode.getLineNumber());
        }
    }

    private Object evaluate(DeclarationNode declarationNode) throws InterpreterException {
        String variableName = declarationNode.getIdentifier();
        Object value = evaluate(declarationNode.getValue());
        String operator = declarationNode.getOperator();

        if (operator.equals("=")) {
            setVariable(variableName, value);
            return value;
        }
        if (value instanceof Number) {
            Number numberValue = (Number) value;
            Object currentValue = getVariable(variableName);

            if (currentValue instanceof Number) {
                Number currentNumber = (Number) currentValue;

                switch (operator) {
                    case "+=":
                        setVariable(variableName, currentNumber.doubleValue() + numberValue.doubleValue());
                        break;
                    case "-=":
                        setVariable(variableName, currentNumber.doubleValue() - numberValue.doubleValue());
                        break;
                    case "*=":
                        setVariable(variableName, currentNumber.doubleValue() * numberValue.doubleValue());
                        break;
                    case "/=":
                        if (numberValue.doubleValue() == 0) {
                            throw new InterpreterException("Division by zero", declarationNode.getLineNumber());
                        }
                        setVariable(variableName, currentNumber.doubleValue() / numberValue.doubleValue());
                        break;
                    default:
                        throw new InterpreterException("Unsupported assignment operator: " + operator, declarationNode.getLineNumber());
                }
                return getVariable(variableName);
            } else {
                throw new InterpreterException("Variable is not a number: " + variableName, declarationNode.getLineNumber());
            }
        } else {
            throw new InterpreterException("Invalid assignment value: " + value, declarationNode.getLineNumber());
        }
    }

    private Object evaluate(AssignmentNode assignmentNode) throws InterpreterException {
        String variableName = assignmentNode.getIdentifier();
        Object value = evaluate(assignmentNode.getValue());
        String operator = assignmentNode.getOperator();

        if (operator.equals("=")) {
            changeVariable(variableName, value);
            return value;
        }
        if (value instanceof Number) {
            Number numberValue = (Number) value;
            Object currentValue = getVariable(variableName);

            if (currentValue instanceof Number) {
                Number currentNumber = (Number) currentValue;

                switch (operator) {
                    case "+=":
                        changeVariable(variableName, currentNumber.doubleValue() + numberValue.doubleValue());
                        break;
                    case "-=":
                        changeVariable(variableName, currentNumber.doubleValue() - numberValue.doubleValue());
                        break;
                    case "*=":
                        changeVariable(variableName, currentNumber.doubleValue() * numberValue.doubleValue());
                        break;
                    case "/=":
                        if (numberValue.doubleValue() == 0) {
                            throw new InterpreterException("Division by zero", assignmentNode.getLineNumber());
                        }
                        changeVariable(variableName, currentNumber.doubleValue() / numberValue.doubleValue());
                        break;
                    default:
                        throw new InterpreterException("Unsupported assignment operator: " + operator, assignmentNode.getLineNumber());
                }
                return getVariable(variableName);
            } else {
                throw new InterpreterException("Variable is not a number: " + variableName, assignmentNode.getLineNumber());
            }
        } else {
            throw new InterpreterException("Invalid assignment value: " + value, assignmentNode.getLineNumber());
        }
    }

    private Object evaluate(BinaryExpressionNode binaryExpressionNode) throws InterpreterException {
        Object left = evaluate(binaryExpressionNode.getLeft());
        Object right = evaluate(binaryExpressionNode.getRight());
        String operator = binaryExpressionNode.getOperator();

        if (left instanceof Number && right instanceof Number) {
            int leftValue = ((Number) left).intValue();
            int rightValue = ((Number) right).intValue();

            switch (operator) {
                case "+":
                    return leftValue + rightValue;
                case "-":
                    return leftValue - rightValue;
                case "*":
                    return leftValue * rightValue;
                case "/":
                    if (rightValue == 0) {
                        throw new InterpreterException("Division by zero", binaryExpressionNode.getLineNumber());
                    }
                    return leftValue / rightValue;
                case "%":
                    return leftValue % rightValue;
                case "==":
                    return leftValue == rightValue;
                case "!=":
                    return leftValue != rightValue;
                case ">":
                    return leftValue > rightValue;
                case "<":
                    return leftValue < rightValue;
                case ">=":
                    return leftValue >= rightValue;
                case "<=":
                    return leftValue <= rightValue;
                default:
                    throw new InterpreterException("Unsupported operator: " + operator, binaryExpressionNode.getLineNumber());
            }
        } else if (left instanceof Boolean && right instanceof Boolean) {
            boolean leftValue = (Boolean) left;
            boolean rightValue = (Boolean) right;

            switch (operator) {
                case "&&":
                    return leftValue && rightValue;
                case "||":
                    return leftValue || rightValue;
                case "==":
                    return leftValue == rightValue;
                case "!=":
                    return leftValue != rightValue;
                default:
                    throw new InterpreterException("Unsupported operator for boolean: " + operator, binaryExpressionNode.getLineNumber());
            }
        } else if (left instanceof String && right instanceof String) {
            String leftValue = (String) left;
            String rightValue = (String) right;

            switch (operator) {
                case "+":
                    return leftValue + rightValue;
                case "==":
                    return leftValue.equals(rightValue);
                case "!=":
                    return !leftValue.equals(rightValue);
                default:
                    throw new InterpreterException("Unsupported operator for string: " + operator, binaryExpressionNode.getLineNumber());
            }
        } else if (operator.equals("==") || operator.equals("!=")) {
            boolean isEqual = left.equals(right);
            return operator.equals("==") ? isEqual : !isEqual;
        } else {
            throw new InterpreterException("Operands must be compatible types: " + left + ", " + right, binaryExpressionNode.getLineNumber());
        }
    }

    private Object evaluate(IdentifierNode identifierNode) throws InterpreterException {
        String variableName = identifierNode.getIdentifier();
        return getVariable(variableName);
    }

    private Object evaluate(NumberNode numberNode) {
        return numberNode.getValue();
    }

    private Object evaluate(BooleanNode booleanNode) {
        return booleanNode.getValue();
    }

    private Object evaluate(StringNode stringNode) {
        return stringNode.getValue();
    }

    private Object evaluate(ArePeopleWaitingNode arePeopleWaitingNode) {
        return robotState.arePeopleWaiting();

    }

    private Object evaluate(GetRobotPosXNode getRobotPosXNode) {
        return robotState.getRobotPosX();

    }

    private Object evaluate(GetRobotPosZNode getRobotPosZNode) {
        return robotState.getRobotPosZ();

    }

    private Object evaluate(GetFirstOrderCoffeeNameNode getFirstOrderCoffeeNameNode) {
        return robotState.getFirstOrderCoffeeName();
    }

    private Object evaluate(GetFirstOrderTableNameNode getFirstOrderTableNameNode) {
        return robotState.getFirstOrderTableName();
    }

    private Object evaluate(Node node) throws InterpreterException {
        if (node instanceof NumberNode) {
            return evaluate((NumberNode) node);
        } else if (node instanceof IdentifierNode) {
            return evaluate((IdentifierNode) node);
        } else if (node instanceof BinaryExpressionNode) {
            return evaluate((BinaryExpressionNode) node);
        } else if (node instanceof DeclarationNode) {
            return evaluate((DeclarationNode) node);
        } else if (node instanceof AssignmentNode) {
            return evaluate((AssignmentNode) node);
        } else if (node instanceof PrefixIncrementDecrementNode) {
            return evaluate((PrefixIncrementDecrementNode) node);
        } else if (node instanceof PostfixIncrementDecrementNode) {
            return evaluate((PostfixIncrementDecrementNode) node);
        } else if (node instanceof BooleanNode) {
            return evaluate((BooleanNode) node);
        } else if (node instanceof StringNode) {
            return evaluate((StringNode) node);
        } else if (node instanceof ArePeopleWaitingNode) {
            return evaluate((ArePeopleWaitingNode) node);
        } else if (node instanceof GetRobotPosXNode) {
            return evaluate((GetRobotPosXNode) node);
        } else if (node instanceof GetRobotPosZNode) {
            return evaluate((GetRobotPosZNode) node);
        } else if (node instanceof GetFirstOrderCoffeeNameNode) {
            return evaluate((GetFirstOrderCoffeeNameNode) node);
        } else if (node instanceof GetFirstOrderTableNameNode) {
            return (evaluate((GetFirstOrderTableNameNode) node));
        } else {
            throw new InterpreterException("Unsupported node type: " + node.getClass().getName(), node.getLineNumber());
        }
    }
}
