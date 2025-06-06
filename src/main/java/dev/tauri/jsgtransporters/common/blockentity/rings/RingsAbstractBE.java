package dev.tauri.jsgtransporters.common.blockentity.rings;

import dev.tauri.jsg.JSG;
import dev.tauri.jsg.blockentity.IAddressProvider;
import dev.tauri.jsg.blockentity.IPreparable;
import dev.tauri.jsg.blockentity.util.IUpgradable;
import dev.tauri.jsg.blockentity.util.ScheduledTask;
import dev.tauri.jsg.blockentity.util.ScheduledTaskExecutorInterface;
import dev.tauri.jsg.chunkloader.ChunkManager;
import dev.tauri.jsg.config.ingame.ITileConfig;
import dev.tauri.jsg.config.ingame.JSGConfigOption;
import dev.tauri.jsg.config.ingame.JSGIntRangeConfigOption;
import dev.tauri.jsg.config.ingame.JSGTileEntityConfig;
import dev.tauri.jsg.config.stargate.StargateDimensionConfig;
import dev.tauri.jsg.helpers.BlockPosHelper;
import dev.tauri.jsg.helpers.LinkingHelper;
import dev.tauri.jsg.integration.ComputerDeviceHolder;
import dev.tauri.jsg.integration.ComputerDeviceProvider;
import dev.tauri.jsg.item.energy.CapacitorItemBlock;
import dev.tauri.jsg.item.notebook.PageNotebookItemFilled;
import dev.tauri.jsg.packet.JSGPacketHandler;
import dev.tauri.jsg.packet.packets.StateUpdatePacketToClient;
import dev.tauri.jsg.packet.packets.StateUpdateRequestToServer;
import dev.tauri.jsg.power.general.LargeEnergyStorage;
import dev.tauri.jsg.registry.BlockRegistry;
import dev.tauri.jsg.sound.JSGSoundHelper;
import dev.tauri.jsg.stargate.BiomeOverlayRegistry;
import dev.tauri.jsg.stargate.EnumScheduledTask;
import dev.tauri.jsg.stargate.network.IAddress;
import dev.tauri.jsg.stargate.network.SymbolInterface;
import dev.tauri.jsg.stargate.network.SymbolTypeEnum;
import dev.tauri.jsg.state.State;
import dev.tauri.jsg.state.StateProviderInterface;
import dev.tauri.jsg.state.StateTypeEnum;
import dev.tauri.jsg.state.stargate.StargateBiomeOverrideState;
import dev.tauri.jsg.util.*;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.blockentity.controller.AbstractRingsCPBE;
import dev.tauri.jsgtransporters.common.config.BlockConfigOptionRegistry;
import dev.tauri.jsgtransporters.common.energy.EnergyRequiredToOperateRings;
import dev.tauri.jsgtransporters.common.helpers.TeleportHelper;
import dev.tauri.jsgtransporters.common.registry.ItemRegistry;
import dev.tauri.jsgtransporters.common.registry.SoundRegistry;
import dev.tauri.jsgtransporters.common.registry.TagsRegistry;
import dev.tauri.jsgtransporters.common.rings.RingsConnectResult;
import dev.tauri.jsgtransporters.common.rings.network.*;
import dev.tauri.jsgtransporters.common.state.gui.RingsContainerGuiState;
import dev.tauri.jsgtransporters.common.state.gui.RingsContainerGuiUpdate;
import dev.tauri.jsgtransporters.common.state.renderer.RingsRendererState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.StreamSupport;

public abstract class RingsAbstractBE extends BlockEntity implements ILinkable<AbstractRingsCPBE>, IUpgradable, ITileConfig, IAddressProvider, ITickable, ComputerDeviceProvider, ScheduledTaskExecutorInterface, StateProviderInterface, IPreparable {

    public RingsAbstractBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    protected Map<SymbolTypeEnum<?>, RingsAddress> addressMap = new HashMap<>();
    protected RingsPos ringsPos;
    protected int verticalOffset = 0;
    protected RingsAddressDynamic dialedAddress = new RingsAddressDynamic(getSymbolType());

    public static final int BIOME_OVERRIDE_SLOT = 10;

    protected final JSGItemStackHandler inventory = new JSGItemStackHandler(11) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            Item item = stack.getItem();
            boolean isItemCapacitor = (item instanceof CapacitorItemBlock);
            switch (slot) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return RingsUpgradeEnum.contains(item) && !hasUpgrade(item) && RingsUpgradeEnum.valueOf(item).slot == slot;

