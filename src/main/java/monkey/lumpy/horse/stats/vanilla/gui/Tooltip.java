package monkey.lumpy.horse.stats.vanilla.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.math.Color;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import net.minecraft.text.Text;

public class Tooltip extends LightweightGuiDescription {

    public Tooltip(double speed, double jump, double health) {
        super();

        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        WBox root = new WBox(Axis.VERTICAL);
        setRootPanel(root);
        root.setSpacing(-8);
        root.setInsets(new Insets(5, 5, 0, 5));

        // Coloring
        Color jumpColor = config.getNeutralColor();
        Color speedColor = config.getNeutralColor();
        Color hearthColor = config.getNeutralColor();

        if (config.useColors()) {
            if (jump > config.getGoodHorseJumpValue()) {
                jumpColor = config.getGoodColor();
            } else if (jump < config.getBadHorseJumpValue()) {
                jumpColor = config.getBadColor();
            }

            if (speed > config.getGoodHorseSpeedValue()) {
                speedColor = config.getGoodColor();
            } else if (speed < config.getBadHorseSpeedValue()) {
                speedColor = config.getBadColor();
            }

            if (health > config.getGoodHorseHeartsValue()) {
                hearthColor = config.getGoodColor();
            } else if (health < config.getBadHorseHeartsValue()) {
                hearthColor = config.getBadColor();
            }
        }

        WBox speedBox = new WBox(Axis.HORIZONTAL);

        WLabel speedSymbol = new WLabel(Text.literal("➟"), speedColor.hashCode());
        WLabel speedLabel = new WLabel(Text.literal("" + speed), speedColor.hashCode());

        speedBox.add(speedSymbol);
        speedBox.add(speedLabel);

        WBox jumpBox = new WBox(Axis.HORIZONTAL);

        WLabel jumpSymbol = new WLabel(Text.literal("⇮"), jumpColor.hashCode());
        WLabel jumpLabel = new WLabel(Text.literal("" + jump), jumpColor.hashCode());

        jumpBox.add(jumpSymbol);
        jumpBox.add(jumpLabel);

        WBox healthBox = new WBox(Axis.HORIZONTAL);

        WLabel healthSymbol = new WLabel(Text.literal("♥"), hearthColor.hashCode());
        WLabel healthLabel = new WLabel(Text.literal("" + health), hearthColor.hashCode());

        healthBox.add(healthSymbol);
        healthBox.add(healthLabel);

        root.add(speedBox);
        root.add(jumpBox);
        root.add(healthBox);
        root.validate(this);
    }

}
