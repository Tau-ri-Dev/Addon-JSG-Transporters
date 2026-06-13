package dev.tauri.jsgtransporters.common.blockentity.rings;

import dev.tauri.jsg.core.common.blockentity.*;
import dev.tauri.jsg.core.common.chunkloader.ChunkManager;
import dev.tauri.jsg.core.common.config.JSGCoreConfig;
import dev.tauri.jsg.core.common.config.ingame.BEConfig;
import dev.tauri.jsg.core.common.config.ingame.IConfigurable;
import dev.tauri.jsg.core.common.config.ingame.option.ConfigOptionsHolder;
import dev.tauri.jsg.core.common.config.json.dimension.JSGDimensionConfig;
import dev.tauri.jsg.core.common.entity.*;
import dev.tauri.jsg.core.common.helper.BlockPosHelper;
import dev.tauri.jsg.core.common.helper.LinkingHelper;
import dev.tauri.jsg.core.common.integration.ComputerDeviceHolder;
import dev.tauri.jsg.core.common.integration.ComputerDeviceProvider;
import dev.tauri.jsg.core.common.item.capacitor.CapacitorItemBlock;
import dev.tauri.jsg.core.common.item.notebook.PageNotebookItemFilled;
import dev.tauri.jsg.core.common.packet.JSGCorePacketHandler;
import dev.tauri.jsg.core.common.packet.packets.StateUpdatePacketToClient;
import dev.tauri.jsg.core.common.packet.packets.StateUpdateRequestToServer;
import dev.tauri.jsg.core.common.power.general.LargeEnergyStorage;
import dev.tauri.jsg.core.common.registry.CoreBlocks;
import dev.tauri.jsg.core.common.registry.CoreItems;
import dev.tauri.jsg.core.common.registry.CoreScheduledTasks;
import dev.tauri.jsg.core.common.registry.CoreStateTypes;
import dev.tauri.jsg.core.common.sound.JSGSoundHelper;
import dev.tauri.jsg.core.common.state.BiomeOverrideState;
import dev.tauri.jsg.core.common.symbol.SymbolInterface;
import dev.tauri.jsg.core.common.symbol.SymbolType;
import dev.tauri.jsg.core.common.symbol.address.IAddress;
import dev.tauri.jsg.core.common.symbol.pointoforigin.PointOfOrigin;
import dev.tauri.jsg.core.common.util.*;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.blockentity.controller.AbstractRingsCPBE;
import dev.tauri.jsgtransporters.common.config.ingame.RingsConfigOptions;
import dev.tauri.jsgtransporters.common.energy.EnergyRequiredToOperateRings;
import dev.tauri.jsgtransporters.common.entity.RingsAddressData;
import dev.tauri.jsgtransporters.common.helpers.TeleportHelper;
import dev.tauri.jsgtransporters.common.registry.*;
import dev.tauri.jsgtransporters.common.registry.tags.JSGTBlockTags;
import dev.tauri.jsgtransporters.common.rings.RingsConnectResult;
import dev.tauri.jsgtransporters.common.rings.network.RingsAddress;
import dev.tauri.jsgtransporters.common.rings.network.RingsAddressDynamic;
import dev.tauri.jsgtransporters.common.rings.network.RingsNetwork;
import dev.tauri.jsgtransporters.common.rings.network.RingsPos;
import dev.tauri.jsgtransporters.common.state.gui.RingsContainerGuiState;
import dev.tauri.jsgtransporters.common.state.gui.RingsContainerGuiUpdate;
import dev.tauri.jsgtransporters.common.state.renderer.RingsRendererState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
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
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.TargetPoint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.StreamSupport;

public abstract class RingsAbstractBE extends BlockEntity implements ILinkable<AbstractRingsCPBE>, IUpgradable, IConfigurable, IAddressProvider, ITickable, ComputerDeviceProvider, ScheduledTaskExecutorInterface, StateProviderInterface, IPreparable {

    public RingsAbstractBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    protected Map<SymbolType<?>, RingsAddress> addressMap = new HashMap<>();
    protected RingsPos ringsPos;
    protected int verticalOffset = 0;
    protected final RingsAddressDynamic dialedAddress = new RingsAddressDynamic(getSymbolType());

