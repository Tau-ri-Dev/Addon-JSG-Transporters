package dev.tauri.jsgtransporters.common.block.rings;

import dev.tauri.jsg.block.TickableBEBlock;
import dev.tauri.jsg.helpers.BlockPosHelper;
import dev.tauri.jsg.item.ITabbedItem;
import dev.tauri.jsg.property.JSGProperties;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.registry.TabRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class RingsAbstractBlock extends TickableBEBlock implements ITabbedItem {

    public static final Properties RINGS_BASE_PROPS = Properties.of();

    public RingsAbstractBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                defaultBlockState().setValue(JSGProperties.FACING_HORIZONTAL_PROPERTY, Direction.NORTH)
        );
    }

    @Override
    @ParametersAreNonnullByDefault
    public void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player) {
        super.playerWillDestroy(level, pos, blockState, player);
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RingsAbstractBE rings) {
                rings.onBroken();
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        super.wasExploded(level, pos, explosion);
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RingsAbstractBE rings) {
                rings.onBroken();
            }
        }
    }

    @NotNull
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            var be = level.getBlockEntity(pos);
            if (be instanceof RingsAbstractBE rings) {
                rings.test();
            }
        }
        return InteractionResult.FAIL;
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

    @Nullable
    @Override
    public RegistryObject<CreativeModeTab> getTab() {
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
}
