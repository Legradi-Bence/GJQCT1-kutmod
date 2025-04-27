package roboticoffee.states;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;

import io.tlf.jme.jfx.JavaFxUI;
import javafx.scene.control.Button;
import roboticoffee.utils.CodeWindowConrolGenerator;
import roboticoffee.utils.CodeWindowControl;
import roboticoffee.utils.ConsoleControl;

public class UIState extends AbstractAppState {

    private final Node rootNode;
    private final Node guiNode;

    private final static List<String> codeWindowNames = new ArrayList<>();

    public UIState(SimpleApplication app, RobotState robotState, PeopleState peopleState) {

        rootNode = app.getRootNode();
        guiNode = app.getGuiNode();
        CodeWindowConrolGenerator generator = new CodeWindowConrolGenerator(app, robotState, peopleState);
        JavaFxUI.initialize(app);

        Button button = new Button("New Code Window");
        button.setOnAction(event -> {
            CodeWindowControl codeControl = generator.generate();
            if (codeControl == null) {
                return;
            }
            codeWindowNames.add(codeControl.getName());
            JavaFxUI.getInstance().attachChild(codeControl);

        });
        JavaFxUI.getInstance().attachChild(button);

        ConsoleControl consoleControl = new ConsoleControl();

        JavaFxUI.getInstance().attachChild(consoleControl);
        consoleControl.setLayoutX(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width * 0.8 - 400);
        consoleControl.setLayoutY(10);
        robotState.setOnOrderChange(() -> {
            consoleControl.setOrders(robotState.getOrdersString());
        });
        robotState.setOnPrint(() -> {
            consoleControl.appendToConsole(robotState.getPrintString());
        });

    }

    public static boolean isCodeWindowNameUnique(String name) {
        return !codeWindowNames.contains(name);
    }

    public static List<String> getCodeWindowNames() {
        return codeWindowNames;
    }

    public static String getCode(String codeWindowName) {
        return ((CodeWindowControl) JavaFxUI.getInstance().getChild(codeWindowName)).getCode();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        app.getInputManager().setCursorVisible(true);
    }
}
