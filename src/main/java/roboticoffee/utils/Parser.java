package roboticoffee.utils;

import java.util.ArrayList;
import java.util.List;

import roboticoffee.utils.Nodes.DeclarationNode;
import roboticoffee.utils.Nodes.ArePeopleWaitingNode;
import roboticoffee.utils.Nodes.AssignmentNode;
import roboticoffee.utils.Nodes.BinaryExpressionNode;
import roboticoffee.utils.Nodes.BlockNode;
import roboticoffee.utils.Nodes.BooleanNode;
import roboticoffee.utils.Nodes.ExpressionStatementNode;
import roboticoffee.utils.Nodes.ForStatementNode;
import roboticoffee.utils.Nodes.FunctionNode;
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

public class Parser {
    private List<Token> tokens;
    private int position;
    private int line = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public ProgramNode parse() {
        ProgramNode programNode = new ProgramNode(0);
        while (!isAtEnd()) {
            programNode.addStatement(parseStatement());
        }
        return programNode;
    }

    private Node parseStatement() {
        Token current = peek();
        if (current.getType() == TokenType.KEYWORD) {
            switch (current.getValue()) {
                case "int", "string", "boolean":
                    return parseAssignment();
                case "if":
                    return parseIfStatement();
                case "for":
                    return parseForStatement();
                case "while":
                    return parseWhileStatement();
                case "move":
                    return parseMoveStatement();
                case "turn":
                    return parseTurnStatement();
                case "function":
                    return parseFunction();
                case "takeOrder":
                    return parseTakeOrder();
                case "placeOrder":
                    return parsePlaceOrder();
                case "takeCoffee":
                    return parseTakeCoffee();
                case "print":
                    return parsePrint();
                default:
                    throw new IllegalArgumentException("Unexpected keyword: " + current.getValue());
            }
        } else {
            return parseExpressionStatement();
        }
    }

    private Node parseGetRobotPosZ() {
        consume(TokenType.KEYWORD, "getRobotPosZ");
        consume(TokenType.OPEN_PAREN, "(");
        consume(TokenType.CLOSE_PAREN, ")");
        return new GetRobotPosZNode(line);
    }

    private Node parseGetRobotPosX() {
        consume(TokenType.KEYWORD, "getRobotPosX");
        consume(TokenType.OPEN_PAREN, "(");
        consume(TokenType.CLOSE_PAREN, ")");
        return new GetRobotPosXNode(line);
    }

    private Node parsePrint() {
        consume(TokenType.KEYWORD, "print");
        consume(TokenType.OPEN_PAREN, "(");
        Node expression = parseExpression();
        consume(TokenType.CLOSE_PAREN, ")");
        consume(TokenType.SEMICOLON, ";");
        return new PrintNode(expression, line);
    }

    private Node parseArePeopleWaiting() {
        consume(TokenType.KEYWORD, "arePeopleWaiting");
        consume(TokenType.OPEN_PAREN, "(");
        consume(TokenType.CLOSE_PAREN, ")");
        return new ArePeopleWaitingNode(line);
    }

    private Node parseTakeCoffee() {
        consume(TokenType.KEYWORD, "takeCoffee");
        consume(TokenType.OPEN_PAREN, "(");
        consume(TokenType.CLOSE_PAREN, ")");
        consume(TokenType.SEMICOLON, ";");
        return new TakeCoffeeNode(line);
    }

    private Node parsePlaceOrder() {
        consume(TokenType.KEYWORD, "placeOrder");
        consume(TokenType.OPEN_PAREN, "(");
        consume(TokenType.CLOSE_PAREN, ")");
        consume(TokenType.SEMICOLON, ";");
        return new PlaceCoffeeNode(line);
    }

    private Node parseTakeOrder() {
        consume(TokenType.KEYWORD, "takeOrder");
        consume(TokenType.OPEN_PAREN, "(");
        consume(TokenType.CLOSE_PAREN, ")");
        consume(TokenType.SEMICOLON, ";");
        return new TakeOrderNode(line);
    }

    private Node parseExpressionStatement() {
        Node expression = parseExpression();
        consume(TokenType.SEMICOLON, ";");
        if (expression instanceof BinaryExpressionNode) {
            BinaryExpressionNode binaryExpressionNode = (BinaryExpressionNode) expression;
            if (binaryExpressionNode.getOperator().equals("=")) {

                return new AssignmentNode(((IdentifierNode) binaryExpressionNode.getLeft()).getIdentifier(), "=", binaryExpressionNode.getRight(), line);
            }
        }
        return new ExpressionStatementNode(expression, line);
    }

