package roboticoffee.utils;

import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.ElementManager;

import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class CustomTextField extends TextField {

    private int rowIndex; // A sor indexe a TextArea-ban
    private TextArea parentTextArea; // Hivatkozás a szülő TextArea-ra

    public CustomTextField(ElementManager screen, Vector2f position, Vector2f dimensions, int rowIndex, TextArea parentTextArea) {
        super(screen, position, dimensions);
        this.rowIndex = rowIndex;
        this.parentTextArea = parentTextArea;
    }

    public int getCursorPosition() {
        return this.caretIndex;
    }

    public void setCursorPosition(int position) {
        this.caretIndex = Math.max(0, Math.min(position, text.length()));
    }



    @Override
    public void onKeyPress(KeyInputEvent evt) {
        super.onKeyPress(evt);

        int key = evt.getKeyCode();
        boolean isPressed = evt.isPressed();

        if (!isPressed) {
            return;
        }

        switch (key) {
            case KeyInput.KEY_RETURN: // Enter gomb
                parentTextArea.splitLine(rowIndex, getCursorPosition());
                break;

            case KeyInput.KEY_BACK: // Backspace gomb
                if (getCursorPosition() == 0 && rowIndex > 0) {
                    parentTextArea.mergeLine(rowIndex);
                }
                break;

            case KeyInput.KEY_UP: // Fel gomb
                parentTextArea.moveCursorToRow(rowIndex - 1, getCursorPosition());
                break;

            case KeyInput.KEY_DOWN: // Le gomb
                parentTextArea.moveCursorToRow(rowIndex + 1, getCursorPosition());
                break;

            case KeyInput.KEY_LEFT: // Balra gomb
                if (getCursorPosition() == 0 && rowIndex > 0) {
                    parentTextArea.moveCursorToRow(rowIndex - 1, parentTextArea.getLineLength(rowIndex - 1));
                } else {
                    setCursorPosition(getCursorPosition() - 1);
                }
                break;

            case KeyInput.KEY_RIGHT: // Jobbra gomb
                if (getCursorPosition() == getText().length() && rowIndex < parentTextArea.getMaxLines() - 1) {
                    parentTextArea.moveCursorToRow(rowIndex + 1, 0);
                } else {
                    setCursorPosition(getCursorPosition() + 1);
                }

            default:
                break;
        }
    }
}