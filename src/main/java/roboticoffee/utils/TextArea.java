package roboticoffee.utils;

import java.util.ArrayList;
import java.util.List;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.ElementManager;

public class TextArea {

    private Panel basePanel;
    private List<CustomTextField> textFields = new ArrayList<>();
    private List<Label> lineNumberLabels = new ArrayList<>();
    private float lineHeight = 20f;
    private float padding = 40f;
    private int maxLines = 50;

    public TextArea(ElementManager screen, Vector2f position, Vector2f dimension) {
        basePanel = new Panel(screen, position, dimension) {
            @Override
            public void setHeight(float height) {
                super.setHeight(height);
                updateRows(height);
            }
        };

        for (int i = 0; i < maxLines; i++) {
            textFields.add(createTextField(i));
            lineNumberLabels.add(createLabel(i));
        }

        updateRows(dimension.y);
    }

    private CustomTextField createTextField(int rowIndex) {
        CustomTextField textField = new CustomTextField(basePanel.getScreen(), new Vector2f(padding, (rowIndex + 1) * lineHeight),
                new Vector2f(basePanel.getAbsoluteWidth() - 2 * padding, lineHeight), rowIndex, this);

        textField.setColorMap("Textures/Transparent.png");
        textField.setFontColor(ColorRGBA.White);
        textField.setName("TextField_" + rowIndex);
        textField.setText("TextField_" + rowIndex);
        return textField;
    }

    private Label createLabel(int rowIndex) {
        Label label = new Label(basePanel.getScreen(), new Vector2f(0, (rowIndex + 1) * lineHeight), new Vector2f(40, lineHeight));

        label.setFontColor(ColorRGBA.Gray);
        label.setText(String.valueOf(rowIndex));
        label.setTextAlign(BitmapFont.Align.Right);
        return label;
    }

    private void updateRows(float height) {
        basePanel.removeAllChildren();
        int rowCount = (int) ((height - 2 * lineHeight) / lineHeight);

        while (rowCount > maxLines) {
            int oldMaxLines = maxLines;
            maxLines *= 2;
            for (int i = oldMaxLines; i < maxLines; i++) {
                textFields.add(createTextField(i));
                lineNumberLabels.add(createLabel(i));
            }
            System.out.println("Max lines increased to: " + maxLines);
        }

        for (int i = 0; i < rowCount; i++) {
            float yPosition = height - ((i + 2) * lineHeight);
            Label lineNumberLabel = lineNumberLabels.get(i);
            lineNumberLabel.setPosition(new Vector2f(0, yPosition+3));
            lineNumberLabel.setDimensions(new Vector2f(40, lineHeight));
            basePanel.addChild(lineNumberLabel);

            CustomTextField textField = textFields.get(i);
            textField.setPosition(new Vector2f(padding, yPosition));
            textField.setDimensions(new Vector2f(basePanel.getAbsoluteWidth() - 2 * padding, lineHeight));
            basePanel.addChild(textField);
        }
    }

    public Panel getBasePanel() {
        return basePanel;
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (CustomTextField textField : textFields) {
            String line = textField.getText();
            if (!line.isEmpty()) {
                sb.append(line).append(" ");
            }
        }
        return sb.toString().trim();
    }

    public void splitLine(int rowIndex, int cursorPosition) {
        if (rowIndex < 0 || rowIndex >= textFields.size()) {
            throw new IndexOutOfBoundsException("Invalid row index: " + rowIndex);
        }

        CustomTextField currentField = textFields.get(rowIndex);
        String currentLine = currentField.getText();

        String firstPart = currentLine.substring(0, cursorPosition);
        String secondPart = currentLine.substring(cursorPosition);

        currentField.setText(firstPart);
        CustomTextField newField = createTextField(rowIndex + 1);
        newField.setText(secondPart);
        textFields.add(rowIndex + 1, newField);

        moveCursorToRow(rowIndex + 1, 0);
    }

    public void mergeLine(int rowIndex) {
        if (rowIndex > 0) {
            CustomTextField previousField = textFields.get(rowIndex - 1);
            CustomTextField currentField = textFields.get(rowIndex);

            String mergedText = previousField.getText() + currentField.getText();
            previousField.setText(mergedText);

            textFields.remove(rowIndex);
            moveCursorToRow(rowIndex - 1, previousField.getText().length());
        }
    }

    public void moveCursorToRow(int rowIndex, int cursorPosition) {
        if (rowIndex >= 0 && rowIndex < textFields.size()) {
            CustomTextField textField = textFields.get(rowIndex);
            textField.setCursorPosition(cursorPosition);

            basePanel.getScreen().setKeyboardElement(textField);
            textField.setHasFocus(true);

            for (int i = 0; i < textFields.size(); i++) {
                if (i != rowIndex) {
                    textFields.get(i).setHasFocus(false);
                }
            }
        }

        updateRows(basePanel.getHeight());
    }

    public int getLineLength(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < textFields.size()) {
            return textFields.get(rowIndex).getText().length();
        }
        return 0;
    }

    public int getMaxLines() {
        return maxLines;
    }
}