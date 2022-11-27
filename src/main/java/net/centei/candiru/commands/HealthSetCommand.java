package net.centei.candiru.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.centei.candiru.Candiru;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class HealthSetCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("health")
                .then(literal("set")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(argument("player", EntityArgumentType.players())
                                .then(argument("count", IntegerArgumentType.integer(1)).executes(HealthSetCommand::run)))));
    }

    public static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
        EntityAttributeInstance health = player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if ((int) health.getBaseValue() == IntegerArgumentType.getInteger(ctx, "count")) {
            ctx.getSource().sendFeedback(Text.literal("You can not set someone's health to be the exact same!"), true);
            return -1;
        }
        int beforechange = (int) health.getBaseValue();
        Candiru.DEATH_MAP.put(player.getUuid(), IntegerArgumentType.getInteger(ctx, "count"));
        health.setBaseValue(Candiru.DEATH_MAP.get(player.getUuid()).doubleValue());
        if (IntegerArgumentType.getInteger(ctx, "count") < beforechange) {
            boolean bl = false;
            boolean bl2 = false;
            StatusEffectInstance status = null;
            StatusEffectInstance status2 = null;

            if (player.hasStatusEffect(StatusEffects.ABSORPTION)) {
                bl = true;
                status = player.getStatusEffect(StatusEffects.ABSORPTION);

            }
            if (player.hasStatusEffect(StatusEffects.RESISTANCE)) {
                bl2 = true;
                status2 = player.getStatusEffect(StatusEffects.RESISTANCE);
            }
            DamageSource damageSource = DamageSource.GENERIC;
            player.removeStatusEffect(StatusEffects.ABSORPTION);
            player.removeStatusEffect(StatusEffects.RESISTANCE);

            player.damage(damageSource, 0.5F);
            if (bl) {
                player.addStatusEffect(status);
            }
            if (bl2) {
                player.addStatusEffect(status2);
            }
        }
        player.sendMessage(Text.literal("Your health has been set to " + health.getValue() + " by Server Operator " + ctx.getSource().getPlayerOrThrow().getEntityName() + "!"), true);
        return 1;
    }
}
