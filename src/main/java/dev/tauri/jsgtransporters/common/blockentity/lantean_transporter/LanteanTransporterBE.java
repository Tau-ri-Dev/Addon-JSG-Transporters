package dev.tauri.jsgtransporters.common.blockentity.lantean_transporter;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.tauri.jsgcore.common.power.general.LargeEnergyStorage;
import dev.tauri.jsgcore.common.registry.CoreStateTypes;
import dev.tauri.jsgcore.common.entity.State;
import dev.tauri.jsgcore.common.entity.StateType;
import dev.tauri.jsgcore.common.packet.JSGCorePacketHandler;
import dev.tauri.jsgcore.common.packet.packets.StateUpdateRequestToServer;
import dev.tauri.jsgcore.common.util.JSGItemStackHandler;
import dev.tauri.jsgtransporters.common.registry.tags.JSGTItemTags;
import dev.tauri.jsgtransporters.common.registry.JSGTBlockEntities;
import dev.tauri.jsgcore.common.blockentity.IPreparable;
import dev.tauri.jsgcore.common.blockentity.ITickable;
import dev.tauri.jsgcore.common.blockentity.StateProviderInterface;
import dev.tauri.jsgcore.common.multistructure.IMultiStructureBE;
import dev.tauri.jsgtransporters.common.multistructure.mergehelper.LanteanTransporterMergeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor.TargetPoint;

public class LanteanTransporterBE extends BlockEntity implements ITickable, IPreparable, StateProviderInterface, IMultiStructureBE<LanteanTransporterMergeHelper> {

    protected final UUID[] networks = new UUID[3];
    private float doorState = 0f;
    private LanteanTransporterMergeHelper mergeHelper;

    public LanteanTransporterBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.mergeHelper = new LanteanTransporterMergeHelper(this);
    }

    public LanteanTransporterBE(BlockPos pPos, BlockState pBlockState) {
        this(JSGTBlockEntities.LANTEAN_TRANSPORTER_BE.get(), pPos, pBlockState);
    }

    @Override
    public void tick(@NotNull Level arg0) {
        getMergeHelper().tick(arg0);
    }
    @Override
    public LanteanTransporterMergeHelper getMergeHelper() {
        if (mergeHelper == null) mergeHelper = new LanteanTransporterMergeHelper(this);
        return mergeHelper;
    }

    @Override
    public boolean prepareBE() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'prepareBE'");
    }

    protected final JSGItemStackHandler inventory = new JSGItemStackHandler(6) {
        public boolean isItemValid(int slot, net.minecraft.world.item.ItemStack stack) {
            Item item = stack.getItem();
            boolean isItemEnergyCrystal = stack.is(JSGTItemTags.LANTEAN_TRANSPORTER_ENERGY_CRYSTALS) && stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
            return switch (slot) {
                case 0, 1 -> false; // TODO Network filter/link items
                case 2 -> false; // TODO identity crystal
                case 3, 4, 5 -> isItemEnergyCrystal;
                default -> false;
            };
        }

        protected int getStackLimit(int slot, net.minecraft.world.item.ItemStack stack) {
            return 1;
        }

        protected void onContentsChanged(int slot) {
            switch (slot) {
                case 0, 1 -> {
                    updateNetworkLinks();
                }
                case 2 -> {
                    updateIdentity();
                }
                case 3, 4, 5 -> {
                    updatePowerTier();
                }
            }
        }
    };

    private final LargeEnergyStorage energyStorage = new LargeEnergyStorage() {
        @Override
        public void onEnergyChanged() {
            setChanged();
        }
    };

    /**
     * @return the energyStorage
     */
    public LargeEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    protected int currentPowerTier = 1;

    public int getCurrentPowerTier() {
        return currentPowerTier;
    }

    public void updatePowerTier() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updatePowerTier'");
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return LazyOptional.of(this::getEnergyStorage).cast();
        }
        return super.getCapability(cap, side);
    }

    public void updateIdentity() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateIdentity'");
    }

    public void updateNetworkLinks() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateNetworkLinks'");
    }

    @Override
    public void load(@Nonnull CompoundTag pTag) {
        if (pTag.contains("networks")) {
            CompoundTag networksTag = pTag.getCompound("networks");
            networks[0] = networksTag.contains("A") ? networksTag.getUUID("A") : null;
            networks[1] = networksTag.contains("B") ? networksTag.getUUID("B") : null;
            networks[2] = networksTag.contains("C") ? networksTag.getUUID("C") : null;
        } else {
            networks[0] = null;
            networks[1] = null;
            networks[2] = null;
        }
        this.doorState = pTag.getFloat("doorState");
        super.load(pTag);
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag pTag) {
        CompoundTag networksTag = new CompoundTag();
        if (networks[0] != null) {
            networksTag.putUUID("A", networks[0]);
        }
        if (networks[1] != null) {
            networksTag.putUUID("B", networks[1]);
        }
        if (networks[2] != null) {
            networksTag.putUUID("C", networks[2]);
        }
        if (!networksTag.isEmpty()) {
            pTag.put("networks", networksTag);
        }
        pTag.putFloat("doorState", doorState);
        // TODO Auto-generated method stub
        super.saveAdditional(pTag);
    }

    @Override
    public State createState(StateType arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createState'");
    }

    @Override
    public State getState(StateType arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getState'");
    }

    @Override
    public void sendState(StateType arg0, State arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendState'");
    }

    @Override
    public void setState(StateType arg0, State arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setState'");
    }

    protected TargetPoint targetPoint;

    @Override
    public void onLoad() {
        var pos = getBlockPos();
        if (level != null) {
            if (!level.isClientSide) {
                this.targetPoint = new TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 512, level.dimension());

                updatePowerTier();
            } else {
                JSGCorePacketHandler.sendToServer(new StateUpdateRequestToServer(getBlockPos(), CoreStateTypes.RENDERER_STATE.get()));
                JSGCorePacketHandler.sendToServer(new StateUpdateRequestToServer(getBlockPos(), CoreStateTypes.GUI_STATE.get()));
            }
        }
        super.onLoad();
    }

    @Override
    public TargetPoint getTargetPoint() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTargetPoint'");
    }

    @Override
    public BlockPos getStateHandlerBlockPos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStateHandlerBlockPos'");
    }
}