                case 4:
                case 5:
                case 6:
                    return isItemCapacitor && getSupportedCapacitors() >= (slot - 3);

                case 7:
                case 8:
                case 9:
                    return item == dev.tauri.jsg.registry.ItemRegistry.NOTEBOOK_PAGE_EMPTY.get() || item == dev.tauri.jsg.registry.ItemRegistry.NOTEBOOK_PAGE_FILLED.get();

                case BIOME_OVERRIDE_SLOT:
                    var override = BiomeOverlayRegistry.getBiomeOverlayByItem(stack);
                    if (override == null) return false;

                    return getSupportedOverlays().contains(override);
                default:
                    return true;
            }
        }

        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);

            switch (slot) {
                case 4:
                case 5:
                case 6:
                    updatePowerTier();
                    break;

                case BIOME_OVERRIDE_SLOT:
                    sendState(StateTypeEnum.BIOME_OVERRIDE_STATE, new StargateBiomeOverrideState(determineBiomeOverride()));
                    break;
                default:
                    break;
            }

            setChanged();
        }
    };

    // Server
    private BiomeOverlayRegistry.BiomeOverlayInstance determineBiomeOverride() {
        ItemStack stack = inventory.getStackInSlot(BIOME_OVERRIDE_SLOT);

        if (stack.isEmpty()) {
            return null;
        }

        var biomeOverlay = BiomeOverlayRegistry.getBiomeOverlayByItem(stack);

        if (getSupportedOverlays().contains(biomeOverlay)) {
            return biomeOverlay;
        }

        return null;
    }

    public int getSupportedCapacitors() {
        return ((JSGIntRangeConfigOption) getConfig().getOption("maxCapacitors")).getValue();
    }

    public enum RingsUpgradeEnum implements EnumKeyInterface<Item> {
        GOAULD_GLYPHS(ItemRegistry.CRYSTAL_GLYPH_GOAULD.get(), 0),
        ORI_GLYPHS(ItemRegistry.CRYSTAL_GLYPH_ORI.get(), 1),
        ANCIENT_GLYPHS(ItemRegistry.CRYSTAL_GLYPH_ANCIENT.get(), 2),
        DIMENSIONAL_TUNNELING(ItemRegistry.CRYSTAL_UPGRADE_DIM_TUNNELING.get(), 3);

        public final Item item;
        public final int slot;

        RingsUpgradeEnum(Item item, int slot) {
            this.item = item;
            this.slot = slot;
        }

        @Override
        public Item getKey() {
            return item;
        }

        private static final EnumKeyMap<Item, RingsUpgradeEnum> idMap = new EnumKeyMap<>(values());

        public static RingsUpgradeEnum valueOf(Item item) {
            return idMap.valueOf(item);
        }

        public static boolean contains(Item item) {
            return idMap.contains(item);
        }
    }

    public boolean canTransportCrossDim() {
        return inventory.getStackInSlot(3).getItem() == RingsUpgradeEnum.DIMENSIONAL_TUNNELING.item;
    }

    private final LargeEnergyStorage energyStorage = new LargeEnergyStorage() {

        @Override
        protected void onEnergyChanged() {
            setChanged();
        }
    };

    private int energyStoredLastTick = 0;
    protected int energyTransferredLastTick = 0;

    public int getEnergyTransferredLastTick() {
        return energyTransferredLastTick;
    }

    public LargeEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public int getEnergyStored() {
        return getEnergyStorage().getEnergyStored();
    }

    public int currentPowerTier = 1;

    public int getPowerTier() {
        return currentPowerTier;
    }

    public void updatePowerTier() {
        int powerTier = 1;

        for (int i = 4; i < 7; i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                powerTier++;
            }
        }

        if (powerTier != currentPowerTier) {
            currentPowerTier = powerTier;

            energyStorage.clearStorages();

            for (int i = 4; i < 7; i++) {
                ItemStack stack = inventory.getStackInSlot(i);

                if (!stack.isEmpty()) {
                    LazyOptional<IEnergyStorage> capCapability = stack.getCapability(ForgeCapabilities.ENERGY, null);
                    if (capCapability.isPresent() && capCapability.resolve().isPresent()) {
                        energyStorage.addStorage(capCapability.resolve().get());
                    }
                }
            }

            JSG.logger.debug("Updated to power tier: {}", powerTier);
        }
    }

    // -----------------------------------------------------------------------------
    // Page conversion

    private short pageProgress = 0;
    private int pageSlotId;
    private boolean doPageProgress;
    private ScheduledTask givePageTask;
    private boolean lockPage;

    @Override
    public int getPageProgress() {
        return pageProgress;
    }

    public void setPageProgress(int pageProgress) {
        this.pageProgress = (short) pageProgress;
    }

    @Override
    public boolean prepareBE() {
        this.needRegenerate = true;
        setChanged();
        return true;
    }

    @Nonnull
    public Level getLevelNotNull() {
        return Objects.requireNonNull(getLevel());
    }

    public long getTime() {
        return getLevelNotNull().getGameTime();
    }

    protected TargetPoint targetPoint;
    protected BlockPos pos;

    @Override
    public void onLoad() {
        this.pos = getBlockPos();
        if (level != null) {
            if (!level.isClientSide) {
                this.targetPoint = new TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 512, Objects.requireNonNull(getLevel()).dimension());

                generateAddresses(false);
            } else {
                JSGPacketHandler.sendToServer(new StateUpdateRequestToServer(getBlockPos(), StateTypeEnum.RENDERER_STATE));
                JSGPacketHandler.sendToServer(new StateUpdateRequestToServer(getBlockPos(), StateTypeEnum.GUI_STATE));
            }
        }
        super.onLoad();
    }

    private boolean needRegenerate = false;

    private boolean addedToNetwork;

    @Override
    public void tick() {
        // Scheduled tasks
        ScheduledTask.iterate(scheduledTasks, getTime());
        if (!getLevelNotNull().isClientSide) {
            if (!addedToNetwork) {
                addedToNetwork = true;
                getDeviceHolder().connectToWirelessNetwork();
            }

            if (givePageTask != null) {
                if (givePageTask.update(getTime())) {
                    givePageTask = null;
                }
            }

            if (doPageProgress) {
                if (getTime() % 2 == 0) {
                    pageProgress++;

                    if (pageProgress > 18) {
                        pageProgress = 0;
                        doPageProgress = false;
                    }
                }

                if (inventory.getStackInSlot(pageSlotId).isEmpty()) {
                    lockPage = false;
                    doPageProgress = false;
                    pageProgress = 0;
                    givePageTask = null;
                }
            } else {
                if (lockPage && inventory.getStackInSlot(pageSlotId).isEmpty()) {
                    lockPage = false;
                }

                if (!lockPage) {
                    for (int i = 7; i < 10; i++) {
                        if (!inventory.getStackInSlot(i).isEmpty()) {
                            doPageProgress = true;
                            lockPage = true;
                            pageSlotId = i;
                            givePageTask = new ScheduledTask(EnumScheduledTask.STARGATE_GIVE_PAGE, 36);
                            givePageTask.setTaskCreated(getTime());
                            givePageTask.setExecutor(this);

                            break;
                        }
                    }
                }
            }

            if (getEnergyStorage().getEnergyStored() != energyStoredLastTick)
                setChanged();
            energyTransferredLastTick = (getEnergyStorage().getEnergyStored() - energyStoredLastTick);
            energyStoredLastTick = getEnergyStorage().getEnergyStored();
        } else {
            // Client -> request to update client config & request addresses from the server
            if (getConfig() == null || getConfig().getOptions().isEmpty() || addressMap.isEmpty()) {
                JSGPacketHandler.sendToServer(new StateUpdateRequestToServer(getBlockPos(), StateTypeEnum.GUI_STATE));
            }
        }
    }

    public void onBroken() {
        initRingsPos();
        setBorderBlocks(true, false);
        RingsNetwork.INSTANCE.removeRings(ringsPos);
        if (isLinked()) {
            Objects.requireNonNull(getLinkedDevice()).setLinkedDevice(null);
        }
        setLinkedDevice(null);
    }

    @Nullable
    public RingsAddress getRingsAddress(SymbolTypeEnum<?> symbolType) {
        if (addressMap == null) return null;

        return addressMap.get(symbolType);
    }

    @Override
    public IAddress getAddress(SymbolTypeEnum<?> symbolTypeEnum) {
        return getRingsAddress(symbolTypeEnum);
    }

    @Override
    public int getOriginId() {
        return 0;
    }

    public void generateAddresses(boolean reset) {
        if (reset && ringsPos != null)
            RingsNetwork.INSTANCE.removeRings(ringsPos);
        Random random = new Random(pos.hashCode() * 31L + getLevelNotNull().dimension().location().hashCode());

        for (SymbolTypeEnum<?> symbolType : SymbolTypeEnum.values(AddressTypeRegistry.RINGS_SYMBOLS)) {
            var address = getRingsAddress(symbolType);

            if (address == null || reset) {
                do {
                    address = new RingsAddress(symbolType);
                    address.generate(random);
                } while (RingsNetwork.INSTANCE.getAllAddresses().contains(address));
            }

            this.setRingsAddress(symbolType, address);
        }
    }

    public abstract SymbolTypeEnum<?> getSymbolType();

    protected void initRingsPos() {
        var oldPos = ringsPos;
        ringsPos = new RingsPos(getLevelNotNull().dimension(), getBlockPos(), getSymbolType());
        if (oldPos != null)
            ringsPos.setName(oldPos.getName());
    }

    public void setRingsAddress(SymbolTypeEnum<?> symbolType, RingsAddress address) {
        initRingsPos();
        addressMap.put(symbolType, address);
        RingsNetwork.INSTANCE.putRings(address, ringsPos);
        setChanged();
    }

    public void renameRings(String newName) {
        initRingsPos();
        RingsNetwork.INSTANCE.renameRings(ringsPos, newName);
        ringsPos.setName(newName);
        setChanged();
    }

    public String getRingsName() {
        initRingsPos();
        return ringsPos.getName();
    }


    // ------------------------------------------------------------------------
    // Scheduled tasks

    /**
     * List of scheduled tasks to be performed on {@link ITickable#tick()()}.
     */
    protected List<ScheduledTask> scheduledTasks = new ArrayList<>();

    @Override
    public void addTask(ScheduledTask task) {
        task.setExecutor(this);
        task.setTaskCreated(getTime());
        scheduledTasks.add(task);
        setChanged();
    }

    @Override
    public void executeTask(EnumScheduledTask task, CompoundTag context) {
        switch (task) {
            case RINGS_START_ANIMATION:
                if (level == null) break;
                if (level.isClientSide()) break;
                if (context == null) {
                    context = new CompoundTag();
                    context.putBoolean("start", true);
                    addTask(new ScheduledTask(task, (int) (1.67f * 20), context));
                    JSGSoundHelper.playSoundEvent(level, getBlockPos(), SoundRegistry.RINGS_TRANSPORT_START);

                    context = new CompoundTag();
                    context.putBoolean("playEnd", true);
                    addTask(new ScheduledTask(task, (int) (4.37 * 20), context));
                } else {
                    if (context.getBoolean("playEnd")) {
                        JSGSoundHelper.playSoundEvent(level, getBlockPos(), SoundRegistry.RINGS_TRANSPORT_END);
                    } else if (context.getBoolean("start")) {
                        rendererState.startAnimation(level.getGameTime());
                        setChanged();
                        getAndSendState(StateTypeEnum.RENDERER_STATE);
                        context = new CompoundTag();
                        context.putBoolean("end", true);
                        addTask(new ScheduledTask(task, RING_ANIMATION_LENGTH, context));
                        addTask(new ScheduledTask(EnumScheduledTask.RINGS_SOLID_BLOCKS, 10));
                        addTask(new ScheduledTask(EnumScheduledTask.RINGS_SOLID_BLOCKS, RING_ANIMATION_LENGTH, new CompoundTag()));

                        var offset = getVerticalOffset();
                        for (int i = 0; i < 3; i++) {
                            context = new CompoundTag();
                            context.putBoolean("tp", true);
                            context.putInt("index", i);
                            context.putBoolean("isLast", i == (offset > 0 ? 0 : 2));
                            addTask(new ScheduledTask(task, 45 + ((offset > 0 ? (2 - i) : i) * 8), context));
                        }
                        break;
                    } else if (context.getBoolean("end")) {
                        ChunkManager.unforceChunk((ServerLevel) level, new ChunkPos(getBlockPos()));
                        busy = false;
                        targetRings = null;
                        break;
                    } else if (context.getBoolean("tp") && context.contains("index")) {
                        if (targetRings == null) break;
                        teleportVolumes(context.getInt("index"), context.getBoolean("isLast"));
                        break;
                    }
                }
                break;
            case RINGS_SOLID_BLOCKS:
                setBorderBlocks(context != null, false);
                break;
            case STARGATE_GIVE_PAGE:
                if (pageSlotId < 7) break;
                SymbolTypeEnum<?> symbolType = null;
                switch (pageSlotId) {
                    case 7:
                        symbolType = SymbolTypeRegistry.GOAULD;
                        break;
                    case 8:
                        symbolType = SymbolTypeRegistry.ANCIENT;
                        break;
                    case 9:
                        symbolType = SymbolTypeRegistry.ORI;
                        break;
                    default:
                        break;
                }
                if (symbolType == null) break;
                var stack = getAddressPage(symbolType, new int[]{1, 2, 3, 4, 9});
                inventory.setStackInSlot(pageSlotId, stack);

                break;
            default:
                break;
        }
        setChanged();
    }

    public ItemStack getAddressPage(SymbolTypeEnum<?> symbolType, int[] symbolsToDisplay) {
        JSG.logger.info("Giving Notebook page of address {}", symbolType);

        CompoundTag compound = PageNotebookItemFilled.getCompoundFromAddress(addressMap.get(symbolType), symbolsToDisplay, PageNotebookItemFilled.getRegistryPathFromWorld(getLevelNotNull(), pos), getOriginId(), AddressTypeRegistry.RINGS_ADDRESS_TYPE);

        var stack = new ItemStack(dev.tauri.jsg.registry.ItemRegistry.NOTEBOOK_PAGE_FILLED.get(), 1);
        stack.setTag(compound);
        return stack;
    }

    protected boolean setBorderBlocks(boolean clear, boolean simulate) {
        if (level == null || level.isClientSide()) return false;
        if (clear && simulate) return true;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (x != -2 && x != 2 && z != 2 && z != -2) continue;
                for (int y = 0; y < 3; y++) {
                    var pos = getBlockPos().offset(x, getVerticalOffset() + y, z);
                    var state = level.getBlockState(pos);
                    if (clear) {
                        if (state.getBlock() != BlockRegistry.INVISIBLE_BLOCK.get()) continue;
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                        continue;
                    }
                    if (!state.canBeReplaced()) return false;
                    if (simulate) continue;
                    var ctx = new BlockPlaceContext(level, null, InteractionHand.MAIN_HAND, ItemStack.EMPTY, new BlockHitResult(pos.getCenter(), Direction.UP, pos, false));
                    var stateNew = BlockRegistry.INVISIBLE_BLOCK.get().getStateForPlacement(ctx);
                    if (stateNew == null) stateNew = BlockRegistry.INVISIBLE_BLOCK.get().defaultBlockState();
                    level.setBlock(pos, stateNew, 3);
                }
            }
        }
        return true;
    }

    @Override
    public void sendState(StateTypeEnum type, State state) {
        if (Objects.requireNonNull(getLevel()).isClientSide) {
            return;
        }
        if (targetPoint != null) {
            JSGPacketHandler.sendToClient(new StateUpdatePacketToClient(getBlockPos(), type, state), targetPoint);
        } else {
            JSGTransporters.logger.debug("targetPoint as null trying to send {} from {}", this, this.getClass().getCanonicalName());
        }
    }

    protected final ComputerDeviceHolder deviceHolder = new ComputerDeviceHolder(this);

    @Override
    public ComputerDeviceHolder getDeviceHolder() {
        return deviceHolder;
    }

    public RingsRendererState rendererState = new RingsRendererState();

    public RingsRendererState getRendererStateClient() {
        return rendererState;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public State getState(@NotNull StateTypeEnum stateType) {
        return stateType.stateSupplier()
                .tryType(StateTypeEnum.GUI_STATE, () -> new RingsContainerGuiState(addressMap, getConfig()))
                .tryType(StateTypeEnum.GUI_UPDATE, () -> new RingsContainerGuiUpdate(energyStorage.getEnergyStoredInternally(), energyTransferredLastTick, pageProgress))
                .tryType(StateTypeEnum.RENDERER_STATE, () -> {
                    var state = getRendererStateClient();
                    state.verticalOffset = verticalOffset;
                    return state;
                })
                .orElseThrow(this);
    }

    @Override
    public State createState(@NotNull StateTypeEnum stateType) {
        return stateType.stateSupplier()
                .tryType(StateTypeEnum.GUI_STATE, RingsContainerGuiState::new)
                .tryType(StateTypeEnum.GUI_UPDATE, RingsContainerGuiUpdate::new)
                .tryType(StateTypeEnum.RENDERER_STATE, RingsRendererState::new)
                .orElseThrow(this);
    }

    @Override
    public void setState(@NotNull StateTypeEnum stateType, @NotNull State state) {
        stateType.stateExecutor()
                .tryType(StateTypeEnum.RENDERER_STATE, () -> {
                    rendererState = (RingsRendererState) state;
                    verticalOffset = rendererState.verticalOffset;
                    setChanged();
                })
                .tryType(StateTypeEnum.GUI_STATE, () -> {
                    var guiState = (RingsContainerGuiState) state;
                    addressMap = guiState.addressMap;
                    setConfig(guiState.config);
                    setChanged();
                })
                .tryType(StateTypeEnum.GUI_UPDATE, () -> {
                    RingsContainerGuiUpdate guiUpdate = (RingsContainerGuiUpdate) state;
                    energyStorage.setEnergyStoredInternally(guiUpdate.energyStored);
                    energyTransferredLastTick = guiUpdate.transferedLastTick;
                    pageProgress = (short) guiUpdate.pageProgress;
                    setChanged();
                })
                .run();
    }


    public static final int RING_ANIMATION_LENGTH = (int) (20 * 4.91f);

    // ------------------------------------------------------------------------
    // NBT
    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        for (var address : addressMap.values()) {
            compound.put("address_" + address.getSymbolType(), address.serializeNBT());
        }
        if (isLinked(true))
            compound.putLong("linkedPos", linkedPos.asLong());
        compound.putBoolean("busy", busy);
        if (targetRings != null)
            compound.put("targetRings", targetRings.serializeNBT());
        compound.put("scheduledTasks", ScheduledTask.serializeList(scheduledTasks));

        compound.put("config", getConfig().serializeNBT());
        compound.put("itemHandler", inventory.serializeNBT());
        compound.putInt("verticalOffset", verticalOffset);

        if (energyToOperate != null) {
            compound.putInt("energyToOperate_start", energyToOperate.energyToOpen);
            compound.putInt("energyToOperate_teleport", energyToOperate.keepAlive);
        }

        super.saveAdditional(compound);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        for (SymbolTypeEnum<?> symbolType : SymbolTypeEnum.values(AddressTypeRegistry.RINGS_SYMBOLS)) {
            if (compound.contains("address_" + symbolType))
                addressMap.put(symbolType, new RingsAddress(compound.getCompound("address_" + symbolType)));
        }
        if (compound.contains("linkedPos"))
            linkedPos = BlockPos.of(compound.getLong("linkedPos"));
        busy = compound.getBoolean("busy");
        if (compound.contains("targetRings"))
            targetRings = new RingsPos(compound.getCompound("targetRings"));
        ScheduledTask.deserializeList(compound.getCompound("scheduledTasks"), scheduledTasks, this);

        getConfig().deserializeNBT(compound.getCompound("config"));
        inventory.deserializeNBT(compound.getCompound("itemHandler"));
        verticalOffset = compound.getInt("verticalOffset");

        if (compound.contains("energyToOperate_start")) {
            energyToOperate = new EnergyRequiredToOperateRings(compound.getInt("energyToOperate_start"), compound.getInt("energyToOperate_teleport"));
        }
    }

    private BlockPos linkedPos;

    @Override
    public boolean canLinkTo() {
        return (linkedPos == null);
    }

    @Override
    public void setLinkedDevice(BlockPos blockPos) {
        linkedPos = blockPos;
        setChanged();
    }

    @Override
    public @Nullable AbstractRingsCPBE getLinkedDevice() {
        if (level == null) return null;
        if (linkedPos == null) return null;
        if (level.getBlockEntity(linkedPos) instanceof AbstractRingsCPBE cp) return cp;
        return null;
    }

    @Override
    public @Nullable BlockPos getLinkedPos() {
        return linkedPos;
    }

    public abstract TagKey<Block> getControlPanelBlocks();

    public void updateLinkStatus() {
        pos = getBlockPos();
        var block = getControlPanelBlocks();
        if (block == null) return;
        BlockPos closestCP = LinkingHelper.findClosestUnlinked(getLevelNotNull(), pos, LinkingHelper.getDhdRange(), block);

        if (closestCP != null && getLevelNotNull().getBlockEntity(closestCP) instanceof AbstractRingsCPBE be) {
            be.setLinkedDevice(pos);
            setLinkedDevice(closestCP);
            setChanged();
        }
    }

    @Nullable
    public RingsConnectResult addSymbolToAddress(SymbolInterface symbol) {
        dialedAddress.addSymbol(symbol);
        if (dialedAddress.getSize() > 4 || symbol.origin()) {
            var result = tryConnect();
            dialedAddress.clear();
            return result;
        }
        return null;
    }

    public boolean busy = false;
    public RingsPos targetRings;
    public boolean outbound = false;

    @NotNull
    public RingsConnectResult tryConnect() {
        if (level == null || level.isClientSide()) return RingsConnectResult.CLIENT;
        RingsPos rings;
        if (dialedAddress.size() == 1 && dialedAddress.get(0).origin()) {
            // only one symbol - origin -> connect to nearest rings
            rings = RingsNetwork.INSTANCE.getNearestRings(this.ringsPos);
        } else {

            if (dialedAddress.size() < 5) {
                return RingsConnectResult.ADDRESS_MALFORMED;
            }
            if (!dialedAddress.getLast().origin()) {
                return RingsConnectResult.NO_ORIGIN;
            }

            rings = RingsNetwork.INSTANCE.getRings(dialedAddress.toImmutable());
            if (rings == null || rings == ringsPos || (rings.ringsPos == getBlockPos() && rings.dimension == level.dimension())) {
                return RingsConnectResult.ADDRESS_MALFORMED;
            }
            if (RingsNetwork.INSTANCE.isOutOfRange(ringsPos, rings, canTransportCrossDim())) {
                return RingsConnectResult.OUT_OF_RANGE;
            }
        }
        if (rings == null)
            return RingsConnectResult.ADDRESS_MALFORMED;

        var energyNeeded = getEnergyToOperate(rings);
        if (energyNeeded.getEnergyToStart() > getEnergyStored())
            return RingsConnectResult.NO_POWER;

        var ringsBe = rings.getBlockEntity();
        if (ringsBe.busy) {
            return RingsConnectResult.BUSY;
        }

        if (!ringsBe.setBorderBlocks(false, true)) return RingsConnectResult.OBFUSCATED_TARGET;
        if (!setBorderBlocks(false, true)) return RingsConnectResult.OBFUSCATED;

        outbound = true;
        busy = true;
        targetRings = rings;
        getEnergyStorage().extractEnergy(energyNeeded.energyToOpen, false);
        energyToOperate = energyNeeded;
        setChanged();
        startTeleportAnimation();

        ringsBe.outbound = false;
        ringsBe.busy = true;
        ringsBe.targetRings = ringsPos;
        ringsBe.energyToOperate = energyNeeded;
        ringsBe.setChanged();
        ringsBe.startTeleportAnimation();
        return RingsConnectResult.OK;
    }

    public int getVerticalOffset() {
        return verticalOffset + 2;
    }

    public void setVerticalOffset(int verticalOffset) {
        if (busy) return;
        if (verticalOffset < 1 && verticalOffset > -4) verticalOffset = -4;
        this.verticalOffset = verticalOffset - 2;
        setChanged();
        getAndSendState(StateTypeEnum.RENDERER_STATE);
    }

    protected void startTeleportAnimation() {
        if (level == null || level.isClientSide()) return;
        ignoredEntities.clear();
        pistonHeads.clear();
        ChunkManager.forceChunk((ServerLevel) level, new ChunkPos(getBlockPos()));
        addTask(new ScheduledTask(EnumScheduledTask.RINGS_START_ANIMATION, 40));
    }

    public final List<Entity> ignoredEntities = new ArrayList<>();
    public final ArrayList<TeleportHelper.BlockToTeleport> pistonHeads = new ArrayList<>();

    protected void teleportVolumes(int index, boolean isLast) {
        if (targetRings == null) return;
        if (level == null) return;
        if (index < 0) return;
        var targetRings = this.targetRings.getBlockEntity();
        if (energyToOperate == null) return;

        var minPos = new BlockPos(-1, getVerticalOffset() + index, -1).offset(getBlockPos());
        var maxPos = new BlockPos(1, getVerticalOffset() + index, 1).offset(getBlockPos());
        var poses = StreamSupport.stream(BlockPos.betweenClosed(minPos, maxPos).spliterator(), false);
        var entities = level.getEntities(null, new JSGAxisAlignedBB(minPos.getCenter(), maxPos.getCenter()).grow(0.5, 0.5, 0.5));
        for (var e : entities) {
            if (ignoredEntities.contains(e)) continue;
            var energyToTransport = energyToOperate.getEnergyForTransport(e);
            if (getEnergyStored() < energyToTransport) continue;
            targetRings.ignoredEntities.add(e);
            TeleportHelper.teleportEntity(e, ringsPos, this.targetRings);
            getEnergyStorage().extractEnergy(energyToTransport, false);
        }

        if (!outbound || targetRings.level == null) return;

        var filteredPoses = poses.map(BlockPos::immutable)
                .filter(imPos -> imPos != this.getBlockPos() && imPos != this.getLinkedPos())
                .map(p -> {
                    var relPos = p.subtract(getBlockPos().above(getVerticalOffset()));
                    return Map.entry(p, targetRings.getBlockPos().above(targetRings.getVerticalOffset()).offset(relPos));
                })
                .filter(pp -> !level.getBlockState(pp.getKey()).is(TagsRegistry.UNTRANSPORTABLE_BLOCK) && !targetRings.level.getBlockState(pp.getValue()).is(TagsRegistry.UNTRANSPORTABLE_BLOCK));
        TeleportHelper.teleportBlocks(filteredPoses, this, targetRings, pistonHeads);
        if (isLast) {
            pistonHeads.forEach(pp -> pp.placeOrAdd(null));
        }
    }

    private ResourceLocation getConfigType() {
        return BlockConfigOptionRegistry.RINGS_COMMON;
    }

    protected final JSGTileEntityConfig config = new JSGTileEntityConfig(getConfigType());

    @Override
    public JSGTileEntityConfig getConfig() {
        return config;
    }


    @Override
    public void setConfig(JSGTileEntityConfig newConfig) {
        boolean changed = false;
        for (JSGConfigOption<?> opt : newConfig.getOptions()) {
            changed = changed || this.config.getOption(opt.getLabel()).setValue(opt.getValue().toString());
        }
        if (changed) {
            setChanged();
        }
    }

    @Override
    public void setConfigAndUpdate(JSGTileEntityConfig newConfig) {
        setConfig(newConfig);
        sendState(StateTypeEnum.GUI_STATE, getState(StateTypeEnum.GUI_STATE));
    }

    @Override
    public String getDeviceType() {
        return "RINGS";
    }

    public List<BiomeOverlayRegistry.BiomeOverlayInstance> getSupportedOverlays() {
        return List.of(BiomeOverlayRegistry.NORMAL);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return LazyOptional.of(() -> inventory).cast();
        }
        if (capability == ForgeCapabilities.ENERGY) {
            return LazyOptional.of(this::getEnergyStorage).cast();
        }
        var computerCaps = getDeviceHolder().getOrCreateDeviceBasedOnCap(capability);
        if (computerCaps.isPresent())
            return computerCaps;
        return super.getCapability(capability, facing);
    }

    public EnergyRequiredToOperateRings energyToOperate;

    public EnergyRequiredToOperateRings getEnergyToOperate(@NotNull RingsPos targetRings) {
        var energyRequired = EnergyRequiredToOperateRings.rings();

        BlockPos sPos = pos;
        BlockPos tPos = targetRings.ringsPos;

        ResourceKey<Level> sourceDim = getLevelNotNull().dimension();
        ResourceKey<Level> targetDim = targetRings.getWorld().dimension();

        if (sourceDim == Level.OVERWORLD && targetDim == Level.NETHER)
            tPos = new BlockPos(tPos.getX() * 8, tPos.getY(), tPos.getZ() * 8);
        else if (sourceDim == Level.NETHER && targetDim == Level.OVERWORLD)
            sPos = new BlockPos(sPos.getX() * 8, sPos.getY(), sPos.getZ() * 8);

        double distance = (int) BlockPosHelper.dist(sPos, tPos.getX(), tPos.getY(), tPos.getZ());

        if (distance < 50) distance *= 0.8;
        else distance = 50 * Math.log10(distance) / Math.log10(50);

        return (EnergyRequiredToOperateRings) energyRequired.mul(distance).add(StargateDimensionConfig.INSTANCE.getCost(sourceDim, targetDim).mul(0.01));
    }
}
