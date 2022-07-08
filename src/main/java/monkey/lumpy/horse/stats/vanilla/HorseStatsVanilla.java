package monkey.lumpy.horse.stats.vanilla;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import net.fabricmc.api.ModInitializer;

public class HorseStatsVanilla implements ModInitializer {

    @Override
    public void onInitialize() {
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

}