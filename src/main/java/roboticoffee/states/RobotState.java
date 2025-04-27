/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package roboticoffee.states;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import roboticoffee.RobotiCoffee;
import roboticoffee.utils.CoffeeType;
import roboticoffee.utils.Direction;
import roboticoffee.utils.Order;
import roboticoffee.utils.Rectangle;
import roboticoffee.utils.Table;

/**
 *
 * @author Bence
 */

public class RobotState extends AbstractAppState {

    private final SimpleApplication simpleApp;
    private final Node rootNode;
    private final Node localRootNode = new Node("BaseRobotNode");
    private Spatial robotNode = new Node("RobotNode");
    private Spatial robotWithCoffeeNode = new Node("RobotWithCoffeeNode");
    private Spatial robotNoCoffeeNode = new Node("RobotNoCoffeeNode");
    private final AssetManager assetManager;
    private Integer robotPosX = 5;
    private Integer robotPosZ = 3;
    private Direction robotDirection = Direction.SOUTH;
    private final List<Rectangle> validAreas = new ArrayList<>();
    private final List<Rectangle> obstacles = new ArrayList<>();
    private CoffeeType inHand = null;
    private final Queue<Order> orders = new LinkedList<>();
    private String printString = null;

    private Runnable onPrint = null;
    private Runnable onOrderChange = null;

