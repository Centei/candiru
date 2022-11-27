package net.centei.candiru.mixin;

import net.centei.candiru.AdvancementCallback;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public abstract class AdvancementMixin {
	private Advancement advancement;
	private ServerPlayerEntity owner;

	@Inject(at = @At("TAIL"), method = "grantCriterion")
	private void onGetAdvancement(Advancement advancement, String criterionName, final CallbackInfoReturnable<Boolean> info) {
		if (advancement.getDisplay() != null && advancement.getDisplay().shouldAnnounceToChat() && this.owner.world.getGameRules().getBoolean(GameRules.ANNOUNCE_ADVANCEMENTS)) {
			ActionResult result = AdvancementCallback.EVENT.invoker().interact(this.owner);
		}
	}
}

