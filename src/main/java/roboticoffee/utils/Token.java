package roboticoffee.utils;

public class Token {
    private final TokenType type;
    private final String value;
    private int lineNumber;

    public Token(TokenType type, String value, int lineNumber) {
        this.lineNumber = lineNumber;
        this.type = type;
        this.value = value;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
