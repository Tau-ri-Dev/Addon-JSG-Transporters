package dev.tauri.jsgtransporters.common.blockentity.lantean_transporter;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.tauri.jsg.api.power.general.LargeEnergyStorage;
import dev.tauri.jsg.api.state.State;
import dev.tauri.jsg.api.state.StateType;
import dev.tauri.jsg.api.util.JSGItemStackHandler;
import dev.tauri.jsg.api.util.blockentity.IPreparable;
import dev.tauri.jsg.api.util.blockentity.ITickable;
import dev.tauri.jsg.item.energy.CapacitorItemBlock;
import dev.tauri.jsg.state.StateProviderInterface;
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

public class LanteanTransporterBE extends BlockEntity implements ITickable, IPreparable, StateProviderInterface/*
                                                                                                                * ,
                                                                                                                * IMultiStructureBE
                                                                                                                * <
                                                                                                                * LanteanTransporterMergeHelper>
                                                                                                                */ {

  protected final UUID[] networks = new UUID[2];
  private float doorState = 0f;

  public LanteanTransporterBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
    super(pType, pPos, pBlockState);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void tick(@NotNull Level arg0) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'tick'");
  }
  /*
   * @Override
   * public LanteanTransporterMergeHelper getMergeHelper() {
   * return new LanteanTransporterMergeHelper(this);
   * }
   */

  @Override
  public boolean prepareBE() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'prepareBE'");
  }

  protected final JSGItemStackHandler inventory = new JSGItemStackHandler(6) {
    public boolean isItemValid(int slot, net.minecraft.world.item.ItemStack stack) {
      Item item = stack.getItem();
      return switch (slot) {
        case 0, 1 -> false; // TODO Network filter/link items
        case 2 -> false; // TODO identity crystal
        case 3, 4, 5 -> item instanceof CapacitorItemBlock;
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
    protected void onEnergyChanged() {
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
    throw new UnsupportedOperationException("Unimplemented method 'updatePowerTier'");
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
    throw new UnsupportedOperationException("Unimplemented method 'updateIdentity'");
  }

  public void updateNetworkLinks() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateNetworkLinks'");
  }

  @Override
  public void load(@Nonnull CompoundTag pTag) {
    if (pTag.contains("networks")) {
      CompoundTag networksTag = pTag.getCompound("networks");
      networks[0] = networksTag.contains("A") ? networksTag.getUUID("A") : null;
      networks[1] = networksTag.contains("B") ? networksTag.getUUID("B") : null;
    } else {
      networks[0] = null;
      networks[1] = null;
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

  protected void teleportVolume() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'teleportVolume'");
  }
}

