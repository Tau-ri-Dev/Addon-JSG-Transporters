package dev.tauri.jsgtransporters.common.raycaster;

import dev.tauri.jsg.property.JSGProperties;
import dev.tauri.jsg.raycaster.Raycaster;
import dev.tauri.jsg.raycaster.util.RayCastedButton;
import dev.tauri.jsg.util.vectors.Vector3f;
import dev.tauri.jsgtransporters.common.block.controller.RingsOriCPBlock;
import dev.tauri.jsgtransporters.common.packet.JSGTPacketHandler;
import dev.tauri.jsgtransporters.common.packet.packets.CPButtonClickedToServer;
import dev.tauri.jsgtransporters.common.rings.network.SymbolTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class OriCPRaycaster extends AbstractCPRaycaster {

    public static final OriCPRaycaster INSTANCE = new OriCPRaycaster();
    public static final List<RayCastedButton> BUTTONS = List.of(
            new RayCastedButton(0, SymbolTypeRegistry.ORI, List.of(
                    //Left Group of buttons
                    //Pillar
                    new Vector3f(-0.18155f, -0.013004f, -0.082774f),
                    new Vector3f(-0.23455f, -0.013004f, -0.073187f),
                    new Vector3f(-0.21676f, -0.013004f, -0.020849f),
                    new Vector3f(-0.17159f, -0.013004f, -0.041715f)
            )),

            new RayCastedButton(1, SymbolTypeRegistry.ORI, List.of(
                    //Celestia
                    new Vector3f(-0.17099f, -0.013004f, -0.035233f),
                    new Vector3f(-0.21486f, -0.013004f, -0.015658f),
                    new Vector3f(-0.18983f, -0.013004f, 0.025606f),
                    new Vector3f(-0.15499f, -0.013004f, -0.003318f)
            )),

            new RayCastedButton(2, SymbolTypeRegistry.ORI, List.of(
                    //Radiance
                    new Vector3f(-0.15054f, -0.0131004f, 0.002812f),
                    new Vector3f(-0.18673f, -0.0131004f, 0.030345f),
                    new Vector3f(-0.15578f, -0.0131004f, 0.064715f),
                    new Vector3f(-0.12789f, -0.0131004f, 0.02907f)
            )),

            new RayCastedButton(3, SymbolTypeRegistry.ORI, List.of(
                    //Judgment
                    new Vector3f(-0.12233f, -0.013004f, 0.033699f),
                    new Vector3f(-0.15036f, -0.013004f, 0.070006f),
                    new Vector3f(-0.10804f, -0.013004f, 0.100067f),
                    new Vector3f(-0.089672f, -0.013004f, 0.059474f)
            )),

            new RayCastedButton(4, SymbolTypeRegistry.ORI, List.of(
                    //Chalice
                    new Vector3f(-0.084257f, -0.013004f, 0.063524f),
                    new Vector3f(-0.10199f, -0.013004f, 0.10359f),
                    new Vector3f(-0.062934f, -0.013004f, 0.12106f),
                    new Vector3f(-0.063908f, -0.013004f, 0.07371f)
            )),

            new RayCastedButton(5, SymbolTypeRegistry.ORI, List.of(
                    //Top right buttons
                    //Dominion
                    new Vector3f(0.036466f, -0.013004f, 0.103989f),
                    new Vector3f(0.036623f, -0.013004f, 0.135606f),
                    new Vector3f(0.086747f, -0.013004f, 0.113242f),
                    new Vector3f(0.074396f, -0.013004f, 0.086127f)
            )),

            new RayCastedButton(6, SymbolTypeRegistry.ORI, List.of(
                    //Harmony
                    new Vector3f(0.081f, -0.013004f, 0.083001f),
                    new Vector3f(0.092378f, -0.013004f, 0.107348f),
                    new Vector3f(0.120845f, -0.013004f, 0.08729f),
                    new Vector3f(0.104532f, -0.013004f, 0.066361f)
            )),

            new RayCastedButton(7, SymbolTypeRegistry.ORI, List.of(
                    //Path
                    new Vector3f(0.112572f, -0.013004f, 0.05973f),
                    new Vector3f(0.128828f, -0.013004f, 0.081945f),
                    new Vector3f(0.155523f, -0.013004f, 0.057843f),
                    new Vector3f(0.133207f, -0.013004f, 0.039603f)
            )),

            new RayCastedButton(8, SymbolTypeRegistry.ORI, List.of(
                    //Oblivion
                    new Vector3f(0.139377f, -0.013004f, 0.033244f),
                    new Vector3f(0.161538f, -0.013004f, 0.05503f),
                    new Vector3f(0.187955f, -0.013004f, 0.02032f),
                    new Vector3f(0.162571f, -0.013004f, 0.00277f)
            )),

            new RayCastedButton(9, SymbolTypeRegistry.ORI, List.of(
                    //Serenity
                    new Vector3f(0.166224f, -0.013004f, -0.002522f),
                    new Vector3f(0.190218f, -0.013004f, 0.011871f),
                    new Vector3f(0.206586f, -0.013004f, -0.022029f),
                    new Vector3f(0.178788f, -0.013004f, -0.030848f)
            )),

            new RayCastedButton(10, SymbolTypeRegistry.ORI, List.of(
                    //Eternum
                    new Vector3f(0.183035f, -0.013004f, -0.041109f),
                    new Vector3f(0.210613f, -0.013004f, -0.030953f),
                    new Vector3f(0.223276f, -0.013004f, -0.07556f),
                    new Vector3f(0.190311f, -0.013004f, -0.07556f)
            )),

            new RayCastedButton(11, SymbolTypeRegistry.ORI, List.of(
                    //Right bottom buttons
                    //Origin
                    new Vector3f(0.04133f, -0.013004f, 0.054985f),
                    new Vector3f(0.039854f, -0.013004f, 0.085017f),
                    new Vector3f(0.06611f, -0.013004f, 0.074377f),
                    new Vector3f(0.054176f, -0.013004f, 0.048803f)
            )),

            new RayCastedButton(12, SymbolTypeRegistry.ORI, List.of(
                    //Ascension
                    new Vector3f(0.061161f, -0.013004f, 0.044651f),
                    new Vector3f(0.072853f, -0.013004f, 0.071048f),
                    new Vector3f(0.100268f, -0.013004f, 0.051765f),
                    new Vector3f(0.082989f, -0.013004f, 0.028576f)
            )),

            new RayCastedButton(13, SymbolTypeRegistry.ORI, List.of(
                    //Salvation
                    new Vector3f(0.087827f, -0.013004f, 0.024658f),
                    new Vector3f(0.104475f, -0.013004f, 0.047861f),
                    new Vector3f(0.124838f, -0.013004f, 0.027748f),
                    new Vector3f(0.103879f, -0.013004f, 0.009348f)
            )),

            new RayCastedButton(14, SymbolTypeRegistry.ORI, List.of(
                    //Flame
                    new Vector3f(0.110947f, -0.013004f, 0.0004f),
                    new Vector3f(0.131255f, -0.013004f, 0.019662f),
                    new Vector3f(0.14686f, -0.013004f, -0.002191f),
                    new Vector3f(0.122937f, -0.013004f, -0.016823f)
            )),

            new RayCastedButton(15, SymbolTypeRegistry.ORI, List.of(
                    //Convergence
                    new Vector3f(0.12849f, -0.013004f, -0.025304f),
                    new Vector3f(0.1533f, -0.013004f, -0.010561f),
                    new Vector3f(0.166765f, -0.013004f, -0.036136f),
                    new Vector3f(0.138972f, -0.013004f, -0.045024f)
            )),

            new RayCastedButton(16, SymbolTypeRegistry.ORI, List.of(
                    //Vigil
                    new Vector3f(0.142736f, -0.013004f, -0.057193f),
                    new Vector3f(0.170152f, -0.013004f, -0.046017f),
                    new Vector3f(0.179473f, -0.013004f, -0.076373f),
                    new Vector3f(0.147612f, -0.013004f, -0.076373f)
            )),

            new RayCastedButton(17, SymbolTypeRegistry.ORI, List.of(
                    //Crystal - Activation button
                    new Vector3f(-0.010562f, -0.021196f, -0.07804f),
                    new Vector3f(0.015691f, -0.021196f, -0.111748f),
                    new Vector3f(-0.010562f, -0.021196f, -0.143649f),
                    new Vector3f(-0.036658f, -0.021196f, -0.111748f)
            ))
    );

    public static void register() {
        Raycaster.register(RingsOriCPBlock.class, INSTANCE);
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
        return 1.5f;
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
        JSGTPacketHandler.sendToServer(new CPButtonClickedToServer(pos, SymbolTypeRegistry.ORI.valueOf(button), false));
        return true;
    }
}
