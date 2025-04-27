package roboticoffee.utils;

import java.util.List;

import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import roboticoffee.states.PeopleState;
import roboticoffee.states.RobotState;
import roboticoffee.utils.Nodes.ProgramNode;

public class CodeWindowControl extends StackPane {

    private double mouseX, mouseY;
    private TextArea textArea;
    private String name;
    private RobotState robotState;
    private Interpreter interpreter;
    private Button runButton;
    private Button pauseButton;
    private Button stopButton;
    private Task<Void> currentTask;

    public CodeWindowControl(String name, RobotState robotState, PeopleState peopleState) {
        this.robotState = robotState;
        this.name = name;
        interpreter = new Interpreter(name, robotState, peopleState, this);
        Label label = new Label(name);
        runButton = new Button("R");
        runButton.setOnAction(event -> {
            onRunButtonClicked();
        });
        pauseButton = new Button("P");
        pauseButton.setOnAction(event -> {
            onPauseButtonClicked();
        });
        pauseButton.setDisable(true);
        stopButton = new Button("S");
        stopButton.setOnAction(event -> {
            onStopButtonClicked();
        });
        stopButton.setDisable(true);

        textArea = new TextArea("");
        HBox hbox = new HBox(10);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hbox.getChildren().addAll(label, spacer, runButton, pauseButton, stopButton);
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

    private void onStopButtonClicked() {
        interpreter.setProgramStatus(ProgramStatus.STOPPED);

        if (currentTask != null && currentTask.isRunning()) {
            currentTask.cancel();
            currentTask = null;
        }

        runButton.setDisable(false);
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
    }

    private void onPauseButtonClicked() {
        interpreter.setProgramStatus(ProgramStatus.PAUSED);
        runButton.setDisable(false);
        pauseButton.setDisable(true);
        stopButton.setDisable(false);
    }

    private void onRunButtonClicked() {
        if (currentTask != null && currentTask.isRunning()) {
            if (interpreter.getProgramStatus() == ProgramStatus.PAUSED) {
                interpreter.setProgramStatus(ProgramStatus.RUNNING);
                runButton.setDisable(true);
                pauseButton.setDisable(false);
                stopButton.setDisable(false);
            } else {
                System.out.println("A program már fut!");
            }
            return;
        }
        interpreter.setProgramStatus(ProgramStatus.RUNNING);
        runButton.setDisable(true);
        pauseButton.setDisable(false);
        stopButton.setDisable(false);
        Lexer lexer = new Lexer(textArea.getText());
        List<Token> tokens = lexer.tokenize();
        Parser parser = new Parser(tokens);
        ProgramNode programNode = parser.parse();
        currentTask = new Task<>() {
            @Override
            protected Void call() {
                interpreter.execute(programNode);
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                runButton.setDisable(false);
                pauseButton.setDisable(true);
                stopButton.setDisable(true);
                currentTask = null;
            }

            @Override
            protected void failed() {
                super.failed();
                runButton.setDisable(false);
                pauseButton.setDisable(true);
                stopButton.setDisable(true);
                currentTask = null;
            }
        };

        Thread thread = new Thread(currentTask);
        thread.setDaemon(true);
        thread.start();

    }

    public void highlightLine(int lineNumber) {
        String[] lines = textArea.getText().split("\n");
        int start = 0;

        for (int i = 0; i < lineNumber - 1; i++) {
            start += lines[i].length() + 1;
        }

        if (lineNumber > lines.length || lineNumber < 1) {
            return;
        }
        int end = start + lines[lineNumber - 1].length();

        textArea.selectRange(start, end);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return textArea.getText();
    }

}
