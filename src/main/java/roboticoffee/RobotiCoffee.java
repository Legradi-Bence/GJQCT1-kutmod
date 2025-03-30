package roboticoffee;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;

import roboticoffee.states.CreateSceeneState;
import roboticoffee.states.RobotState;
import roboticoffee.states.UIState;

/**
 * This is the Main Class of your Game. It should boot up your game and do
 * initial initialisation Move your Logic into AppStates or Controls or other
 * java classes
 */
public class RobotiCoffee extends SimpleApplication {

    public static void main(String[] args) {
        RobotiCoffee app = new RobotiCoffee();

        AppSettings settings = new AppSettings(true);
        settings.setTitle("RobotiCoffee");
        settings.setResolution(1280, 720);
        settings.setFullscreen(false);
        app.setSettings(settings);

        app.start();
    }

    @Override
    public void simpleInitApp() {

        stateManager.detach(stateManager.getState(com.jme3.app.StatsAppState.class));
        stateManager.detach(stateManager.getState(com.jme3.app.DebugKeysAppState.class));

        stateManager.attach(new CreateSceeneState(this));
        stateManager.attach(new RobotState(this));
        stateManager.attach(new UIState(this));

    }

    @Override
    public void simpleUpdate(float tpf) {
        // TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        // TODO: add render code
    }
}
