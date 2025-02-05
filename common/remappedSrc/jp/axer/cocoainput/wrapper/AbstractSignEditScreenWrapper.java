package jp.axer.cocoainput.wrapper;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;
import jp.axer.cocoainput.util.Rect;
import jp.axer.cocoainput.util.WrapperUtil;
import net.minecraft.block.SignBlock;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;

public final class AbstractSignEditScreenWrapper extends IMEReceiver {

    private final AbstractSignEditScreen owner;
    private final IMEOperator myIME;

    public AbstractSignEditScreenWrapper(AbstractSignEditScreen field) {
        ModLogger.debug("AbstractSignEditScreen init: " + field.hashCode());
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
        myIME.setFocused(true);
    }

    @Override
    protected void setText(String text) {
        owner.setCurrentRowMessage(text);
        String[] util = owner.messages;
        util[owner.currentRow] = text;
    }

    @Override
    protected String getText() {
        return owner.messages[owner.currentRow];
    }

    @Override
    protected void setCursorInvisible() {
        // TODO
        owner.ticksSinceOpened = 6;
    }

    @Override
    protected int getCursorPos() {
        return owner.selectionManager.getSelectionStart();
    }

    @Override
    protected void setCursorPos(int cursorPos) {
        owner.selectionManager.moveCursorTo(cursorPos, true);
    }

    @Override
    protected void setSelectionPos(int selectionPos) {
        owner.selectionManager.setSelection(selectionPos, selectionPos);
    }

    @Override
    public Rect getRect() {
        TextRenderer fontRendererObj = null;
        try {
            fontRendererObj = WrapperUtil.makeFont(owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        float y = 91 + (owner.currentRow - 1) * (10);
        if (!(owner.blockEntity.getCachedState().getBlock() instanceof SignBlock)) {
            y += 30;
        }
        return new Rect(
            owner.width / 2 + fontRendererObj.getWidth(owner.messages[owner.currentRow].substring(0, originalCursorPosition)) / 2,
            // owner.width / 2 + fontRendererObj.width(owner.sign.getMessage(owner.line,false).getString()) / 2,
            y,
            0,
            0
        );
    }

    public int renewCursorCounter() {
        return owner.ticksSinceOpened + (cursorVisible ? 1 : 0);
    }
}
