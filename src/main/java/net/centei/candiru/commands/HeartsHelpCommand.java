package net.centei.candiru.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class HeartsHelpCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("hearts").then(literal("help").executes(HeartsHelpCommand::run)));
    }

    private static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ctx.getSource().sendFeedback(Text.literal("To get hearts, you must complete advancements!"), true);
        ctx.getSource().sendFeedback(Text.literal("Once you have gotten over " + (HeartsUseCommand.advancementthreshold - 3) + " advancements, you will start earning hearts!"), true);
        ctx.getSource().sendFeedback(Text.literal("Every " + HeartsUseCommand.heartdelay + " hearts after this, you will get a heart!"), true);
        ctx.getSource().sendFeedback(Text.literal("This means that you will get your first heart when you have " + (HeartsUseCommand.advancementthreshold + HeartsUseCommand.heartdelay) + " advancements!"), true);
        ctx.getSource().sendFeedback(Text.literal("Check out the Source!: https://github.com/Centei/candiru.1.18"), true);
        return 1;
    }
}
