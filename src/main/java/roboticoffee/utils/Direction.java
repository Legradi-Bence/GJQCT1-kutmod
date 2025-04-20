package roboticoffee.utils;

public enum Direction {
    NORTH, EAST, SOUTH, WEST;
    
    public Direction rotateRight() {
        int newIndex = (this.ordinal() + 1) % values().length;
        return values()[newIndex];
    }

    // Balra forgatás (90 fok)
    public Direction rotateLeft() {
        int newIndex = (this.ordinal() - 1 + values().length) % values().length;
        return values()[newIndex];
    }
}
