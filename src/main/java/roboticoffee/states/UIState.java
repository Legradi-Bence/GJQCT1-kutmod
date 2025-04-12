package roboticoffee.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;

import groovyjarjarpicocli.CommandLine.Help.Ansi.Text;
import io.tlf.jme.jfx.JavaFxUI;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UIState extends AbstractAppState {

    private final Node rootNode;
    private final Node guiNode;
    private double mouseX, mouseY;
    private TextArea textArea;

    public UIState(SimpleApplication app) {

        rootNode = app.getRootNode();
        guiNode = app.getGuiNode();

        JavaFxUI.initialize(app);

        Button button = new Button("Run");
        button.setOnAction(event -> {
            getTextFromTextArea();
            System.out.println("Button clicked! Text: " + textArea.getText());
        });
        textArea = new TextArea("Hello, JavaFX in JME!");

        VBox content = new VBox(10);
        content.getChildren().addAll(button, textArea);
        VBox.setVgrow(textArea, javafx.scene.layout.Priority.ALWAYS);

        StackPane window = new StackPane(content);
        window.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: lightgray;");
        window.setPrefSize(300, 200);

        window.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        window.setOnMouseDragged(event -> {
            if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                double deltaX = event.getSceneX() - mouseX;
                double deltaY = event.getSceneY() - mouseY;
                window.setTranslateX(window.getTranslateX() + deltaX);
                window.setTranslateY(window.getTranslateY() + deltaY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            } else if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                double newWidth = window.getPrefWidth() + (event.getSceneX() - mouseX);
                double newHeight = window.getPrefHeight() + (event.getSceneY() - mouseY);
                window.setPrefSize(Math.max(newWidth, 100), Math.max(newHeight, 100));
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            }
        });
        JavaFxUI.getInstance().attachChild(window);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        app.getInputManager().setCursorVisible(true);
    }

    private String getTextFromTextArea() {
        String text = textArea.getText();
        return text;
    }
}
