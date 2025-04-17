package roboticoffee.utils;

import java.util.List;

import roboticoffee.utils.Nodes.AssignmentNode;
import roboticoffee.utils.Nodes.BinaryExpressionNode;
import roboticoffee.utils.Nodes.BlockNode;
import roboticoffee.utils.Nodes.DirectionNode;
import roboticoffee.utils.Nodes.ExpressionStatementNode;
import roboticoffee.utils.Nodes.ForStatementNode;
import roboticoffee.utils.Nodes.IdentifierNode;
import roboticoffee.utils.Nodes.IfStatementNode;
import roboticoffee.utils.Nodes.MoveStatementNode;
import roboticoffee.utils.Nodes.Node;
import roboticoffee.utils.Nodes.NumberNode;
import roboticoffee.utils.Nodes.PostfixIncrementDecrementNode;
import roboticoffee.utils.Nodes.PrefixIncrementDecrementNode;
import roboticoffee.utils.Nodes.ProgramNode;
import roboticoffee.utils.Nodes.TurnStatementNode;
import roboticoffee.utils.Nodes.WhileStatementNode;

public class Parser {
    private List<Token> tokens;
    private int position;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public ProgramNode parse() {
        ProgramNode programNode = new ProgramNode();
        while (!isAtEnd()) {
            programNode.addStatement(parseStatement());
        }
        return programNode;
    }

    private Node parseStatement() {
        Token current = peek();
        if (current.getType() == TokenType.KEYWORD && current.getValue().equals("if")) {
            return parseIfStatement();
        } else if (current.getType() == TokenType.KEYWORD && current.getValue().equals("for")) {
            return parseForStatement();
        } else if (current.getType() == TokenType.KEYWORD && current.getValue().equals("while")) {
            return parseWhileStatement();
        } else if (current.getType() == TokenType.KEYWORD && current.getValue().equals("move")) {
            return parseMoveStatement();
        } else if (current.getType() == TokenType.KEYWORD && current.getValue().equals("turn")) {
            return parseTurnStatement();
        } else {
            return parseExpressionStatement();
        }
    }

    private Node parseExpressionStatement() {
        Node expression = parseExpression();
        consume(TokenType.SEMICOLON, ";");
        return new ExpressionStatementNode(expression);
    }

    private Node parseWhileStatement() {
        consume(TokenType.KEYWORD, "while");
        consume(TokenType.OPEN_PAREN, "(");
        Node condition = parseCondition();
        consume(TokenType.CLOSE_PAREN, ")");
        Node body = parseBlock();
        return new WhileStatementNode(condition, body);
    }

    private Node parseForStatement() {
        consume(TokenType.KEYWORD, "for");
        consume(TokenType.OPEN_PAREN, "(");
        Node initialization = parseAssignment();
        Node condition = parseCondition();
        Node increment = parseExpression();
        consume(TokenType.CLOSE_PAREN, ")");
        Node body = parseBlock();
        return new ForStatementNode(initialization, condition, increment, body);
    }

    private Node parseIfStatement() {
        consume(TokenType.KEYWORD, "if");
        consume(TokenType.OPEN_PAREN, "(");
        Node condition = parseCondition();
        consume(TokenType.CLOSE_PAREN, ")");
        Node body = parseBlock();
        return new IfStatementNode(condition, body);
    }

    private Node parseMoveStatement() {
        consume(TokenType.KEYWORD, "move");
        consume(TokenType.OPEN_PAREN, "(");
        Node expression = parseExpression();
        consume(TokenType.CLOSE_PAREN, ")");
        return new MoveStatementNode(expression);
    }

    private Node parseTurnStatement() {
        consume(TokenType.KEYWORD, "turn");
        consume(TokenType.OPEN_PAREN, "(");
        Node direction = parseDirection();
        consume(TokenType.CLOSE_PAREN, ")");
        return new TurnStatementNode(direction);
    }

    private Node parseExpression() {
        return parseExpressionWithPrecedence(0);
    }

