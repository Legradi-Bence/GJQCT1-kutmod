package roboticoffee.utils;

public enum CoffeeType {
    LATTE, CAPPUCCINO, MELANGE, PRESSO, FRAPPE;

    public static CoffeeType getCoffeeByNumber(int i) {
        switch (i) {
            case 1:
                return LATTE;
            case 2:
                return CAPPUCCINO;
            case 3:
                return MELANGE;
            case 4:
                return PRESSO;
            case 5:
                return FRAPPE;
            default:
                return null;
        }
    }
}
