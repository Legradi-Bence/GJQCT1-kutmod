package roboticoffee.utils;

public enum Direction {
    NORTH, EAST, SOUTH, WEST;
    
    public Direction rotateRight() {
        int newIndex = (this.ordinal() + 1) % values().length;
        return values()[newIndex];
    }
    public Direction rotateLeft() {
        int newIndex = (this.ordinal() - 1 + values().length) % values().length;
        return values()[newIndex];
    }
}
