package roboticoffee.utils;

public class InterpreterException extends Exception {
    private final int lineNumber;

    public InterpreterException(String message, int lineNumber) {
        super(message);
        this.lineNumber = lineNumber;
    }
    
    public InterpreterException(String message) {
        super(message);
        this.lineNumber = -1;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        if (lineNumber == -1) {
            return "Error: " + getMessage();
        }
        return "Error at line " + lineNumber + ": " + getMessage();
    }
}
