/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package roboticoffee.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import roboticoffee.utils.Direction;

/**
 *
 * @author Bence
 */

public class RobotState extends AbstractAppState {

    private final Node rootNode;
    private final Node localRootNode = new Node("BaseRobotNode");
    private Spatial robotNode = new Node("RobotNode");
    private final AssetManager assetManager;
    private Integer robotPosX = 0;
    private Integer robotPosZ = 0;
    private Direction robotDirection = Direction.NORTH;

    public RobotState(SimpleApplication app) {
        rootNode = app.getRootNode();
        assetManager = app.getAssetManager();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        rootNode.attachChild(localRootNode);
        robotNode = assetManager.loadModel("Models/Robot.glb");
        localRootNode.attachChild(robotNode);
        turn("north");

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
        robotNode.setLocalRotation(rotation);

        System.out.println("Robot is now facing: " + robotDirection);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Quaternion getRotationForDirection(Direction direction) {
        switch (direction) {
            case WEST:
                return new Quaternion().fromAngleAxis(0, new com.jme3.math.Vector3f(0, 1, 0));
            case SOUTH:
                return new Quaternion().fromAngleAxis(FastMath.HALF_PI, new com.jme3.math.Vector3f(0, 1, 0));
            case EAST:
                return new Quaternion().fromAngleAxis(FastMath.PI, new com.jme3.math.Vector3f(0, 1, 0));
            case NORTH:
                return new Quaternion().fromAngleAxis(-FastMath.HALF_PI, new com.jme3.math.Vector3f(0, 1, 0));
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    public void move(int steps) {
        switch (robotDirection) {
            case NORTH:
                robotPosZ += steps;
                break;
            case SOUTH:
                robotPosZ -= steps;
                break;
            case EAST:
                robotPosX -= steps;
                break;
            case WEST:
                robotPosX += steps;
                break;
        }
        if (robotPosX < 0 || robotPosZ < 0 || robotPosX > 19 || robotPosZ > 19) {
            System.out.println("Robot cannot move outside the grid!");
            robotPosX = Math.max(0, Math.min(robotPosX, 19));
            robotPosZ = Math.max(0, Math.min(robotPosZ, 19));
        }
        robotNode.setLocalTranslation(robotPosX, 0, robotPosZ);
        System.out.println("Robot moved to position: (" + robotPosX + ", " + robotPosZ + ")");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);
    }

}
