package dev.tauri.jsgtransporters.common.raycaster;

import dev.tauri.jsg.property.JSGProperties;
import dev.tauri.jsg.raycaster.Raycaster;
import dev.tauri.jsg.raycaster.util.RayCastedButton;
import dev.tauri.jsg.util.vectors.Vector3f;
import dev.tauri.jsgtransporters.common.block.controller.RingsAncientCPBlock;
import dev.tauri.jsgtransporters.common.packet.JSGTPacketHandler;
import dev.tauri.jsgtransporters.common.packet.packets.CPButtonClickedToServer;
import dev.tauri.jsgtransporters.common.rings.network.SymbolTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class AncientCPRaycaster extends AbstractCPRaycaster {

    public static final AncientCPRaycaster INSTANCE = new AncientCPRaycaster();
    public static final List<RayCastedButton> BUTTONS = List.of(
            new RayCastedButton(0, List.of(
                    //1st line
                    //Selen
                    new Vector3f(-0.049301f, -0.010011f, 0.170078f),
                    new Vector3f(-0.009128f, -0.010011f, 0.170078f),
                    new Vector3f(-0.009128f, -0.010011f, 0.11672f),
                    new Vector3f(-0.049301f, -0.010011f, 0.11672f)
            )),

            new RayCastedButton(1, List.of(
                    //Tiris
                    new Vector3f(0.009138f, -0.010011f, 0.170078f),
                    new Vector3f(0.049482f, -0.010011f, 0.170078f),
                    new Vector3f(0.049482f, -0.010011f, 0.11672f),
                    new Vector3f(0.009138f, -0.010011f, 0.11672f)
            )),

            new RayCastedButton(2, List.of(
                    //2nd line
                    //Varun
                    new Vector3f(-0.022506f, -0.010011f, 0.098201f),
                    new Vector3f(0.01776f, -0.010011f, 0.098201f),
                    new Vector3f(0.01776f, -0.010011f, 0.045136f),
                    new Vector3f(-0.022506f, -0.010011f, 0.045136f)

            )),

            new RayCastedButton(3, List.of(
                    //3rd line
                    //Elyra
                    new Vector3f(-0.050507f, -0.010011f, 0.027116f),
                    new Vector3f(-0.010374f, -0.010011f, 0.027116f),
                    new Vector3f(-0.010374f, -0.010011f, -0.026522f),
                    new Vector3f(-0.050507f, -0.010011f, -0.026522f)
            )),

            new RayCastedButton(4, List.of(
                    //Ankor
                    new Vector3f(0.010648f, -0.010011f, 0.027116f),
                    new Vector3f(0.050785f, -0.010011f, 0.027116f),
                    new Vector3f(0.050785f, -0.010011f, -0.026522f),
                    new Vector3f(0.010648f, -0.010011f, -0.026522f)
            )),

            new RayCastedButton(5, List.of(
                    //4th line
                    //Synthis
                    new Vector3f(-0.020189f, -0.010011f, -0.044267f),
                    new Vector3f(0.019998f, -0.010011f, -0.044267f),
                    new Vector3f(0.019998f, -0.010011f, -0.097266f),
                    new Vector3f(-0.020189f, -0.010011f, -0.097266f)
            )),

            new RayCastedButton(6, List.of(
                    //5th line
                    //Aurin
                    new Vector3f(-0.068026f, -0.010011f, -0.115882f),
                    new Vector3f(-0.027767f, -0.010011f, -0.115882f),
                    new Vector3f(-0.027767f, -0.010011f, -0.169972f),
                    new Vector3f(-0.068026f, -0.010011f, -0.169972f)
            )),

            new RayCastedButton(7, List.of(
                    //Velar
                    new Vector3f(-0.020189f, -0.010011f, -0.115882f),
                    new Vector3f(0.019943f, -0.010011f, -0.115882f),
                    new Vector3f(0.019943f, -0.010011f, -0.169972f),
                    new Vector3f(-0.020189f, -0.010011f, -0.169972f)
            )),

            new RayCastedButton(8, List.of(
                    //Omnis - Activation Button
                    new Vector3f(0.027856f, -0.010011f, -0.115882f),
                    new Vector3f(0.067878f, -0.010011f, -0.115882f),
                    new Vector3f(0.067878f, -0.010011f, -0.169972f),
                    new Vector3f(0.027856f, -0.010011f, -0.169972f)
            ))
    );

    public static void register() {
        Raycaster.register(RingsAncientCPBlock.class, INSTANCE);
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

    @Override
    public float getScale() {
        return 2f;
    }


    protected static final Vector3f NORTH_TRANSLATION = new Vector3f(0.5f, 0.5f, 0.97f);
    protected static final Vector3f EAST_TRANSLATION = new Vector3f(0.03f, 0.5f, 0.49f);
    protected static final Vector3f SOUTH_TRANSLATION = new Vector3f(0.5f, 0.5f, 0.03f);
    protected static final Vector3f WEST_TRANSLATION = new Vector3f(0.97f, 0.5f, 0.51f);

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
        player.swing(InteractionHand.MAIN_HAND, true);
        JSGTPacketHandler.sendToServer(new CPButtonClickedToServer(pos, SymbolTypeRegistry.ANCIENT.valueOf(button), false));
        return true;
    }
}
