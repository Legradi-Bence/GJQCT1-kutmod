package roboticoffee.utils;

import java.util.Random;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Person {
    private Node rootNode;
    private String name;
    private Vector3f destination = null;
    private Spatial personNode;
    private Spatial personSittingNode;
    private Table table;
    private float speed = 1.0f;
    private Vector3f counterPos = new Vector3f(5, 0, 1);
    private Order order = null;
    private Random rnd = new Random();
    private boolean movingToCounter = true;
    private boolean leaving = false;
    private float leavingTime = 2;

    public Person(Node rootNode, Spatial person, Spatial personSitting, String name, int x, int z) {
        this.rootNode = rootNode;
        this.name = name;
        this.destination = counterPos;
        personNode = person;
        personSittingNode = personSitting;
        personNode.setName(name);
        personNode.setLocalTranslation(x, 0, z);
        personNode.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, new Vector3f(0, 1, 0)));
        rootNode.attachChild(personNode);
    }

    public void update(float tpf) {
        if (destination != null) {

            if (movingToCounter) {

                Vector3f direction = destination.subtract(personNode.getLocalTranslation()).normalizeLocal();
                personNode.move(direction.mult(speed * tpf));
                if (personNode.getLocalTranslation().distance(destination) < 0.1f) {
                    if (personNode.getLocalTranslation().distance(counterPos) < 0.1f) {

                        personNode.setLocalRotation(new Quaternion().fromAngleAxis(0, new Vector3f(0, 1, 0)));
                    }
                    personNode.setLocalTranslation(destination);
                    destination = null;
                }
            } else {
                Vector3f direction = destination.subtract(personNode.getLocalTranslation()).normalizeLocal();
                personNode.move(direction.mult(speed * tpf));
                personNode.lookAt(destination, Vector3f.UNIT_Y);
                if (personNode.getLocalTranslation().distance(destination) < 0.1f) {
                    rootNode.detachChild(personNode);
                    int rndSitting = rnd.nextInt(2);
                    if (rndSitting == 0) {
                        personSittingNode.setLocalTranslation(destination.add(.5f, 0, 0));
                        personSittingNode.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, new Vector3f(0, 1, 0)));
                    } else {
                        personSittingNode.setLocalTranslation(destination.add(-.5f, 0, 0));
                        personSittingNode.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, new Vector3f(0, 1, 0)));
                    }
                    rootNode.attachChild(personSittingNode);
                    destination = null;
                }
            }
        }
        if (leaving) {
            leavingTime -= tpf;
            if (leavingTime <= 0) {
                rootNode.detachChild(personSittingNode);
            }

        }

    }

    public void setDestiantion(Vector3f destination) {
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public Vector3f getDestination() {
        return destination;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

    public Order order() {
        if (!personNode.getLocalTranslation().equals(counterPos)) {
            return null;
        }
        int rndCoffee = rnd.nextInt(5) + 1;
        order = new Order(table, CoffeeType.getCoffeeByNumber(rndCoffee));
        movingToCounter = false;
        System.out.println("Order: " + order.getOrderedCoffee() + " for table: " + order.getTable().getName());//
        return order;
    }

    public boolean isAtCounter() {
        return personNode.getLocalTranslation().equals(counterPos);
    }

    public void setTableDestiantion() {
        this.destination = table.getPosition();
    }

    public void setLeaving(boolean leaving) {
        this.leaving = leaving;
    }

}
