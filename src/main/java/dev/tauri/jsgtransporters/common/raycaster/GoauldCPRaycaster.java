package dev.tauri.jsgtransporters.common.raycaster;

import dev.tauri.jsg.property.JSGProperties;
import dev.tauri.jsg.raycaster.Raycaster;
import dev.tauri.jsg.raycaster.util.RayCastedButton;
import dev.tauri.jsg.util.vectors.Vector3f;
import dev.tauri.jsgtransporters.common.block.controller.RingsGoauldCPBlock;
import dev.tauri.jsgtransporters.common.packet.JSGTPacketHandler;
import dev.tauri.jsgtransporters.common.packet.packets.CPButtonClickedToServer;
import dev.tauri.jsgtransporters.common.rings.network.SymbolTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;


public class GoauldCPRaycaster extends Raycaster {

    public static final GoauldCPRaycaster INSTANCE = new GoauldCPRaycaster();
    public static final List<RayCastedButton> BUTTONS = List.of(
            new RayCastedButton(0, List.of(
                    new Vector3f(0.682164f, -0.96202f, 0.467285f),
                    new Vector3f(0.567919f, -0.96202f, 0.467285f),
                    new Vector3f(0.567919f, -0.96202f, 0.405885f),
                    new Vector3f(0.682164f, -0.96202f, 0.405885f)
            )),

            new RayCastedButton(1, List.of(
                    new Vector3f(0.429659f, -0.96202f, 0.467285f),
                    new Vector3f(0.315479f, -0.96202f, 0.467285f),
                    new Vector3f(0.315479f, -0.96202f, 0.405885f),
                    new Vector3f(0.429659f, -0.96202f, 0.405885f)
            )),

            new RayCastedButton(2, List.of(
                    new Vector3f(0.429659f, -0.96202f, 0.367759f),
                    new Vector3f(0.315479f, -0.96202f, 0.367759f),
                    new Vector3f(0.315479f, -0.96202f, 0.306547f),
                    new Vector3f(0.429659f, -0.96202f, 0.306547f)
            )),

            new RayCastedButton(3, List.of(
                    new Vector3f(0.682164f, -0.96202f, 0.367759f),
                    new Vector3f(0.567919f, -0.96202f, 0.367759f),
                    new Vector3f(0.567919f, -0.96202f, 0.306547f),
                    new Vector3f(0.682164f, -0.96202f, 0.306547f)
            )),

            new RayCastedButton(4, List.of(
                    new Vector3f(0.682164f, -0.96202f, 0.26352f),
                    new Vector3f(0.567919f, -0.96202f, 0.26352f),
                    new Vector3f(0.567919f, -0.96202f, 0.202186f),
                    new Vector3f(0.682164f, -0.96202f, 0.202186f)
            )),

            new RayCastedButton(5, List.of(
                    new Vector3f(0.429659f, -0.96202f, 0.26352f),
                    new Vector3f(0.315479f, -0.96202f, 0.26352f),
                    new Vector3f(0.315479f, -0.96202f, 0.202186f),
                    new Vector3f(0.429659f, -0.96202f, 0.202186f)
            ))
    );

    public static void register() {
        Raycaster.register(RingsGoauldCPBlock.class, INSTANCE);
    }


    @Override
    protected List<RayCastedButton> getButtons() {
        return BUTTONS;
    }

    @Override
    protected Vector3f getTranslation(Level level, BlockPos blockPos) {
        var facing = level.getBlockState(blockPos).getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY);
        return getTranslation(facing);
    }

    public static int getIntRotation(Direction direction) {
        return switch (direction) {
            case EAST -> 270;
            case SOUTH -> 180;
            case WEST -> 90;
            default -> 0;
        };
    }

    protected static final Vector3f NORTH_TRANSLATION = new Vector3f(0, 0, 0);
    protected static final Vector3f EAST_TRANSLATION = new Vector3f(1, 0, 0);
    protected static final Vector3f SOUTH_TRANSLATION = new Vector3f(1, 0, 1);
    protected static final Vector3f WEST_TRANSLATION = new Vector3f(0, 0, 1);

    public static Vector3f getTranslation(Direction facing) {
        return switch (facing) {
            case EAST -> EAST_TRANSLATION;
            case SOUTH -> SOUTH_TRANSLATION;
            case WEST -> WEST_TRANSLATION;
            default -> NORTH_TRANSLATION;
        };
    }

    @Override
    public boolean onActivated(Level level, BlockPos blockPos, Player player, InteractionHand interactionHand) {
        if (interactionHand != InteractionHand.MAIN_HAND) return false;
        var direction = level.getBlockState(blockPos).getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY);
        var rotation = getIntRotation(direction);
        return super.onActivated(level, blockPos, player, rotation, interactionHand);
    }

    @Override
    protected boolean buttonClicked(Level level, Player player, int button, BlockPos pos, InteractionHand interactionHand) {
        player.swing(interactionHand, true);
        JSGTPacketHandler.sendToServer(new CPButtonClickedToServer(pos, SymbolTypeRegistry.GOAULD.valueOf(button), false));
        return true;
    }
}
