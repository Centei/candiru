package net.centei.candiru.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.centei.candiru.Candiru;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class HeartsUseCommand {
    public static int subtract = 1;
    public static int advancementthreshold = 12;
    public static int heartdelay = 3;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("hearts").then(literal("use")
                .then(argument("count", IntegerArgumentType.integer(2))
                        .executes(HeartsUseCommand::run))));
    }

    public static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        if (IntegerArgumentType.getInteger(ctx, "count") > 1 && IntegerArgumentType.getInteger(ctx, "count") % 2 == 0) {
            subtract = IntegerArgumentType.getInteger(ctx, "count");
        } else {
            ctx.getSource().sendFeedback(Text.of("You must add hearts in multiples of two! (two health equals one heart)"), true);
            return -1;
        }
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
        EntityAttributeInstance health = (player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH));
        if (Candiru.HEART_MAP.containsKey(player.getUuid()) && Candiru.HEART_MAP.get(player.getUuid()) != 0 && health.getBaseValue() != 20 && Candiru.HEART_MAP.get(player.getUuid()) >= advancementthreshold) {
            if (Candiru.HEART_MAP.get(player.getUuid()) - (subtract * (heartdelay / 2)) < advancementthreshold - heartdelay) {
                ctx.getSource().sendFeedback(Text.literal("You can not use more hearts than you have!"), true);
                return -1;
            }
            if (((int) health.getBaseValue()) + subtract * 2 > 20) {
                ctx.getSource().sendFeedback(Text.literal("You can not add hearts past your maximum health (20)!"), true);
                return -1;
            }
            Candiru.HEART_MAP.put(player.getUuid(), Candiru.HEART_MAP.get(player.getUuid()) - (subtract * heartdelay) / 2);
            Candiru.DEATH_MAP.put(player.getUuid(), (int) health.getValue() + subtract);
            health.setBaseValue(health.getValue() + subtract);
            ctx.getSource().sendFeedback(Text.literal(player.getEntityName() + " now has " + health.getValue()), true);
        } else {
            ctx.getSource().sendFeedback(Text.literal("You don't have any hearts in your storage or you have max health!"), true);
            return -1;
        }
        return 1;
    }
}
