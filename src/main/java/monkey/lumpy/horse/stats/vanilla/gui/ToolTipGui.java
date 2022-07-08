package monkey.lumpy.horse.stats.vanilla.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class ToolTipGui extends CottonClientScreen {

    public ToolTipGui(GuiDescription description) {
        super(description);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        KeyBinding key = MinecraftClient.getInstance().options.inventoryKey;

        // Close if inventory is pressed
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || key.matchesKey(keyCode, scanCode)) {
            close();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}