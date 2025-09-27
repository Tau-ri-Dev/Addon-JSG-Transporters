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


public class GoauldCPRaycaster extends AbstractCPRaycaster {

    public static final GoauldCPRaycaster INSTANCE = new GoauldCPRaycaster();
    public static final List<RayCastedButton> BUTTONS = List.of(
            new RayCastedButton(0, List.of(
                    //Amun
                    new Vector3f(0.682055f, -0.974233f, 0.405662f),
                    new Vector3f(0.682092f, -0.974233f, 0.46714f),
                    new Vector3f(0.567787f, -0.974233f, 0.46721f),
                    new Vector3f(0.56775f, -0.974233f, 0.405732f)
            )),

            new RayCastedButton(1, List.of(
                    //Serket
                    new Vector3f(0.429686f, -0.974233f, 0.405662f),
                    new Vector3f(0.429723f, -0.974233f, 0.46714f),
                    new Vector3f(0.315418f, -0.974233f, 0.46721f),
                    new Vector3f(0.315381f, -0.974233f, 0.405732f)
            )),

            new RayCastedButton(2, List.of(
                    //Khepri
                    new Vector3f(0.429686f, -0.974233f, 0.30636f),
                    new Vector3f(0.429723f, -0.974233f, 0.367837f),
                    new Vector3f(0.315418f, -0.974233f, 0.367907f),
                    new Vector3f(0.315381f, -0.974233f, 0.30643f)
            )),

            new RayCastedButton(3, List.of(
                    //Ra
                    new Vector3f(0.682055f, -0.974233f, 0.30636f),
                    new Vector3f(0.682092f, -0.974233f, 0.367837f),
                    new Vector3f(0.567787f, -0.974233f, 0.367907f),
                    new Vector3f(0.56775f, -0.974233f, 0.30643f)
            )),

            new RayCastedButton(4, List.of(
                    //Felluca
                    new Vector3f(0.682055f, -0.974233f, 0.202036f),
                    new Vector3f(0.682092f, -0.974233f, 0.263514f),
                    new Vector3f(0.567787f, -0.974233f, 0.263584f),
                    new Vector3f(0.56775f, -0.974233f, 0.202106f)
            )),

            new RayCastedButton(5, List.of(
                    //Cobra - Activation Button
                    new Vector3f(0.429686f, -0.974233f, 0.202036f),
                    new Vector3f(0.429723f, -0.974233f, 0.263514f),
                    new Vector3f(0.315418f, -0.974233f, 0.263584f),
                    new Vector3f(0.315381f, -0.974233f, 0.202106f)
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
    public Vector3f getTranslation(Level level, BlockPos blockPos) {
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
    protected boolean buttonClicked(Level level, Player player, int button, BlockPos pos, InteractionHand interactionHand) {
        player.swing(interactionHand, true);
        JSGTPacketHandler.sendToServer(new CPButtonClickedToServer(pos, SymbolTypeRegistry.GOAULD.valueOf(button), false));
        return true;
    }

    @Override
    public float getRotation(Level level, BlockPos blockPos, Player player) {
        var direction = level.getBlockState(blockPos).getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY);
        return getIntRotation(direction);
    }
}
