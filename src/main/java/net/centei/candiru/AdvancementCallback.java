package net.centei.candiru;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface AdvancementCallback {
    Event<AdvancementCallback> EVENT = EventFactory.createArrayBacked(AdvancementCallback.class,
            (listeners) -> (player) -> {
                for (AdvancementCallback listener : listeners) {
                    ActionResult result = listener.interact(player);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(ServerPlayerEntity player);
}
