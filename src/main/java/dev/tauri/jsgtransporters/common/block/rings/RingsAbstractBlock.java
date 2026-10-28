package dev.tauri.jsgtransporters.common.block.rings;

import dev.tauri.jsgcore.common.block.TickableBEBlock;
import dev.tauri.jsgcore.common.blockstate.JSGProperties;
import dev.tauri.jsgcore.common.helper.BlockPosHelper;
import dev.tauri.jsgcore.common.item.ITabbedItem;
import dev.tauri.jsgcore.common.registry.CoreTabs;
import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.inventory.RingsContainer;
import dev.tauri.jsgtransporters.common.registry.JSGTTabs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

public abstract class RingsAbstractBlock extends TickableBEBlock implements ITabbedItem {

    public static final Properties RINGS_BASE_PROPS = Properties.of()
            .strength(2.5f, 30f)
            .isRedstoneConductor((BlockState state, BlockGetter getter, BlockPos pos) -> true)
            .isViewBlocking((BlockState state, BlockGetter getter, BlockPos pos) -> true)
            .noOcclusion()
            .isRedstoneConductor((BlockState state, BlockGetter getter, BlockPos pos) -> true)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL);

    public RingsAbstractBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                defaultBlockState().setValue(JSGProperties.FACING_HORIZONTAL_PROPERTY, Direction.NORTH)
        );
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player) {
        var result = super.playerWillDestroy(level, pos, blockState, player);
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RingsAbstractBE rings) {
                rings.onBroken();
            }
        }
        return result;
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

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            var be = level.getBlockEntity(pos);
            if (be instanceof RingsAbstractBE rings) {
                if (player instanceof ServerPlayer sp) {
                    sp.openMenu(new SimpleMenuProvider((id, pInv, p) -> new RingsContainer(id, pInv, rings), Component.empty()), pos);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @Nonnull BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(JSGProperties.FACING_HORIZONTAL_PROPERTY, BlockPosHelper.rotateDir(blockState.getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY), rotation));
    }

    @Override
    @ParametersAreNonnullByDefault
    public @Nonnull BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.setValue(JSGProperties.FACING_HORIZONTAL_PROPERTY, BlockPosHelper.flipDir(blockState.getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY), mirror));
    }

    @Override
    public List<Supplier<RegistryObjectWrapper<CreativeModeTab>>> getTabs() {
        return List.of(() -> JSGTTabs.TAB_RINGS, CoreTabs.TAB_TRANSPORTATION);
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
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
    @ParametersAreNonnullByDefault
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if (level.isClientSide) return;
        BlockEntity e = level.getBlockEntity(blockPos);
        if (e instanceof RingsAbstractBE be) {
            be.updateLinkStatus();
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            if (pLevel.getBlockEntity(pPos) instanceof RingsAbstractBE rings) {
                rings.updateRedstonePower(pLevel.hasNeighborSignal(pPos));
            }
        }
    }
}
