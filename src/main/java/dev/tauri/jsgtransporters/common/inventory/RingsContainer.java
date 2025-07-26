package dev.tauri.jsgtransporters.common.inventory;

import dev.tauri.jsg.forgeutil.SlotHandler;
import dev.tauri.jsg.item.energy.CapacitorItemBlock;
import dev.tauri.jsg.packet.JSGPacketHandler;
import dev.tauri.jsg.packet.packets.StateUpdatePacketToClient;
import dev.tauri.jsg.power.general.LargeEnergyStorage;
import dev.tauri.jsg.screen.inventory.JSGContainer;
import dev.tauri.jsg.screen.inventory.OpenTabHolderInterface;
import dev.tauri.jsg.screen.util.ContainerHelper;
import dev.tauri.jsg.stargate.network.SymbolTypeEnum;
import dev.tauri.jsg.state.StateTypeEnum;
import dev.tauri.jsg.util.CreativeItemsChecker;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.registry.MenuTypeRegistry;
import dev.tauri.jsgtransporters.common.rings.network.AddressTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RingsContainer extends JSGContainer implements OpenTabHolderInterface {

    public RingsAbstractBE ringsTile;
    private final BlockPos pos;
    public final Inventory playerInventory;
    public boolean hasCreative;
    private int lastEnergyStored;
    private int energyTransferedLastTick;
    private float lastEnergySecondsToClose;
    private int lastProgress;
    protected final List<Integer> openedTabsSlotsIds = new ArrayList<>();

    // Server
    public RingsContainer(int containerID, Inventory playerInventory, BlockEntity baseTile) {
        super(MenuTypeRegistry.RINGS_MENU_TYPE.get(), containerID);
        this.playerInventory = playerInventory;
        if (!(baseTile instanceof RingsAbstractBE rings)) throw new ClassCastException();
        this.ringsTile = rings;
        this.hasCreative = playerInventory.player.isCreative();
        this.pos = ringsTile.getBlockPos();
        IItemHandler itemHandler = ringsTile.getItemHandler();

        for (int col = 0; col < 4; col++) {
            addSlot(new SlotHandler(itemHandler, col, 9 + 18 * col, 27));
        }

        // Capacitors 1x3 (index 4-6)
        for (int col = 0; col < 3; col++) {
            final int capacitorIndex = col;
            addSlot(new SlotHandler(itemHandler, col + 4, 115 + 18 * col, 27) {
                @Override
                public boolean isActive() {
                    // hasItem() is a compatibility thing for when players already had their capacitors in the gate.
                    return (capacitorIndex + 1 <= ringsTile.getSupportedCapacitors()) || hasItem();
                }
            });
        }

        // Page slots (index 7-9)
        for (int i = 0; i < SymbolTypeEnum.values(AddressTypeRegistry.RINGS_SYMBOLS).length; i++) {
            addSlot(new SlotHandler(itemHandler, i + 7, -22, 89 + 22 * i));
        }

        // Biome overlay slot (index 10)
        addSlot(new SlotHandler(itemHandler, 10, 0, 0));

        for (Slot slot : ContainerHelper.generatePlayerSlots(playerInventory, 91))
            addSlot(slot);

    }

    // Client
    public RingsContainer(int containerID, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerID, playerInventory, playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    @Override
    public void setData(int id, int data) {
        ringsTile.setPageProgress(data);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack stack = getSlot(index).getItem();

        if (!CreativeItemsChecker.canInteractWith(stack, hasCreative)) return ItemStack.EMPTY;

        // Transfering from Stargate to player's inventory
        if (index < 11) {
            if (!moveItemStackTo(stack, 11, slots.size(), false)) {
                return ItemStack.EMPTY;
            }
            getSlot(index).set(ItemStack.EMPTY);
            setRemoteSlot(index, ItemStack.EMPTY);
        }

        // Transfering from player's inventory to Stargate
        else {
            var openedSlots = getOpenTabsSlotsIds();
            var biomeSlotId = 7 + SymbolTypeEnum.values(AddressTypeRegistry.RINGS_SYMBOLS).length;
            var addressSlots = openedSlots.stream().filter(slot -> (
                    slot >= 7 && slot <= (6 + SymbolTypeEnum.values(AddressTypeRegistry.RINGS_SYMBOLS).length)
                            && ringsTile.getItemHandler().isItemValid(slot, stack) && !getSlot(slot).hasItem()
            )).toList();

            // Capacitors
            if (stack.getItem() instanceof CapacitorItemBlock) {
                for (int i = 4; i < 7; i++) {
                    if (!getSlot(i).hasItem() && getSlot(i).mayPlace(stack)) {
                        ItemStack stack1 = stack.copy();
                        stack1.setCount(1);

                        setRemoteSlot(i, stack1);
                        getSlot(i).set(stack1);
                        stack.shrink(1);

                        return stack;
                    }
                }
            } else if (RingsAbstractBE.RingsUpgradeEnum.contains(stack.getItem()) && !ringsTile.hasUpgrade(stack.getItem())) {
                var upgrade = RingsAbstractBE.RingsUpgradeEnum.valueOf(stack.getItem());
                for (int i = 0; i < 4; i++) {
                    if (!getSlot(i).hasItem() && i == upgrade.slot) {
                        ItemStack stack1 = stack.copy();
                        stack1.setCount(1);

                        setRemoteSlot(i, stack1);
                        getSlot(i).set(stack1);
                        stack.shrink(1);

                        return ItemStack.EMPTY;
                    }
                }
            } else if (!addressSlots.isEmpty()) {
                var s = addressSlots.get(0);
                ItemStack stack1 = stack.copy();
                stack1.setCount(1);

                setRemoteSlot(s, stack1);
                getSlot(s).set(stack1);
                stack.shrink(1);

                return ItemStack.EMPTY;
            }

            // Biome override blocks
            else if (openedSlots.contains(biomeSlotId) && ringsTile.getItemHandler().isItemValid(biomeSlotId, stack)) {
                if (!getSlot(biomeSlotId).hasItem()) {
                    ItemStack stack1 = stack.copy();
                    stack1.setCount(1);

                    setRemoteSlot(biomeSlotId, stack1);
                    getSlot(biomeSlotId).set(stack1);
                    stack.shrink(1);

                    return ItemStack.EMPTY;
                }
            }

            return ItemStack.EMPTY;
        }

        return stack;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        try {
            if (slotId >= 0 && slotId < slots.size() && !CreativeItemsChecker.canInteractWith(getSlot(slotId).getItem(), hasCreative))
                return;
        } catch (Exception ignored) {
        }
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        LargeEnergyStorage energyStorage = (LargeEnergyStorage) ringsTile.getCapability(ForgeCapabilities.ENERGY, null).resolve().orElseThrow();

        if (lastEnergyStored != Objects.requireNonNull(energyStorage).getEnergyStoredInternally()
                || energyTransferedLastTick != ringsTile.getEnergyTransferredLastTick()
                || lastProgress != ringsTile.getPageProgress()

        ) {
            if (playerInventory.player instanceof ServerPlayer sp)
                JSGPacketHandler.sendTo(new StateUpdatePacketToClient(pos, StateTypeEnum.GUI_UPDATE, ringsTile.getState(StateTypeEnum.GUI_UPDATE)), sp);

            lastEnergyStored = energyStorage.getEnergyStoredInternally();
            energyTransferedLastTick = ringsTile.getEnergyTransferredLastTick();
            lastProgress = ringsTile.getPageProgress();
        }
    }

    @Override
    public void addSlotListener(@Nonnull ContainerListener listener) {
        super.addSlotListener(listener);

        if (listener instanceof ServerPlayer)
            JSGPacketHandler.sendTo(new StateUpdatePacketToClient(pos, StateTypeEnum.GUI_STATE, ringsTile.getState(StateTypeEnum.GUI_STATE)), (ServerPlayer) listener);
    }

    @Override
    public List<Integer> getOpenTabsSlotsIds() {
        return openedTabsSlotsIds;
    }

    @Override
    public void modifyOpenTabSlotId(int slotId, boolean add) {
        if (add) openedTabsSlotsIds.add(slotId);
        else openedTabsSlotsIds.removeIf(v -> v == slotId);
    }
}