    public RobotState(SimpleApplication app) {
        simpleApp = app;
        rootNode = app.getRootNode();
        assetManager = app.getAssetManager();
        validAreas.add(new Rectangle(0, 0, 5, 1));
        validAreas.add(new Rectangle(6, 0, 16, 10));
        validAreas.add(new Rectangle(1, 3, 5, 4));
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        simpleApp.enqueue(() -> {
            rootNode.attachChild(localRootNode);
            robotWithCoffeeNode = assetManager.loadModel("Models/Robot.glb");
            robotNoCoffeeNode = assetManager.loadModel("Models/Robotnopohar.glb");
            robotNode = robotNoCoffeeNode;
            robotNode.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, new com.jme3.math.Vector3f(0, 1, 0)));
            robotNode.setLocalTranslation(5, 0, 3);
            localRootNode.attachChild(robotNode);
            return null;
        });

    }

    public void turn(String direction) {
        switch (direction.toLowerCase()) {
            case "right":
                robotDirection = robotDirection.rotateRight();
                break;
            case "left":
                robotDirection = robotDirection.rotateLeft();
                break;
            case "back":
                robotDirection = robotDirection.rotateRight().rotateRight();
                break;
            case "north":
                robotDirection = Direction.NORTH;
                break;
            case "east":
                robotDirection = Direction.EAST;
                break;
            case "south":
                robotDirection = Direction.SOUTH;
                break;
            case "west":
                robotDirection = Direction.WEST;
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        Quaternion rotation = getRotationForDirection(robotDirection);

        simpleApp.enqueue(() -> {
            robotNode.setLocalRotation(rotation);
            return null;
        });
    }

    private Quaternion getRotationForDirection(Direction direction) {
        switch (direction) {
            case WEST:
                return new Quaternion().fromAngleAxis(FastMath.PI, new com.jme3.math.Vector3f(0, 1, 0));
            case SOUTH:
                return new Quaternion().fromAngleAxis(-FastMath.HALF_PI, new com.jme3.math.Vector3f(0, 1, 0));
            case EAST:
                return new Quaternion().fromAngleAxis(0, new com.jme3.math.Vector3f(0, 1, 0));
            case NORTH:
                return new Quaternion().fromAngleAxis(FastMath.HALF_PI, new com.jme3.math.Vector3f(0, 1, 0));
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    public void move(int steps) {
        int newX = robotPosX;
        int newZ = robotPosZ;
        for (int i = 0; i < steps; i++) {
            // Egyesével lépj a megadott irányba
            switch (robotDirection) {
                case NORTH:
                    newZ++;
                    break;
                case SOUTH:
                    newZ--;
                    break;
                case EAST:
                    newX--;
                    break;
                case WEST:
                    newX++;
                    break;
            }

            // Ellenőrizd az aktuális pozíciót
            if (!isPositionValid(newX, newZ)) {
                System.out.println("Cannot move to position: (" + newX + ", " + newZ + "). Obstacle detected!");
                break;
            } else {
                robotPosX = newX;
                robotPosZ = newZ;
            }
        }
        simpleApp.enqueue(() -> {
            robotNode.setLocalTranslation(robotPosX, 0, robotPosZ);
            return null;
        });

    }

    private boolean isPositionValid(int x, int z) {
        for (Rectangle area : validAreas) {
            if (area.contains(x, z)) {
                for (Rectangle obstacle : obstacles) {
                    if (obstacle.contains(x, z)) {
                        return false;
                    }
                }
                for (Table table : ((RobotiCoffee) simpleApp).getPeopleState().getTables()) {
                    if (table.getRectangle().contains(x, z)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public Vector3f getHeadPosition() {
        switch (robotDirection) {
            case NORTH:
                return new Vector3f(robotPosX, 0, robotPosZ + 1);
            case SOUTH:
                return new Vector3f(robotPosX, 0, robotPosZ - 1);
            case EAST:
                return new Vector3f(robotPosX - 1, 0, robotPosZ);
            case WEST:
                return new Vector3f(robotPosX + 1, 0, robotPosZ);
            default:
                throw new IllegalArgumentException("Invalid direction: " + robotDirection);
        }
    }

    public boolean isAtCounter() {
        return getHeadPosition().equals(new Vector3f(5, 0, 2)) && robotPosX == 5 && robotPosZ == 3;
    }

    public int getCoffeeNumber() {
        if (robotDirection != Direction.NORTH) {
            return -1;
        } else if (getHeadPosition().equals(new Vector3f(5, 0, 5))) {
            setInHand(CoffeeType.LATTE);
            return 1;
        } else if (getHeadPosition().equals(new Vector3f(4, 0, 5))) {
            setInHand(CoffeeType.CAPPUCCINO);
            return 2;
        } else if (getHeadPosition().equals(new Vector3f(3, 0, 5))) {
            setInHand(CoffeeType.MELANGE);
            return 3;
        } else if (getHeadPosition().equals(new Vector3f(2, 0, 5))) {
            setInHand(CoffeeType.PRESSO);
            return 4;
        } else if (getHeadPosition().equals(new Vector3f(1, 0, 5))) {
            setInHand(CoffeeType.FRAPPE);
            return 5;
        } else {
            return -1;
        }
    }

    public CoffeeType getInHand() {
        return inHand;
    }

    public void setInHand(CoffeeType inHand) {
        this.inHand = inHand;

        Vector3f currentPosition = robotNode.getLocalTranslation();
        Quaternion currentRotation = robotNode.getLocalRotation();

        simpleApp.enqueue(() -> {
            localRootNode.detachChild(robotNode);

            if (inHand != null) {
                robotNode = robotWithCoffeeNode;
            } else {
                robotNode = robotNoCoffeeNode;
            }

            robotNode.setLocalTranslation(currentPosition);
            robotNode.setLocalRotation(currentRotation);

            localRootNode.attachChild(robotNode);
            return null;
        });
    }

    public int getRobotPosX() {
        return robotPosX;
    }

    public int getRobotPosZ() {
        return robotPosZ;
    }

    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);
    }

    public boolean arePeopleWaiting() {
        if (((RobotiCoffee) simpleApp).getPeopleState().isEmptyQueue()) {
            return false;
        }
        return true;
    }

    public void addOrder(Order order) {
        orders.add(order);
        OnOrderChanged();
    }

    public Order getOrderByTable(Table table) {
        for (Order order : orders) {
            if (order.getTable().equals(table)) {
                return order;
            }
        }
        return null;
    }

    public void removeOrder(Order order) {
        if (orders.remove(order)) {
            System.out.println("Order removed: " + order);
        } else {
            System.out.println("Order not found: " + order);

        }
        OnOrderChanged();
    }

    public String getOrdersString() {
        StringBuilder orderString = new StringBuilder();
        for (Order order : orders) {
            orderString.append(order.getOrderedCoffee().toString().substring(0, 1) + order.getOrderedCoffee().toString().substring(1).toLowerCase()).append(" for table: ")
                    .append(order.getTable().getName()).append("\n");
        }
        return orderString.toString();
    }

    private void OnOrderChanged() {
        onOrderChange.run();
    }

    public void setOnOrderChange(Runnable onOrderChange) {
        this.onOrderChange = onOrderChange;
    }

    public void setOnPrint(Runnable onPrint) {
        this.onPrint = onPrint;
    }

    public String getPrintString() {
        return printString;
    }

    public void OnPrint(String printString) {
        if (onPrint == null) {
            return;
        }
        this.printString = printString;
        onPrint.run();
    }

    public String getFirstOrderCoffeeName() {
        if (orders.isEmpty()) {
            return "null";
        }
        return orders.peek().getOrderedCoffee().toString().substring(0, 1) + orders.peek().getOrderedCoffee().toString().substring(1).toLowerCase();
    }

    public String getFirstOrderTableName() {
        if (orders.isEmpty()) {
            return "null";
        }
        return orders.peek().getTable().getName();
    }
}
