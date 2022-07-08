package monkey.lumpy.horse.stats.vanilla;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.util.Converter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.text.Text;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HorseStatsVanilla implements ModInitializer {

    private static boolean spotHorses = true;
    private static final ArrayList<UUID> knownHorses = new ArrayList<>();
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.#");
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void onInitialize() {

        // Register entity load hook
        ClientEntityEvents.ENTITY_LOAD.register(((entity, world) -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (!(entity instanceof HorseEntity horse)) return;
            if (knownHorses.contains(horse.getUuid())) return;

            if (!spotHorses) {
                knownHorses.add(horse.getUuid());
                return;
            }

            if (player == null) {
                knownHorses.add(horse.getUuid());
                return;
            }

            // Patch weird bug with the horse's data not being present on servers
            scheduler.schedule(() -> {

                if (!horse.isBaby()) {
                    knownHorses.add(horse.getUuid());
                    return;
                }

                player.sendMessage(Text.translatable(
                        "horsestatsvanilla.chat.baby",
                        "§3" + entity.getBlockX(),
                        "§3" + entity.getBlockY(),
                        "§3" + entity.getBlockZ(),
                        "§a" + formatValue(Converter.genericSpeedToBlocPerSec(horse.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED))),
                        "§9" + formatValue(Converter.jumpStrengthToJumpHeight(horse.getJumpStrength())),
                        "§c" + formatValue(horse.getMaxHealth()))
                );

                knownHorses.add(horse.getUuid());

            }, 500, TimeUnit.MILLISECONDS);

        }));

        // Register config
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
    }

    /**
     * Format a {@link double} value
     *
     * @param value The value to format
     * @return The formatted value
     */
    public static double formatValue(double value) {
        String stringValue = decimalFormat.format(value);
        return new BigDecimal(stringValue.replace(',', '.')).doubleValue();
    }

    /**
     * @return Whether horses are spotted inc hat
     */
    public static boolean spotHorses() {
        return spotHorses;
    }

    /**
     * @param value Whether horses should be spotted in chat
     */
    public static void setSpotHorses(boolean value) {
        spotHorses = value;
    }

    /**
     * @return The main {@link ScheduledExecutorService}
     */
    public static ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    /**
     * @return An {@link ArrayList} of {@link UUID}s of known horses
     */
    public static ArrayList<UUID> getKnownHorses() {
        return knownHorses;
    }

}