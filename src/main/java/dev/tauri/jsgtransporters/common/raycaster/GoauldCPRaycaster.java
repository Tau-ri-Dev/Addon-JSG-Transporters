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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoauldCPRaycaster extends Raycaster {
    public static final GoauldCPRaycaster INSTANCE = new GoauldCPRaycaster();
    public static final ArrayList<RayCastedButton> BUTTONS = new ArrayList<>() {{
        add(new RayCastedButton(0, Arrays.asList(
                new Vector3f(0.566581f, -0.961707f, 0.387802f),
                new Vector3f(0.565342f, -0.961707f, 0.468747f),
                new Vector3f(0.684695f, -0.961707f, 0.468747f),
                new Vector3f(0.683045f, -0.96172f, 0.387804f)
        )));
        add(new RayCastedButton(1, Arrays.asList(
                new Vector3f(0.315226f, -0.961707f, 0.382972f),
                new Vector3f(0.315226f, -0.961707f, 0.468873f),
                new Vector3f(0.431275f, -0.961707f, 0.46846f),
                new Vector3f(0.432101f, -0.961707f, 0.386276f)
        )));
        add(new RayCastedButton(2, Arrays.asList(
                new Vector3f(0.3144f, -0.961707f, 0.283443f),
                new Vector3f(0.315226f, -0.961707f, 0.382972f),
                new Vector3f(0.432101f, -0.961707f, 0.386276f),
                new Vector3f(0.430864f, -0.96172f, 0.282202f)
        )));
        add(new RayCastedButton(3, Arrays.asList(
                new Vector3f(0.564515f, -0.96172f, 0.28662f),
                new Vector3f(0.566581f, -0.961707f, 0.387802f),
                new Vector3f(0.683045f, -0.96172f, 0.387804f),
                new Vector3f(0.682219f, -0.96172f, 0.285381f)
        )));
        add(new RayCastedButton(4, Arrays.asList(
                new Vector3f(0.566167f, -0.96172f, 0.199477f),
                new Vector3f(0.564515f, -0.96172f, 0.28662f),
                new Vector3f(0.682219f, -0.96172f, 0.285381f),
                new Vector3f(0.682219f, -0.96172f, 0.201129f)
        )));
        add(new RayCastedButton(5, Arrays.asList(
                new Vector3f(0.314399f, -0.96172f, 0.201255f),
                new Vector3f(0.3144f, -0.961707f, 0.283443f),
                new Vector3f(0.430864f, -0.96172f, 0.282202f),
                new Vector3f(0.431277f, -0.96172f, 0.200429f)
        )));
    }};

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