    private Node parseFunction() {
        consume(TokenType.KEYWORD, "function");
        Token functionName = consume(TokenType.IDENTIFIER, null);
        consume(TokenType.SEMICOLON, ";");
        return new FunctionNode(functionName.getValue(), line);
    }

    private Node parseWhileStatement() {
        consume(TokenType.KEYWORD, "while");
        consume(TokenType.OPEN_PAREN, "(");
        Node condition = parseCondition();
        consume(TokenType.CLOSE_PAREN, ")");
        Node body = parseBlock();
        return new WhileStatementNode(condition, body, line);
    }

    private Node parseForStatement() {
        consume(TokenType.KEYWORD, "for");
        consume(TokenType.OPEN_PAREN, "(");

        Node initialization = parseAssignment();

        Node condition = parseCondition();
        consume(TokenType.SEMICOLON, ";");

        Node increment = parseExpression();

        consume(TokenType.CLOSE_PAREN, ")");

        Node body = parseBlock();

        return new ForStatementNode(initialization, condition, increment, body, line);
    }

    private Node parseIfStatement() {
        consume(TokenType.KEYWORD, "if");
        consume(TokenType.OPEN_PAREN, "(");
        Node condition = parseCondition();
        consume(TokenType.CLOSE_PAREN, ")");
        Node body = parseBlock();
        return new IfStatementNode(condition, body, line);
    }

    private Node parseMoveStatement() {
        consume(TokenType.KEYWORD, "move");
        consume(TokenType.OPEN_PAREN, "(");
        Node expression = parseExpression();
        consume(TokenType.CLOSE_PAREN, ")");
        consume(TokenType.SEMICOLON, ";");
        return new MoveStatementNode(expression, line);
    }

    private Node parseTurnStatement() {
        consume(TokenType.KEYWORD, "turn");
        consume(TokenType.OPEN_PAREN, "(");
        String direction = parseDirection();
        consume(TokenType.CLOSE_PAREN, ")");
        consume(TokenType.SEMICOLON, ";");
        return new TurnStatementNode(direction, line);
    }

    private Node parseExpression() {

        switch (peek().getValue()) {
            case "arePeopleWaiting":
                return parseArePeopleWaiting();
            case "getRobotPosX":
                return parseGetRobotPosX();
            case "getRobotPosZ":
                return parseGetRobotPosZ();
        }
        return parseExpressionWithPrecedence(0);
    }

    private Node parseExpressionWithPrecedence(int minPrecedence) {
        Token current = peek();

        if (isOperator(current) && (current.getValue().equals("++") || current.getValue().equals("--"))) {
            Token operator = advance();

            Token operandToken = peek();
            if (operandToken.getType() != TokenType.IDENTIFIER) {
                throw new IllegalArgumentException("Expected a variable name after " + operator.getValue() + ", but found: " + operandToken.getValue());
            }

            IdentifierNode operand = new IdentifierNode(advance().getValue(), line);
            return new PrefixIncrementDecrementNode(operator.getValue(), operand.getIdentifier(), line);
        }

        Node left = parsePrimary();

        while (!isAtEnd() && isOperator(peek()) && getPrecedence(peek()) >= minPrecedence) {
            Token operator = advance();
            int precedence = getPrecedence(operator);

            int nextMinPrecedence = isRightAssociative(operator) ? precedence : precedence + 1;

            Node right = parseExpressionWithPrecedence(nextMinPrecedence);
            left = new BinaryExpressionNode(left, operator.getValue(), right, line);
        }

        return left;
    }

    private Node parsePrimary() {
        Token current = advance();
        if (current.getType() == TokenType.NUMBER) {
            return new NumberNode(current.getValue(), line);
        } else if (current.getType() == TokenType.STRING) {
            return new StringNode(current.getValue(), line);
        } else if (current.getType() == TokenType.BOOLEAN) {
            return new BooleanNode(current.getValue().equals("true"), line);
        } else if (current.getType() == TokenType.IDENTIFIER) {
            IdentifierNode identifier = new IdentifierNode(current.getValue(), line);
            if (!isAtEnd() && peek().getType() == TokenType.OPERATOR && (peek().getValue().equals("++") || peek().getValue().equals("--"))) {
                return parsePostfixIncrementOrDecrement(identifier);
            }
            return identifier;
        } else if (current.getType() == TokenType.OPEN_PAREN) {
            Node expression = parseExpression();
            consume(TokenType.CLOSE_PAREN, ")");
            return expression;
        } else {
            throw new IllegalArgumentException("Unexpected token: " + current);
        }
    }

    private boolean isOperator(Token token) {
        return token.getType() == TokenType.OPERATOR;
    }

