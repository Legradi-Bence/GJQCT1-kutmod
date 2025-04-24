package roboticoffee;


import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
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

    private Node rootNode;

    private Node localRootNode = new Node();

    public static void main(String[] args) {
        RobotiCoffee app = new RobotiCoffee();

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
        peopleState.addTable("Table_A_1", 10, 1);
        peopleState.addTable("Table_A_2", 10, 3);
        peopleState.addTable("Table_A_3", 10, 5);
        peopleState.addTable("Table_A_4", 10, 7);
        peopleState.addTable("Table_B_1", 12, 1);
        peopleState.addTable("Table_B_2", 12, 3);
        peopleState.addTable("Table_B_3", 12, 5);
        peopleState.addTable("Table_B_4", 12, 7);
    }

    @Override
    public void simpleUpdate(float tpf) {

    }

    @Override
    public void simpleRender(RenderManager rm) {
        // TODO: add render code
    }

    public PeopleState getPeopleState() {
        return stateManager.getState(PeopleState.class);
    }

    public RobotState getRobotState() {
        return stateManager.getState(RobotState.class);
    }
}
