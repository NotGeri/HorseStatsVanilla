package monkey.lumpy.horse.stats.vanilla.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import monkey.lumpy.horse.stats.vanilla.HorseStatsVanilla;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.gui.ToolTipGui;
import monkey.lumpy.horse.stats.vanilla.gui.Tooltip;
import monkey.lumpy.horse.stats.vanilla.util.Converter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HorseEntity.class)
public abstract class HorseEntityMixin extends AbstractHorseEntity {

    private final ModConfig config;

    protected HorseEntityMixin(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
        this.config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    @Inject(at = @At("HEAD"), method = "interactMob")
    public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ret) {
        if (this.world.isClient && !this.isTame() && player.shouldCancelInteraction() && (config == null || config.isTooltipEnabled())) {

            // Get stat values
            double jumpStrength = Converter.jumpStrengthToJumpHeight(this.getJumpStrength());
            double maxHealth = this.getMaxHealth();
            double speed = Converter.genericSpeedToBlocPerSec(this.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));

            // Show tooltip
            MinecraftClient.getInstance().setScreen(new ToolTipGui(new Tooltip(
                    HorseStatsVanilla.formatValue(speed),
                    HorseStatsVanilla.formatValue(jumpStrength),
                    HorseStatsVanilla.formatValue(maxHealth)))
            );
        }
    }


}
