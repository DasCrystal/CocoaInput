package jp.axer.cocoainput.wrapper;

import java.util.List;
import java.util.Optional;
import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;
import jp.axer.cocoainput.util.Rect;
import jp.axer.cocoainput.util.WrapperUtil;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;

public final class BookEditScreenWrapper extends IMEReceiver {

	private final BookEditScreen owner;
    private final IMEOperator myIME;

    public BookEditScreenWrapper(BookEditScreen field) {
        ModLogger.log("BookEditScreen init: " + field.hashCode());
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
        myIME.setFocused(true);
    }

	@Override
    protected void setText(String text) {
        if (owner.signing) {
            owner.title = text;
        } else {
            owner.setPageContent(text);
        }
    }

	@Override
    protected String getText() {
        if (owner.signing) {
            return owner.title;
        } else {
            return owner.getCurrentPageContent();
        }
    }

	@Override
    protected void setCursorInvisible() {
		// TODO
        owner.tickCounter = 6;
    }

	@Override
    protected int getCursorPos() {
        if (owner.signing) {
            return owner.bookTitleSelectionManager.getSelectionStart();
        } else {
            return owner.currentPageSelectionManager.getSelectionStart();
        }
    }

	@Override
    protected void setCursorPos(int cursorPos) {
        if (owner.signing) {
            owner.bookTitleSelectionManager.moveCursorTo(cursorPos, true);
        } else {
            owner.currentPageSelectionManager.moveCursorTo(cursorPos, true);
        }
    }

	@Override
    protected void setSelectionPos(int selectionPos) {
        if (owner.signing) {
            owner.bookTitleSelectionManager.setSelection(selectionPos, selectionPos);
        } else {
            owner.currentPageSelectionManager.setSelection(selectionPos, selectionPos);
        }
    }

    @Override
    public Rect getRect() {
        TextRenderer fontRendererObj = null;
        try {
            fontRendererObj = WrapperUtil.makeFont(owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (owner.signing) {
            return new Rect(
				(fontRendererObj.getWidth(owner.title.substring(0, originalCursorPosition)) / 2 + ((owner.width - 192) / 2) + 36 + (116 - 0) / 2),
				(50 + fontRendererObj.fontHeight),
				0,
				0
            );
        } else {
            TextHandler manager = fontRendererObj.getTextHandler();
            List<StringVisitable> lines = manager.wrapLines(owner.getCurrentPageContent(), 116, Style.EMPTY);
            final String[] lastLine = new String[1];
            StringVisitable.Visitor acceptor = new StringVisitable.Visitor() {
                @Override
                public Optional accept(String p_accept_1_) {
                    lastLine[0] = p_accept_1_;
                    return Optional.empty();
                }
            };
            lines.get(lines.size() - 1).visit(acceptor);

            return new Rect(
				(((owner.width - 192) / 2) + 36 + fontRendererObj.getWidth(lastLine[0])),
				(34 + lines.size() * fontRendererObj.fontHeight),
				0,
				0
            );
        }
    }

    public int renewCursorCounter() {
        return owner.tickCounter + (cursorVisible ? 1 : 0);
    }
}
