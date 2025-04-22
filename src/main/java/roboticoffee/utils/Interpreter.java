package roboticoffee.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javafx.application.Platform;
import roboticoffee.states.RobotState;
import roboticoffee.utils.Nodes.AssignmentNode;
import roboticoffee.utils.Nodes.BinaryExpressionNode;
import roboticoffee.utils.Nodes.BlockNode;
import roboticoffee.utils.Nodes.BooleanNode;
import roboticoffee.utils.Nodes.DeclarationNode;
import roboticoffee.utils.Nodes.ExpressionStatementNode;
import roboticoffee.utils.Nodes.ForStatementNode;
import roboticoffee.utils.Nodes.FunctionNode;
import roboticoffee.utils.Nodes.IdentifierNode;
import roboticoffee.utils.Nodes.IfStatementNode;
import roboticoffee.utils.Nodes.MoveStatementNode;
import roboticoffee.utils.Nodes.Node;
import roboticoffee.utils.Nodes.NumberNode;
import roboticoffee.utils.Nodes.PostfixIncrementDecrementNode;
import roboticoffee.utils.Nodes.PrefixIncrementDecrementNode;
import roboticoffee.utils.Nodes.ProgramNode;
import roboticoffee.utils.Nodes.StringNode;
import roboticoffee.utils.Nodes.TurnStatementNode;
import roboticoffee.utils.Nodes.WhileStatementNode;

public class Interpreter {
    private RobotState robotState;
    private CodeWindowControl codeWindowControl;
    private Stack<HashMap<String, Object>> scopeStack = new Stack<>();
    private final Map<String, Node> functionCache = new HashMap<>();
    private String functionName;
    private ProgramStatus programState = ProgramStatus.STOPPED;
    private long delay = 500;

    public Interpreter(String functionName, RobotState robotState, CodeWindowControl codeWindowControl) {
        this.codeWindowControl = codeWindowControl;
        this.functionName = functionName;
        this.robotState = robotState;
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

    private void setVariable(String name, Object value) {
        if (!scopeStack.isEmpty()) {
            HashMap<String, Object> currentScope = scopeStack.peek();
            if (currentScope.containsKey(name)) {
                throw new IllegalArgumentException("Variable '" + name + "' already exists in the current scope");
            }
            currentScope.put(name, value);
        } else {
            throw new IllegalStateException("No scope available to set variable: " + name);
        }
    }

    private Object getVariable(String name) {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            if (scopeStack.get(i).containsKey(name)) {
                return scopeStack.get(i).get(name);
            }
        }
        throw new IllegalArgumentException("Variable not found: " + name);
    }

    private void changeVariable(String name, Object value) {
        if (!scopeStack.isEmpty()) {

            for (int i = scopeStack.size() - 1; i >= 0; i--) {
                if (scopeStack.get(i).containsKey(name)) {
                    scopeStack.get(i).put(name, value);
                    return;
                }
            }
            throw new IllegalArgumentException("Variable not found: " + name);
        } else {
            throw new IllegalStateException("No scope available to set variable: " + name);
        }
    }

