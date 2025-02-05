package jp.axer.cocoainput.util;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;

public final class WrapperUtil {

    public static TextRenderer makeFont(Screen owner) throws Exception {
        return owner.textRenderer;
    }
}
