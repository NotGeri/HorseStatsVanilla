package monkey.lumpy.horse.stats.vanilla.mixin;

import monkey.lumpy.horse.stats.vanilla.HorseStatsVanilla;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.TimeUnit;

@Mixin(ClientPlayNetworkHandler.class)
public class JoinWorldMixin {

    @Inject(at = @At("TAIL"), method = "onGameJoin")
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {

        // Check if horses are being spotted right now
        if (HorseStatsVanilla.spotHorses()) {

            // Clear known horses
            HorseStatsVanilla.getKnownHorses().clear();

            // Do not spot horses for the next few seconds
            HorseStatsVanilla.setSpotHorses(false);
            HorseStatsVanilla.getScheduler().schedule(() -> HorseStatsVanilla.setSpotHorses(true), 5, TimeUnit.SECONDS);
        }

    }

}