    private int getPrecedence(Token token) {
        int precedence = switch (token.getValue()) {
            case "++", "--" -> 9;
            case "!" -> 8;
            case "*", "/", "%" -> 7;
            case "+", "-" -> 6;
            case "<", ">", "<=", ">=" -> 5;
            case "==", "!=" -> 4;
            case "&&" -> 3;
            case "||" -> 2;
            case "=", "+=", "-=", "*=", "/=" -> 1;
            default -> -1;
        };

        if (precedence == -1) {
            throw new IllegalArgumentException("Unknown operator: " + token.getValue());
        }
        return precedence;
    }

    private boolean isRightAssociative(Token operator) {
        return operator.getValue().equals("=") || operator.getValue().equals("+=") || operator.getValue().equals("-=") || operator.getValue().equals("*=")
                || operator.getValue().equals("/=");
    }

    private Node parsePostfixIncrementOrDecrement(IdentifierNode identifier) {
        Token operator = advance();
        if (operator.getValue().equals("++") || operator.getValue().equals("--")) {
            return new PostfixIncrementDecrementNode(identifier.getIdentifier(), operator.getValue(), line);
        }
        throw new IllegalArgumentException("Expected '++' or '--', but found: " + operator.getValue());
    }

    private Node parseAssignment() {
        Token typeToken = null;
        if (check(TokenType.KEYWORD) && (peek().getValue().equals("int") || peek().getValue().equals("string") || peek().getValue().equals("boolean"))) {
            typeToken = advance();
        }
        Token identifier = consume(TokenType.IDENTIFIER, null);
        Token operator = advance();
        if (operator.getValue().equals("=")) {
            Node value = parseExpression();
            consume(TokenType.SEMICOLON, ";");
            return new DeclarationNode(typeToken.getValue(), identifier.getValue(), operator.getValue(), value, line);
        } else if (operator.getValue().equals("+=") || operator.getValue().equals("-=") || operator.getValue().equals("*=") || operator.getValue().equals("/=")) {
            if (typeToken.getValue() != "int") {
                throw new IllegalArgumentException("Type " + typeToken.getValue() + " is not allowed for " + operator.getValue());
            }
            Node value = parseExpression();
            consume(TokenType.SEMICOLON, ";");
            return new DeclarationNode(typeToken.getValue(), identifier.getValue(), operator.getValue(), value, line);

        }
        throw new IllegalArgumentException("Expected assignment operator, but found: " + operator.getValue());
    }

    private Node parseBlock() {
        consume(TokenType.OPEN_BRACE, "{");
        BlockNode blockNode = new BlockNode(line);
        while (!isAtEnd() && !check(TokenType.CLOSE_BRACE)) {
            blockNode.addStatement(parseStatement());
        }
        consume(TokenType.CLOSE_BRACE, "}");
        return blockNode;
    }

    private Node parseCondition() {

        Node cond = parseExpression();
        if (cond instanceof BinaryExpressionNode) {
            BinaryExpressionNode binaryExpressionNode = (BinaryExpressionNode) cond;
            if (isBooleanOperator(binaryExpressionNode.getOperator())) {
                return cond;
            } else {
                throw new IllegalArgumentException("Expected a boolean expression, but found: " + binaryExpressionNode.getOperator());
            }

        } else if (cond instanceof IdentifierNode) {
            return cond;
        } else if (cond instanceof BooleanNode) {
            return cond;

        }
        throw new IllegalArgumentException("Expected a boolean expression, but found: " + peek());
    }

    private boolean isBooleanOperator(String operator) {
        return operator.equals("<") || operator.equals(">") || operator.equals("<=") || operator.equals(">=") || operator.equals("==") || operator.equals("!=");
    }

    private String parseDirection() {
        Token current = advance();
        if (current.getType() == TokenType.KEYWORD && isDirection(current.getValue())) {
            return current.getValue();
        }
        throw new IllegalArgumentException("Invalid direction: " + current.getValue());
    }

    private boolean isDirection(String direction) {
        return direction.equals("north") || direction.equals("south") || direction.equals("east") || direction.equals("west") || direction.equals("left")
                || direction.equals("right") || direction.equals("back");
    }

    private Token peek() {
        if (isAtEnd()) {
            return null;
        }
        return tokens.get(position);
    }

    private Token advance() {
        if (!isAtEnd()) {
            position++;
        }
        return previous();
    }

    private Token previous() {
        if (position == 0) {
            return null;
        }
        return tokens.get(position - 1);
    }

    private boolean isAtEnd() {
        return position >= tokens.size();
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token consume(TokenType type, String value) {
        if (check(type) && (value == null || peek().getValue().equals(value))) {
            line = peek().getLineNumber();
            return advance();
        } else {
            throw new IllegalArgumentException("Expected " + value + " but found " + peek());
        }
    }
}
