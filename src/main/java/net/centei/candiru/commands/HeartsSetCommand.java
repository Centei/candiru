package net.centei.candiru.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.centei.candiru.Candiru;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class HeartsSetCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("hearts")
                .then(literal("set")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(argument("player", EntityArgumentType.players())
                                .then(argument("count", IntegerArgumentType.integer(0)).executes(HeartsSetCommand::run)))));
    }

    public static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
        Candiru.HEART_MAP.put(player.getUuid(), IntegerArgumentType.getInteger(ctx, "count"));
        player.sendMessage(Text.literal("Your number of stored hearts has been set to " + IntegerArgumentType.getInteger(ctx, "count") + " by Server Operator " + ctx.getSource().getPlayerOrThrow().getEntityName() + "."), true);
        return 1;
    }
}