    private Node parseExpressionWithPrecedence(int minPrecedence) {
        Token current = peek();

        // Prefix operátorok kezelése
        if (isOperator(current) && (current.getValue().equals("++") || current.getValue().equals("--"))) {
            Token operator = advance();
            Node operand = parseExpressionWithPrecedence(1);
            return new PrefixIncrementDecrementNode(operator.getValue(), operand);
        }

        Node left = parsePrimary();

        while (!isAtEnd() && isOperator(peek()) && getPrecedence(peek()) >= minPrecedence) {
            Token operator = advance();
            int precedence = getPrecedence(operator);

            int nextMinPrecedence = isRightAssociative(operator) ? precedence : precedence + 1;

            Node right = parseExpressionWithPrecedence(nextMinPrecedence);
            left = new BinaryExpressionNode(left, operator.getValue(), right);
        }

        return left;
    }

    private Node parsePrimary() {
        Token current = advance();
        if (current.getType() == TokenType.NUMBER) {
            return new NumberNode(current.getValue());
        } else if (current.getType() == TokenType.IDENTIFIER) {
            Node identifier = new IdentifierNode(current.getValue());
            // Ellenőrizzük, hogy van-e postfix operátor
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
            case "++", "--" -> 1;
            case "!" -> 2;
            case "*", "/", "%" -> 3;
            case "+", "-" -> 4;
            case "<", ">", "<=", ">=" -> 5;
            case "==", "!=" -> 6;
            case "&&" -> 7;
            case "||" -> 8;
            case "=", "+=", "-=", "*=", "/=" -> 9;
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

    private Node parsePostfixIncrementOrDecrement(Node identifier) {
        Token operator = advance(); // ++ vagy --
        if (operator.getValue().equals("++") || operator.getValue().equals("--")) {
            return new PostfixIncrementDecrementNode(identifier, operator.getValue());
        }
        throw new IllegalArgumentException("Expected '++' or '--', but found: " + operator.getValue());
    }

    private Node parsePrefixIncrementOrDecrement() {
        Token operator = advance(); // ++ vagy --
        Node identifier = parsePrimary();
        if (operator.getValue().equals("++") || operator.getValue().equals("--")) {
            return new PrefixIncrementDecrementNode(operator.getValue(), identifier);
        }
        throw new IllegalArgumentException("Expected '++' or '--', but found: " + operator.getValue());
    }

    private Node parseAssignment() {
        Token identifier = consume(TokenType.IDENTIFIER, null);
        Token operator = advance();
        if (operator.getValue().equals("=") || operator.getValue().equals("+=") || operator.getValue().equals("-=") || operator.getValue().equals("*=")
                || operator.getValue().equals("/=")) {
            Node value = parseExpression();
            return new AssignmentNode(identifier.getValue(), operator.getValue(), value);
        }
        throw new IllegalArgumentException("Expected assignment operator, but found: " + operator.getValue());
    }

    private Node parseBlock() {
        consume(TokenType.OPEN_BRACE, "{");
        BlockNode blockNode = new BlockNode();
        while (!isAtEnd() && !check(TokenType.CLOSE_BRACE)) {
            blockNode.addStatement(parseStatement());
        }
        consume(TokenType.CLOSE_BRACE, "}");
        return blockNode;
    }

    private Node parseCondition() {

        Node left = parseExpression(); // Számra kiértékelhető kifejezés
        if (isBooleanOperator(peek())) {
            Token operator = advance();
            Node right = parseExpression(); // Számra kiértékelhető kifejezés
            return new BinaryExpressionNode(left, operator.getValue(), right);
        }
        throw new IllegalArgumentException("Expected a boolean expression, but found: " + peek());
    }

    private boolean isBooleanOperator(Token token) {
        return token.getValue().equals("<") || token.getValue().equals(">") || token.getValue().equals("<=") || token.getValue().equals(">=") || token.getValue().equals("==")
                || token.getValue().equals("!=");
    }

    private Node parseDirection() {
        Token current = advance();
        if (current.getType() == TokenType.KEYWORD && isDirection(current.getValue())) {
            return new DirectionNode(current.getValue());
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
        if (check(type) && peek().getValue().equals(value)) {
            return advance();
        } else {
            throw new IllegalArgumentException("Expected " + value + " but found " + peek());
        }
    }
}
