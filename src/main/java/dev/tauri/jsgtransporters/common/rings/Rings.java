package dev.tauri.jsgtransporters.common.rings;

import dev.tauri.jsgcore.common.blockentity.IPreparable;
import dev.tauri.jsgcore.common.blockentity.IStateProvider;
import dev.tauri.jsgcore.common.blockentity.ITickable;
import dev.tauri.jsgcore.common.blockentity.IUpgradable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public interface Rings extends IUpgradable, IStateProvider, ITickable, IPreparable {
    ItemStack getDropBaseBlock(ServerPlayer player);

    void updateContainerItemsByItemStack(ItemStack stack);
}
