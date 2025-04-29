package roboticoffee;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

import roboticoffee.states.CreateSceeneState;
import roboticoffee.states.PeopleState;
import roboticoffee.states.RobotState;
import roboticoffee.states.UIState;

/**
 * This is the Main Class of your Game. It should boot up your game and do
 * initial initialisation Move your Logic into AppStates or Controls or other
 * java classes
 */
public class RobotiCoffee extends SimpleApplication {
    private static RobotiCoffee instance;

    private Node rootNode;

    private Node localRootNode = new Node();

    public static void main(String[] args) {
        RobotiCoffee app = new RobotiCoffee();
        instance = app;
        AppSettings settings = new AppSettings(true);
        settings.setTitle("RobotiCoffee");

        int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        settings.setResolution((int) (screenWidth * 0.8), (int) (screenHeight * 0.8));
        settings.setFullscreen(false);

        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {

        stateManager.detach(stateManager.getState(com.jme3.app.StatsAppState.class));
        stateManager.detach(stateManager.getState(com.jme3.app.DebugKeysAppState.class));

        RobotState robotState = new RobotState(this);
        PeopleState peopleState = new PeopleState(this);
        stateManager.attach(robotState);
        stateManager.attach(peopleState);
        stateManager.attach(new UIState(this, robotState, peopleState));
        stateManager.attach(new CreateSceeneState(this));
        rootNode = getRootNode();
        rootNode.attachChild(localRootNode);
        addTables(peopleState);
    }

    private void addTables(PeopleState peopleState) {
        peopleState.addTable("Table_9_1", 9, 1);
        peopleState.addTable("Table_9_3", 9, 3);
        peopleState.addTable("Table_9_5", 9, 5);
        peopleState.addTable("Table_9_7", 9, 7);
        peopleState.addTable("Table_9_9", 9, 9);
        peopleState.addTable("Table_13_1", 13, 1);
        peopleState.addTable("Table_13_3", 13, 3);
        peopleState.addTable("Table_13_5", 13, 5);
        peopleState.addTable("Table_13_7", 13, 7);
        peopleState.addTable("Table_13_9", 13, 9);
    }

    public PeopleState getPeopleState() {
        return stateManager.getState(PeopleState.class);
    }

    public RobotState getRobotState() {
        return stateManager.getState(RobotState.class);
    }
    public static RobotiCoffee getInstance() {
        return instance;
    }
}
