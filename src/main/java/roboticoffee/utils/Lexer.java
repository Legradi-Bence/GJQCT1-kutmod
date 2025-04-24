package roboticoffee.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Lexer {

    private final String input;
    private int position = 0;
    private int line = 1;
    private int inlinePosition = 0;
    private static final Set<String> KEYWORDS = Set.of("if", "else", "while", "for", "return", "int", "string", "boolean", "move", "turn", "true", "false", "north", "south", "east",
            "west", "left","right", "back", "function","takeOrder","placeCoffee","takeCoffee","arePeopleWaiting","print","getRobotPosX","getRobotPosZ");
    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/", "%", "=", "==", "!=", "++","--", "+=", "-=", "*=", "/=", "&&", "||", "!", "<", ">", "<=", ">=");

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Stack<Token> stack = new Stack<>();
        while (position < input.length()) {
            char currentChar = input.charAt(position);

            if (Character.isWhitespace(currentChar)) {
                position++;
                inlinePosition++;
                if (currentChar == '\n') {
                    line++;
                    inlinePosition = 0;
                }
                continue;
            }

            if (Character.isDigit(currentChar)) {
                tokens.add(lexNumber());
                continue;
            }

            if (Character.isLetter(currentChar)) {
                tokens.add(lexString());
                continue;
            }
            if (currentChar == '\"') {
                tokens.add(lexStringValue());
                continue;
            }
            Token operatorToken = lexOperator();
            if (operatorToken != null) {
                tokens.add(operatorToken);
                continue;
            }
            Token tk = null;
            switch (currentChar) {
                case '(':
                    tk = new Token(TokenType.OPEN_PAREN, "(", line);
                    tokens.add(tk);
                    stack.push(tk);
                    break;
                case ')':
                    if (stack.isEmpty() || stack.pop().getType() != TokenType.OPEN_PAREN) {
                        System.out.println("Unmatched closing parenthesis at line " + line + ", position " + inlinePosition);
                        //throw new IllegalArgumentException("Unmatched closing parenthesis at position " + position);
                    }
                    tokens.add(new Token(TokenType.CLOSE_PAREN, ")", line));
                    break;
                case '{':
                    tk = new Token(TokenType.OPEN_BRACE, "{", line);
                    tokens.add(tk);
                    stack.push(tk);
                    break;
                case '}':
                    if (stack.isEmpty() || stack.pop().getType() != TokenType.OPEN_BRACE) {
                        System.out.println("Unmatched closing brace at line " + line + ", position " + inlinePosition);
                        //throw new IllegalArgumentException("Unmatched closing brace at position " + position);
                    }
                    tokens.add(new Token(TokenType.CLOSE_BRACE, "}", line));
                    break;
                case '[':
                    tk = new Token(TokenType.OPEN_BRACKET, "[", line);
                    tokens.add(tk);
                    stack.push(tk);
                    break;
                case ']':
                    if (stack.isEmpty() || stack.pop().getType() != TokenType.OPEN_BRACKET) {
                        System.out.println("Unmatched closing bracket at line " + line + ", position " + inlinePosition);
                        //throw new IllegalArgumentException("Unmatched closing bracket at position " + position);
                    }
                    tokens.add(new Token(TokenType.CLOSE_BRACKET, "]", line));
                    break;
                case ';':
                    tokens.add(new Token(TokenType.SEMICOLON, ";", line));
                    break;
                default:
                    tokens.add(new Token(TokenType.UNKNOWN, String.valueOf(currentChar), line));
            }
            position++;
        }
        if (!stack.isEmpty()) {
            System.out.println("Unmatched opening parenthesis at end of input");
            //throw new IllegalArgumentException("Unmatched opening parenthesis at end of input");
        }
        return tokens;
    }

    private Token lexNumber() {
        int start = position;
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            position++;
        }
        String number = input.substring(start, position);
        return new Token(TokenType.NUMBER, number, line);
    }

    private Token lexString() {
        int start = position;
        while (position < input.length() && Character.isLetterOrDigit(input.charAt(position))) {
            position++;
        }
        String identifier = input.substring(start, position);
        if (KEYWORDS.contains(identifier)) {
            if (identifier.equals("true") || identifier.equals("false")) {
                return new Token(TokenType.BOOLEAN, identifier, line);
            }
            return new Token(TokenType.KEYWORD, identifier, line);
        }
        return new Token(TokenType.IDENTIFIER, identifier, line);
    }

    private Token lexStringValue() {
        position++;
        int start = position;
        while (position < input.length() && input.charAt(position) != '\"') {
            position++;
        }
        String value = input.substring(start, position);
        position++;
        return new Token(TokenType.STRING, value, line);
    }

    private Token lexOperator() {
        for (int length = 3; length > 0; length--) {
            if (position + length <= input.length()) {
                String potentialOperator = input.substring(position, position + length);
                if (OPERATORS.contains(potentialOperator)) {
                    position += length;
                    return new Token(TokenType.OPERATOR, potentialOperator, line);
                }
            }
        }
        return null;
    }

}
