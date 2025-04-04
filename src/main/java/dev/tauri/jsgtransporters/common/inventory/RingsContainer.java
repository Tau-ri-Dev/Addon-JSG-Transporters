package dev.tauri.jsgtransporters.common.inventory;

import dev.tauri.jsg.screen.inventory.JSGContainer;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class RingsContainer extends JSGContainer {

    public RingsAbstractBE ringsTile;
    private final BlockPos pos;
    public final Inventory playerInventory;

    // Server
    public RingsContainer(int containerID, Inventory playerInventory, BlockEntity baseTile) {
        super(MenuTypeRegistry.RINGS_MENU_TYPE.get(), containerID);
        this.playerInventory = playerInventory;
        if(!(baseTile instanceof RingsAbstractBE rings)) throw new ClassCastException();
        this.ringsTile = rings;
        this.pos = ringsTile.getBlockPos();

        // TODO: Init slots
        // TODO: Create item stack holder (inv) in rings BE and make capabilities

    }

    // Client
    public RingsContainer(int containerID, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerID, playerInventory, playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public ItemStack quickMoveStack(Player player, int slot) {
        // TODO: Handle item stacks
        return ItemStack.EMPTY;
    }
}
