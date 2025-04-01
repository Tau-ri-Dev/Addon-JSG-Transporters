package dev.tauri.jsgtransporters.common.block.controller;

import dev.tauri.jsg.block.TickableBEBlock;
import dev.tauri.jsg.helpers.BlockPosHelper;
import dev.tauri.jsg.item.ITabbedItem;
import dev.tauri.jsg.property.JSGProperties;
import dev.tauri.jsgtransporters.common.registry.TabRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class AbstractRingsCPBlock extends TickableBEBlock implements ITabbedItem {
    public AbstractRingsCPBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                defaultBlockState().setValue(JSGProperties.FACING_HORIZONTAL_PROPERTY, Direction.NORTH)
        );
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(JSGProperties.FACING_HORIZONTAL_PROPERTY, BlockPosHelper.rotateDir(blockState.getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY), rotation));
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.setValue(JSGProperties.FACING_HORIZONTAL_PROPERTY, BlockPosHelper.flipDir(blockState.getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY), mirror));
    }

    @Override
    public @Nullable RegistryObject<CreativeModeTab> getTab() {
        return TabRegistry.TAB_RINGS;
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(JSGProperties.FACING_HORIZONTAL_PROPERTY);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Player placer = ctx.getPlayer();
        if (placer == null) return defaultBlockState();
        return defaultBlockState().setValue(JSGProperties.FACING_HORIZONTAL_PROPERTY, placer.getDirection().getOpposite());
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    @Nonnull
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
