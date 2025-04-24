package roboticoffee.utils;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Table {
    private String name;
    private boolean occupied;
    private int x;
    private int z;
    private Rectangle rectangle;

    public Table(Node rootNode, Spatial table, String name, int x, int z) {
        this.name = name;
        this.occupied = false;
        this.x = x;
        this.z = z;
        this.rectangle = new Rectangle(x - 1, z, x + 1, z);
        Spatial tableNode = table;
        tableNode.setName(name);
        tableNode.setLocalTranslation(x, 0, z);
        tableNode.setLocalRotation(new com.jme3.math.Quaternion().fromAngleAxis(FastMath.HALF_PI, new com.jme3.math.Vector3f(0, 1, 0)));
        rootNode.attachChild(tableNode);

    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public String getName() {
        return name;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
    public Vector3f getPosition() {
        return new Vector3f(x, 0, z);
    }

}
