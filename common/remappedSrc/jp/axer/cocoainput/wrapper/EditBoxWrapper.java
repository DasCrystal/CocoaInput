
package jp.axer.cocoainput.wrapper;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;
import jp.axer.cocoainput.util.Rect;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public final class EditBoxWrapper extends IMEReceiver {

    private final TextFieldWidget owner;
    private final IMEOperator myIME;

    public EditBoxWrapper(TextFieldWidget field) {
        ModLogger.debug("EditBox init: " + field.hashCode());
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
    }

    public void setCanLoseFocus(boolean newParam) {
        if (!newParam) setFocused(true);
    }

    public void setFocused(boolean newParam) {
        owner.setRenderTextProvider(((abc, def) -> Text.literal(abc).asOrderedText()));
        myIME.setFocused(newParam);
    }

    @Override
    protected void setText(String text) {
        owner.text = text;
    }

    @Override
    protected String getText() {
        return owner.text;
    }

    public boolean isCursorVisible() {
        return cursorVisible;
    }

    @Override
    protected int getCursorPos() {
        return owner.getCursor();
    }

    @Override
    protected void setCursorPos(int cursorPos) {
        owner.setCursor(cursorPos, false);
    }

    @Override
    protected void setSelectionPos(int selectionPos) {
        owner.setSelectionEnd(selectionPos);
    }

    @Override
    public Rect getRect() {
        return new Rect( // {x,y}
            (owner.textRenderer.getWidth(owner.getText().substring(0, originalCursorPosition)) + (owner.drawsBackground ? owner.getX() + 4 : owner.getX())),
            (owner.textRenderer.fontHeight + (owner.drawsBackground ? owner.getY() + (owner.getHeight() - 8) / 2 : owner.getY())),
            owner.getWidth(),
            owner.getHeight()
        );
    }

    @Override
    protected void notifyParent(String text) {
        owner.onChanged(text);
    }
}
