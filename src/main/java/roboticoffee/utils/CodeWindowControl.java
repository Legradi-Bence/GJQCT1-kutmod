package roboticoffee.utils;

import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import roboticoffee.states.RobotState;
import roboticoffee.utils.Nodes.ProgramNode;

public class CodeWindowControl extends StackPane {

    private double mouseX, mouseY;
    private TextArea textArea;
    private String name;
    private RobotState robotState;

    public CodeWindowControl(String name, RobotState robotState) {
        this.robotState = robotState;
        this.name = name;
        Label label = new Label(name);
        Button button = new Button("Run");
        button.setOnAction(event -> {
            onRunButtonClicked();
        });

        textArea = new TextArea("");
        HBox hbox = new HBox(10);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hbox.getChildren().addAll(label, spacer, button);
        VBox content = new VBox(10);
        content.getChildren().addAll(hbox, textArea);
        VBox.setVgrow(textArea, Priority.ALWAYS);

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
                this.setLayoutX(this.getLayoutX() + deltaX);
                this.setLayoutY(this.getLayoutY() + deltaY);
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

        this.getChildren().add(window);
    }

    private void onRunButtonClicked() {
        Lexer lexer = new Lexer(textArea.getText());
        List<Token> tokens = lexer.tokenize();
        Parser parser = new Parser(tokens);
        ProgramNode programNode = parser.parse();
        Interpreter interpreter = new Interpreter(name, robotState);
        interpreter.execute(programNode);

    }

    public String getName() {
        return name;
    }
    public String getCode() {
        return textArea.getText();
    }
}
