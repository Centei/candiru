package net.centei.candiru.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.centei.candiru.Candiru;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class HeartsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("hearts").executes(HeartsCommand::run));
    }

    private static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
        if (!Candiru.HEART_MAP.containsKey(player.getUuid())) {
            ctx.getSource().sendFeedback(Text.literal("You haven't gotten any hearts yet!"), true);
            ctx.getSource().sendFeedback(Text.literal("To learn how to get hearts, do /hearts help!"), true);
            return -1;
        }
        String hearts = String.valueOf(0);
        if (Candiru.HEART_MAP.get(player.getUuid()) >= HeartsUseCommand.advancementthreshold) {
            int heartcalc = (int) Math.floor(((Candiru.HEART_MAP.get(player.getUuid()) - (HeartsUseCommand.advancementthreshold - HeartsUseCommand.heartdelay)) / 3));
            hearts = String.valueOf(heartcalc);
        }
        ctx.getSource().sendFeedback(Text.literal(player.getEntityName() + " has " + hearts + " hearts in storage. To use hearts do /hearts use <number of hearts>"), true);
        return 1;
    }
}