    public void execute(Node node) {

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
        } else {
            evaluate(node);
        }
    }

    private void execute(ProgramNode programNode) {
        enterScope();
        for (Node statement : programNode.getStatements()) {
            execute(statement);
        }
        exitScope();
    }

    private void execute(BlockNode blockNode) {
        enterScope();
        for (Node statement : blockNode.getStatements()) {
            execute(statement);
        }
        exitScope();
    }

    private void execute(FunctionNode functionNode) {
        enterScope();
        String functionName = functionNode.getFunctionName();

        if (functionName.equals(this.functionName)) {
            throw new IllegalArgumentException("Function cannot call itself: " + functionName);
        }
        Node functionBody = functionCache.get(functionName);
        if (functionBody == null) {
            if (!CodeWindowNames.getCodeWindowNames().contains(functionName)) {
                throw new IllegalArgumentException("Function not found: " + functionName);
            }

            String code = CodeWindowNames.getCode(functionName);

            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.tokenize();
            Parser parser = new Parser(tokens);
            ProgramNode programNode = parser.parse();

            functionCache.put(functionName, programNode);

            functionBody = programNode;
        }

        execute(functionBody);

        exitScope();
    }

    private void execute(AssignmentNode assignmentNode) {
        String variableName = assignmentNode.getIdentifier();
        Object value = evaluate(assignmentNode.getValue());
        changeVariable(variableName, value);
    }

    private void execute(DeclarationNode declarationNode) {
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
                            throw new ArithmeticException("Division by zero");
                        }
                        setVariable(variableName, currentNumber.doubleValue() / numberValue.doubleValue());
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported assignment operator: " + operator);
                }
                return;
            } else {
                throw new IllegalArgumentException("Variable is not a number: " + variableName);
            }
        } else {
            throw new IllegalArgumentException("Invalid assignment value: " + value);
        }
    }

    private void execute(TurnStatementNode turnStatementNode) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        robotState.turn(turnStatementNode.getDirection());
    }

    private void execute(MoveStatementNode moveStatementNode) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        if (evaluate(moveStatementNode.getDistance()) instanceof Number) {
            robotState.move(((Number) evaluate(moveStatementNode.getDistance())).intValue());
        }
        else {
            throw new IllegalArgumentException("Distance must be a number: " + moveStatementNode.getDistance());
        }
    }

    private void execute(ExpressionStatementNode expressionStatementNode) {
        execute(expressionStatementNode.getExpression());
    }

    private void execute(WhileStatementNode whileStatementNode) {
        enterScope();
        while ((boolean) evaluate(whileStatementNode.getCondition())) {
            execute(whileStatementNode.getBody());
        }
        exitScope();
    }

    private void execute(ForStatementNode forStatementNode) {
        enterScope();

        try {
            DeclarationNode declarationNode = (DeclarationNode) forStatementNode.getInitializer();
            evaluate(declarationNode);

            Object conditionResult = evaluate(forStatementNode.getCondition());
            if (!(conditionResult instanceof Boolean)) {
                throw new IllegalArgumentException("For loop condition must evaluate to a boolean");
            }

            while ((boolean) conditionResult) {
                execute(forStatementNode.getBody());
                evaluate(forStatementNode.getIterator());
                conditionResult = evaluate(forStatementNode.getCondition());
            }
        } finally {
            exitScope();
        }
    }

    private void execute(IfStatementNode ifStatementNode) {
        enterScope();
        if ((boolean) evaluate(ifStatementNode.getCondition())) {
            execute(ifStatementNode.getThenBlock());
        }
        // else if (ifStatementNode.getElseBlock() != null) {
        // execute(ifStatementNode.getElseBlock());
        // }
        exitScope();
    }

    private Object evaluate(PostfixIncrementDecrementNode postfixIncrementDecrementNode) {
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
            throw new IllegalArgumentException("Variable is not a number: " + variableName);
        }
    }

    private Object evaluate(PrefixIncrementDecrementNode prefixIncrementDecrementNode) {
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
            throw new IllegalArgumentException("Variable is not a number: " + variableName);
        }
    }

    private Object evaluate(DeclarationNode declarationNode) {
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
                            throw new ArithmeticException("Division by zero");
                        }
                        setVariable(variableName, currentNumber.doubleValue() / numberValue.doubleValue());
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported assignment operator: " + operator);
                }
                return getVariable(variableName);
            } else {
                throw new IllegalArgumentException("Variable is not a number: " + variableName);
            }
        } else {
            throw new IllegalArgumentException("Invalid assignment value: " + value);
        }
    }

    private Object evaluate(AssignmentNode assignmentNode) {
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
                            throw new ArithmeticException("Division by zero");
                        }
                        changeVariable(variableName, currentNumber.doubleValue() / numberValue.doubleValue());
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported assignment operator: " + operator);
                }
                return getVariable(variableName);
            } else {
                throw new IllegalArgumentException("Variable is not a number: " + variableName);
            }
        } else {
            throw new IllegalArgumentException("Invalid assignment value: " + value);
        }
    }

    private Object evaluate(BinaryExpressionNode binaryExpressionNode) {
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
                        throw new ArithmeticException("Division by zero");
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
                    throw new IllegalArgumentException("Unsupported operator: " + operator);
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
                    throw new IllegalArgumentException("Unsupported operator for boolean: " + operator);
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
                    throw new IllegalArgumentException("Unsupported operator for string: " + operator);
            }
        } else if (operator.equals("==") || operator.equals("!=")) {
            boolean isEqual = left.equals(right);
            return operator.equals("==") ? isEqual : !isEqual;
        } else {
            throw new IllegalArgumentException("Operands must be compatible types: " + left + ", " + right);
        }
    }

    private Object evaluate(IdentifierNode identifierNode) {
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

    private Object evaluate(Node node) {
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
        }
        else {
            throw new IllegalArgumentException("Unsupported node type: " + node.getClass().getName());
        }
    }
}
