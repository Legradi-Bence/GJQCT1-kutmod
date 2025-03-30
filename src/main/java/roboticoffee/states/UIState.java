package roboticoffee.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

public class UIState extends AbstractAppState {

    private final Node rootNode;
    private final Node guiNode;
    private Screen screen;

    public UIState(SimpleApplication app) {
        rootNode = app.getRootNode();
        guiNode = app.getGuiNode();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        screen = new Screen(app);
        guiNode.addControl(screen);

        Window win = new Window(screen, "win", new Vector2f(15, 15), new Vector2f(300, 500));

        
        ButtonAdapter button = new ButtonAdapter(screen, "MyButton", new Vector2f(0, 0), new Vector2f(20, 20)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                System.out.println("A gombot megnyomták!");
            }
        };
        button.setText("A");
        win.addChild(button);
        screen.addElement(win);
    }

}
