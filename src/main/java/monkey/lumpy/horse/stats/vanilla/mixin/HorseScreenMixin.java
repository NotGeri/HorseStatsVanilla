package monkey.lumpy.horse.stats.vanilla.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.math.Color;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.util.Converter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Mixin(HorseScreen.class)
public abstract class HorseScreenMixin extends HandledScreen<HorseScreenHandler> {

    @Shadow
    @Final
    private AbstractHorseEntity entity;
    private ModConfig config;

    public HorseScreenMixin(HorseScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);
        if (config == null) config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        boolean hasChest = AbstractDonkeyEntity.class.isAssignableFrom(this.entity.getClass()) && ((AbstractDonkeyEntity) this.entity).hasChest();

        DecimalFormat df = new DecimalFormat("#.#");
        String jumpStrength = df.format(Converter.jumpStrengthToJumpHeight(this.entity.getJumpStrength()));
        String maxHealth = df.format(this.entity.getMaxHealth());
        String speed = df.format(Converter.genericSpeedToBlocPerSec(this.entity.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));

        // Coloring
        Color jumpColor = config.getNeutralColor();
        Color speedColor = config.getNeutralColor();
        Color hearthColor = config.getNeutralColor();
        if (config.useColors()) {
            double jumpValue = new BigDecimal(jumpStrength.replace(',', '.')).doubleValue();
            double speedValue = new BigDecimal(speed.replace(',', '.')).doubleValue();
            double healthValue = new BigDecimal(maxHealth.replace(',', '.')).doubleValue();

            if (jumpValue > config.getGoodHorseJumpValue()) {
                jumpColor = config.getGoodColor();
            } else if (jumpValue < config.getBadHorseJumpValue()) {
                jumpColor = config.getBadColor();
            }

            if (speedValue > config.getGoodHorseSpeedValue()) {
                speedColor = config.getGoodColor();
            } else if (speedValue < config.getBadHorseSpeedValue()) {
                speedColor = config.getBadColor();
            }

            if (healthValue > config.getGoodHorseHeartsValue()) {
                hearthColor = config.getGoodColor();
            } else if (healthValue < config.getBadHorseHeartsValue()) {
                hearthColor = config.getBadColor();
            }
        }


        if (!hasChest) {
            int spacer = 1;
            if (config.showMaxMin()) {
                context.drawText(this.textRenderer, "(4.7-14.2)", 119, 26, config.getNeutralColor().hashCode(), false);
                context.drawText(this.textRenderer, "(1-5.3)", 119, 36, config.getNeutralColor().hashCode(), false);
                context.drawText(this.textRenderer, "(15-30)", 119, 46, config.getNeutralColor().hashCode(), false);
            } else {
                spacer = 10;
            }

            context.drawText(this.textRenderer, "➟", 82 + spacer, 26, speedColor.hashCode(), false);
            context.drawText(this.textRenderer, speed, 93 + spacer, 26, speedColor.hashCode(), false);

            context.drawText(this.textRenderer, "⇮", 84 + spacer, 36, jumpColor.hashCode(), false);
            context.drawText(this.textRenderer, jumpStrength, 93 + spacer, 36, jumpColor.hashCode(), false);
            context.drawText(this.textRenderer, "♥", 83 + spacer, 46, hearthColor.hashCode(), false);
            context.drawText(this.textRenderer, maxHealth, 93 + spacer, 46, hearthColor.hashCode(), false);

        } else {
            context.drawText(this.textRenderer, "➟ " + speed, 80, 6, speedColor.hashCode(), false);
            context.drawText(this.textRenderer, "⇮ " + jumpStrength, 115, 6, jumpColor.hashCode(), false);
            context.drawText(this.textRenderer, "♥ " + maxHealth, 140, 6, hearthColor.hashCode(), false);
        }

        Color strengthColor = config.getNeutralColor();

        if (LlamaEntity.class.isAssignableFrom(this.entity.getClass())) {
            int strength = 3 * ((LlamaEntity) this.entity).getStrength();

            if (config.useColors()) {
                if (strength > config.getGoodHorseJumpValue()) {
                    strengthColor = config.getGoodColor();
                } else if (strength < config.getBadHorseJumpValue()) {
                    strengthColor = config.getBadColor();
                }
            }
            if (!hasChest) {
                context.drawText(this.textRenderer, "▦", 91, 56, strengthColor.hashCode(), false);
                context.drawText(this.textRenderer, String.valueOf(strength), 100, 56, strengthColor.hashCode(), false);
            }
        }
    }

}
