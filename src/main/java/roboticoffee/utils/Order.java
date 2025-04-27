package roboticoffee.utils;

public class Order {

    private Table table;
    private CoffeeType orderedCoffee;

    public Order(Table table, CoffeeType orderedCoffee) {
        this.table = table;
        this.orderedCoffee = orderedCoffee;
    }

    public Table getTable() {
        return table;
    }

    public CoffeeType getOrderedCoffee() {
        return orderedCoffee;
    }

}