    public static final int BIOME_OVERRIDE_SLOT = 10;

    protected final JSGItemStackHandler inventory = new JSGItemStackHandler(11) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            Item item = stack.getItem();
            boolean isItemCapacitor = (item instanceof CapacitorItemBlock);
            return switch (slot) {
                case 0, 1, 2, 3 ->
                        RingsUpgradeEnum.contains(item) && !hasUpgrade(item) && RingsUpgradeEnum.valueOf(item).slot == slot;
                case 4, 5, 6 -> isItemCapacitor && getSupportedCapacitors() >= (slot - 3);
                case 7, 8, 9 ->
                        item == CoreItems.NOTEBOOK_PAGE_EMPTY.get() || item == CoreItems.NOTEBOOK_PAGE_FILLED.get();
                case BIOME_OVERRIDE_SLOT ->
                        BiomeOverlayInstance.values().stream().map(BiomeOverlayInstance::getOverlayItems).anyMatch(i -> i.contains(item));
                default -> true;
            };
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
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
                    sendState(CoreStateTypes.BIOME_OVERRIDE_STATE.get(), new BiomeOverrideState(determineBiomeOverride()));
                    break;
                default:
                    break;
            }

            setChanged();
        }
    };

    // Server
    private BiomeOverlayInstance determineBiomeOverride() {
        ItemStack stack = inventory.getStackInSlot(BIOME_OVERRIDE_SLOT);

        if (stack.isEmpty()) {
            return null;
        }

        return BiomeOverlayInstance.getBiomeOverlayByItem(stack);
    }

    public int getSupportedCapacitors() {
        return getConfig().getValueOrDefault(RingsConfigOptions.Common.MAX_CAPACITORS);
    }

    public enum RingsUpgradeEnum implements EnumKeyInterface<Item>, IUpgrade {
        GOAULD_GLYPHS(JSGTItems.CRYSTAL_GLYPH_GOAULD.get(), 0),
        ORI_GLYPHS(JSGTItems.CRYSTAL_GLYPH_ORI.get(), 1),
        ANCIENT_GLYPHS(JSGTItems.CRYSTAL_GLYPH_ANCIENT.get(), 2),
        DIMENSIONAL_TUNNELING(JSGTItems.CRYSTAL_UPGRADE_DIM_TUNNELING.get(), 3);

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

    private final LargeEnergyStorage energyStorage = new LargeEnergyStorage(JSGCoreConfig.Energy.capacitorEnergyStorage.get(), JSGCoreConfig.Energy.capacitorMaxEnergyTransfer.get()) {

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

            JSGTransporters.logger.debug("Updated to power tier: {}", powerTier);
        }
    }

    protected boolean isRSPowered;

    public void updateRedstonePower(boolean power) {
        if (getLevel() == null || getLevel().isClientSide) return;
        if (isRSPowered == power) return;
        isRSPowered = power;
        if (power) {
            dialedAddress.clear();
            if (lastDialedAddress != null)
                dialedAddress.addAll(lastDialedAddress);
            setChanged();
            tryConnect();
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
        needRegenerate = true;
        busy = false;
        scheduledTasks.clear();
        if (isLinked()) {
            var linked = getLinkedDevice();
            if (linked != null)
                linked.setLinkedDevice(null);
            setLinkedDevice(null);
        }
        setChanged();
        return true;
    }

    public long getTime() {
        var level = getLevel();
        if (level == null) return 0;
        return level.getGameTime();
    }

    protected TargetPoint targetPoint;

    @Override
    public void onLoad() {
        var pos = getBlockPos();
        if (level != null) {
            if (!level.isClientSide) {
                this.targetPoint = new TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 512, Objects.requireNonNull(getLevel()).dimension());

                tryRegenerateRingsIfNeeded();

                generateAddresses(false);
                updatePowerTier();
            } else {
                JSGCorePacketHandler.sendToServer(new StateUpdateRequestToServer(getBlockPos(), CoreStateTypes.RENDERER_STATE.get()));
                JSGCorePacketHandler.sendToServer(new StateUpdateRequestToServer(getBlockPos(), CoreStateTypes.GUI_STATE.get()));
            }
        }
        super.onLoad();
    }

    public void regenerateRings() {
        var world = getLevel();
        if (world == null) return;
        if (world.isClientSide) return;
        JSGTransporters.logger.info("Regenerating rings at {} in {}", getBlockPos(), world.dimension().location());
        generateAddresses(true);
        updateLinkStatus();
        setChanged();
    }

    public void tryRegenerateRingsIfNeeded() {
        if (needRegenerate) {
            regenerateRings();
            needRegenerate = false;
            setChanged();
        }
    }

    private boolean needRegenerate = false;

    private boolean addedToNetwork;

    @Override
    public void tick(@Nonnull Level level) {
        // Scheduled tasks
        ScheduledTask.iterate(scheduledTasks, getTime());
        if (!level.isClientSide) {
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
                            givePageTask = new ScheduledTask(CoreScheduledTasks.GIVE_PAGE, 36);
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
                JSGCorePacketHandler.sendToServer(new StateUpdateRequestToServer(getBlockPos(), CoreStateTypes.GUI_STATE.get()));
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
    public RingsAddress getRingsAddress(SymbolType<?> symbolType) {
        if (addressMap == null) return null;

        return addressMap.get(symbolType);
    }

    @Override
    public IAddress getAddress(SymbolType<?> symbolTypeEnum) {
        return getRingsAddress(symbolTypeEnum);
    }

    public void generateAddresses(boolean reset) {
        if (reset && ringsPos != null)
            RingsNetwork.INSTANCE.removeRings(ringsPos);
        var level = getLevel();
        if (level == null) return;
        Random random = new Random(getBlockPos().hashCode() * 31L + level.dimension().location().hashCode());

        for (var symbolType : SymbolType.values(JSGTSymbolUsages.RINGS.get())) {
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

    public abstract SymbolType<?> getSymbolType();

    protected void initRingsPos() {
        var oldPos = ringsPos;
        var level = getLevel();
        if (level == null) return;
        ringsPos = new RingsPos(level.dimension(), getBlockPos(), getSymbolType());
        if (oldPos != null)
            ringsPos.setName(oldPos.getName());
        var ringsFromNetwork = RingsNetwork.INSTANCE.getRings(getRingsAddress(JSGTSymbolTypes.GOAULD.get()));
        if (ringsFromNetwork != null)
            ringsPos.setName(ringsFromNetwork.getName());
    }

    public void setRingsAddress(SymbolType<?> symbolType, RingsAddress address) {
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
     * List of scheduled tasks to be performed on {@link ITickable#tick(Level)()}.
     */
    protected List<ScheduledTask> scheduledTasks = new ArrayList<>();

    @Override
    public void addTask(ScheduledTask task) {
        task.setExecutor(this);
        task.setTaskCreated(getTime());
        scheduledTasks.add(task);
        setChanged();
    }

    public BlockPos getCenterTransportPos() {
        return getBlockPos().offset(0, getVerticalOffset() + 1, 0);
    }

    @Override
    public void executeTask(ScheduledTaskType task, @Nonnull CompoundTag context) {
        if (task == JSGTScheduledTaskTypes.RINGS_START_ANIMATION.get()) {
            if (level == null) return;
            if (level.isClientSide()) return;
            if (!context.contains("start") && !context.contains("end") && !context.contains("index") && !context.contains("tp") && !context.contains("playEnd")) {
                context = new CompoundTag();
                context.putBoolean("start", true);
                addTask(new ScheduledTask(task, (int) (1.67f * 20), context));
                JSGSoundHelper.playSoundEvent(level, getCenterTransportPos(), JSGTSoundEvents.RINGS_TRANSPORT_START);

                context = new CompoundTag();
                context.putBoolean("playEnd", true);
                addTask(new ScheduledTask(task, (int) (4.37 * 20), context));
            } else {
                if (context.getBoolean("playEnd")) {
                    JSGSoundHelper.playSoundEvent(level, getCenterTransportPos(), JSGTSoundEvents.RINGS_TRANSPORT_END);
                } else if (context.getBoolean("start")) {
                    rendererState.startAnimation(level.getGameTime());
                    setChanged();
                    getAndSendState(CoreStateTypes.RENDERER_STATE.get());
                    context = new CompoundTag();
                    context.putBoolean("end", true);
                    addTask(new ScheduledTask(task, RING_ANIMATION_LENGTH, context));
                    addTask(new ScheduledTask(JSGTScheduledTaskTypes.RINGS_SOLID_BLOCKS, 10));
                    var c = new CompoundTag();
                    c.putBoolean("clear", true);
                    addTask(new ScheduledTask(JSGTScheduledTaskTypes.RINGS_SOLID_BLOCKS, RING_ANIMATION_LENGTH, c));

                    var offset = getVerticalOffset();
                    for (int i = 0; i < 3; i++) {
                        context = new CompoundTag();
                        context.putBoolean("tp", true);
                        context.putInt("index", i);
                        context.putBoolean("isLast", i == (offset > 0 ? 0 : 2));
                        addTask(new ScheduledTask(task, 45 + ((offset > 0 ? (2 - i) : i) * 8), context));
                    }
                    return;
                } else if (context.getBoolean("end")) {
                    ChunkManager.unforceChunk((ServerLevel) level, new ChunkPos(getBlockPos()));
                    busy = false;
                    targetRings = null;
                    return;
                } else if (context.getBoolean("tp") && context.contains("index")) {
                    if (targetRings == null) return;
                    teleportVolumes(context.getInt("index"), context.getBoolean("isLast"));
                    return;
                }
            }
        } else if (task == JSGTScheduledTaskTypes.RINGS_SOLID_BLOCKS.get()) {
            setBorderBlocks(context.getBoolean("clear"), false);
        } else if (task == CoreScheduledTasks.GIVE_PAGE.get()) {
            // TODO(Mine): Refactor this and fix this to actually make this working
            if (pageSlotId < 7) return;

            // Temp
            var symbolType = JSGTSymbolTypes.GOAULD.get();

            /*SymbolType<?> symbolType = null;
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
            if (symbolType == null) return;*/
            var stack = getAddressPage(symbolType, new int[]{1, 2, 3, 4, 9});
            inventory.setStackInSlot(pageSlotId, stack);

        }
        setChanged();
    }

    public ItemStack getAddressPage(SymbolType<?> symbolType, int[] symbolsToDisplay) {
        JSGTransporters.logger.info("Giving Notebook page of address {}", symbolType);
        return JSGTNotebookPageTypes.RINGS_ADDRESS.get().createPage(new RingsAddressData(new RingsAddressDynamic(addressMap.get(symbolType)), symbolsToDisplay), PageNotebookItemFilled.getBiomeKeyFromWorld(getLevel(), getBlockPos()));
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
                        if (state.getBlock() != CoreBlocks.INVISIBLE_BLOCK.get()) continue;
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                        continue;
                    }
                    if (!state.canBeReplaced()) return false;
                    if (simulate) continue;
                    var ctx = new BlockPlaceContext(level, null, InteractionHand.MAIN_HAND, ItemStack.EMPTY, new BlockHitResult(pos.getCenter(), Direction.UP, pos, false));
                    var stateNew = CoreBlocks.INVISIBLE_BLOCK.get().getStateForPlacement(ctx);
                    if (stateNew == null) stateNew = CoreBlocks.INVISIBLE_BLOCK.get().defaultBlockState();
                    level.setBlock(pos, stateNew, 3);
                }
            }
        }
        return true;
    }

    @Override
    public void sendState(StateType type, State state) {
        if (Objects.requireNonNull(getLevel()).isClientSide) {
            return;
        }
        if (targetPoint != null) {
            JSGCorePacketHandler.sendToClient(new StateUpdatePacketToClient(getBlockPos(), type, state), targetPoint);
        } else {
            JSGTransporters.logger.debug("targetPoint as null trying to send {} from {}", this, this.getClass().getCanonicalName());
        }
    }

    @Override
    public PacketDistributor.TargetPoint getTargetPoint() {
        return targetPoint;
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
    public State getState(@Nonnull StateType stateType) {
        return stateType.stateSupplier()
                .tryType(CoreStateTypes.GUI_STATE.get(), () -> new RingsContainerGuiState(addressMap, getConfig()))
                .tryType(CoreStateTypes.GUI_UPDATE.get(), () -> new RingsContainerGuiUpdate(energyStorage.getEnergyStoredInternally(), energyTransferredLastTick, pageProgress))
                .tryType(CoreStateTypes.RENDERER_STATE.get(), () -> {
                    var state = getRendererStateClient();
                    state.verticalOffset = verticalOffset;
                    return state;
                })
                .orElseThrow(this);
    }

    @Override
    public State createState(@Nonnull StateType stateType) {
        return stateType.stateSupplier()
                .tryType(CoreStateTypes.GUI_STATE, () -> new RingsContainerGuiState(getConfig()))
                .tryType(CoreStateTypes.GUI_UPDATE, RingsContainerGuiUpdate::new)
                .tryType(CoreStateTypes.RENDERER_STATE, RingsRendererState::new)
                .orElseThrow(this);
    }

    @Override
    public void setState(@Nonnull StateType stateType, @Nonnull State state) {
        stateType.stateExecutor()
                .tryType(CoreStateTypes.RENDERER_STATE, () -> {
                    rendererState = (RingsRendererState) state;
                    verticalOffset = rendererState.verticalOffset;
                    setChanged();
                })
                .tryType(CoreStateTypes.GUI_STATE, () -> {
                    var guiState = (RingsContainerGuiState) state;
                    addressMap = guiState.addressMap;
                    setConfig(guiState.config);
                    setChanged();
                })
                .tryType(CoreStateTypes.GUI_UPDATE, () -> {
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
    public void saveAdditional(@Nonnull CompoundTag compound) {
        for (var address : addressMap.values()) {
            compound.put("address_" + address.getSymbolType(), address.serializeNBT());
        }
        if (isLinked(true))
            compound.putLong("linkedPos", linkedPos.asLong());
        compound.putBoolean("busy", busy);
        compound.putBoolean("needRegenerate", needRegenerate);
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

        compound.putBoolean("isRSPowered", isRSPowered);
        if (lastDialedAddress != null)
            compound.put("lastDialedAddress", lastDialedAddress.serializeNBT());

        super.saveAdditional(compound);
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        for (SymbolType<?> symbolType : SymbolType.values(JSGTSymbolUsages.RINGS.get())) {
            if (compound.contains("address_" + symbolType))
                addressMap.put(symbolType, new RingsAddress(compound.getCompound("address_" + symbolType)));
        }
        if (compound.contains("linkedPos"))
            linkedPos = BlockPos.of(compound.getLong("linkedPos"));
        busy = compound.getBoolean("busy");
        needRegenerate = compound.getBoolean("needRegenerate");
        if (compound.contains("targetRings"))
            targetRings = new RingsPos(compound.getCompound("targetRings"));
        ScheduledTask.deserializeList(compound.getCompound("scheduledTasks"), scheduledTasks, this);

        getConfig().deserializeNBT(compound.getCompound("config"));
        inventory.deserializeNBT(compound.getCompound("itemHandler"));
        verticalOffset = compound.getInt("verticalOffset");

        if (compound.contains("energyToOperate_start")) {
            energyToOperate = new EnergyRequiredToOperateRings(compound.getInt("energyToOperate_start"), compound.getInt("energyToOperate_teleport"));
        }

        isRSPowered = compound.getBoolean("isRSPowered");
        if (compound.contains("lastDialedAddress"))
            lastDialedAddress = new RingsAddressDynamic(compound.getCompound("lastDialedAddress"));
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
        var pos = getBlockPos();
        var block = getControlPanelBlocks();
        if (block == null) return;
        var level = getLevel();
        if (level == null) return;
        BlockPos closestCP = LinkingHelper.findClosestUnlinked(level, pos, new BlockPos(25, 15, 25), block);

        if (closestCP != null && level.getBlockEntity(closestCP) instanceof AbstractRingsCPBE be) {
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
    public RingsAddressDynamic lastDialedAddress;
    public RingsPos targetRings;
    public boolean outbound = false;

    @Nonnull
    public RingsConnectResult tryConnect() {
        if (level == null || level.isClientSide()) return RingsConnectResult.CLIENT;
        RingsPos rings;
        lastDialedAddress = new RingsAddressDynamic(dialedAddress);
        setChanged();
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
        if (energyNeeded.getEnergyToStart() > getEnergyStored()) {
            return RingsConnectResult.NO_POWER;
        }

        var ringsBe = rings.getBlockEntity();
        if (ringsBe.busy) {
            return RingsConnectResult.BUSY;
        }

        if (!ringsBe.setBorderBlocks(false, true)) return RingsConnectResult.OBSTRUCTED_TARGET;
        if (!setBorderBlocks(false, true)) return RingsConnectResult.OBSTRUCTED;

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
        getAndSendState(CoreStateTypes.RENDERER_STATE.get());
    }

    protected void startTeleportAnimation() {
        if (level == null || level.isClientSide()) return;
        ignoredEntities.clear();
        pistonHeads.clear();
        ChunkManager.forceChunk((ServerLevel) level, new ChunkPos(getBlockPos()));
        addTask(new ScheduledTask(JSGTScheduledTaskTypes.RINGS_START_ANIMATION, 40));
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
                .filter(pp -> !level.getBlockState(pp.getKey()).is(JSGTBlockTags.UNTRANSPORTABLE_BLOCK) && !targetRings.level.getBlockState(pp.getValue()).is(JSGTBlockTags.UNTRANSPORTABLE_BLOCK));
        TeleportHelper.teleportBlocks(filteredPoses, this, targetRings, pistonHeads);
        if (isLast) {
            pistonHeads.forEach(pp -> pp.placeOrAdd(null));
        }
    }

    private ConfigOptionsHolder getConfigType() {
        return RingsConfigOptions.Common.HOLDER;
    }

    protected final BEConfig config = new BEConfig(this::setChanged, getConfigType());

    @Override
    public BEConfig getConfig() {
        return config;
    }

    @Override
    public void onConfigUpdated() {
        setChanged();
        sendState(CoreStateTypes.GUI_STATE.get(), getState(CoreStateTypes.GUI_STATE.get()));
    }

    @Override
    public String getDeviceType() {
        return "RINGS";
    }

    @Override
    public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
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

    public EnergyRequiredToOperateRings getEnergyToOperate(@Nonnull RingsPos targetRings) {
        var energyRequired = EnergyRequiredToOperateRings.rings();
        var level = getLevel();
        if (level == null) return energyRequired;

        BlockPos sPos = getBlockPos();
        BlockPos tPos = targetRings.ringsPos;

        ResourceKey<Level> sourceDim = level.dimension();
        ResourceKey<Level> targetDim = targetRings.getWorld().dimension();

        if (sourceDim == Level.OVERWORLD && targetDim == Level.NETHER) {
            tPos = new BlockPos(tPos.getX() * 8, tPos.getY(), tPos.getZ() * 8);
        } else if (sourceDim == Level.NETHER && targetDim == Level.OVERWORLD) {
            sPos = new BlockPos(sPos.getX() * 8, sPos.getY(), sPos.getZ() * 8);
        }

        double distance = (int) BlockPosHelper.dist(sPos, tPos.getX(), tPos.getY(), tPos.getZ());

        if (distance < 50) distance *= 0.8;
        else distance = 50 * Math.log10(distance) / Math.log10(50);

        return energyRequired.mul(distance).add(EnergyRequiredToOperateRings.rings().mul(JSGDimensionConfig.INSTANCE.getDistanceBetween(sourceDim, targetDim)).mul(0.1));
    }

    @Override
    public @Nullable PointOfOrigin getPointOfOrigin(SymbolType<?> abstractSymbolType) {
        return null;
    }
}
