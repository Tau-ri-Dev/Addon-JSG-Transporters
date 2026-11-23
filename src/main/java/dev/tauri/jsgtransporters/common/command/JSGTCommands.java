package dev.tauri.jsgtransporters.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.tauri.jsgtransporters.common.helpers.TeleportHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

public class JSGTCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("jsgt")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("rotate_swap_volumes")
                        .then(Commands.argument("source_back_left_top", BlockPosArgument.blockPos())
                                .then(Commands.argument("source_front_right_bottom", BlockPosArgument.blockPos())
                                        .then(Commands.argument("target_back_left_top", BlockPosArgument.blockPos())
                                                .then(Commands.argument("target_front_right_bottom", BlockPosArgument.blockPos())
                                                        .then(Commands.argument("source_facing", StringArgumentType.word())
                                                                .then(Commands.argument("target_facing", StringArgumentType.word())
                                                                        .executes(JSGTCommands::rotateAndSwapVolumes)))))))));
    }

    private static int rotateAndSwapVolumes(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        try {
            TeleportHelper.rotateAndSwapVolumes(
                    new TeleportHelper.Volume(
                            context.getSource().getLevel(),
                            getBlockPos(context, "source_back_left_top"),
                            getBlockPos(context, "source_front_right_bottom"),
                            getDirection(context, "source_facing")),
                    new TeleportHelper.Volume(
                            context.getSource().getLevel(),
                            getBlockPos(context, "target_back_left_top"),
                            getBlockPos(context, "target_front_right_bottom"),
                            getDirection(context, "target_facing")), (o) -> 0L, (o) -> 0L, (energySumLocal, energySumRemote) -> {});
        } catch (IllegalArgumentException exception) {
            context.getSource().sendFailure(Component.literal(exception.getMessage()));
            return 0;
        }

        context.getSource().sendSuccess(() -> Component.literal("Rotated and swapped volumes."), true);
        return 1;
    }

    private static BlockPos getBlockPos(CommandContext<CommandSourceStack> context, String name)
            throws CommandSyntaxException {
        return BlockPosArgument.getLoadedBlockPos(context, name);
    }

    private static Direction getDirection(CommandContext<CommandSourceStack> context, String name)
            throws CommandSyntaxException {
        Direction direction = Direction.byName(StringArgumentType.getString(context, name));
        if (direction == null) {
            throw new SimpleCommandExceptionType(Component.literal(
                    "Facing must be north, south, east, west, up, or down.")).create();
        }
        return direction;
    }
}
