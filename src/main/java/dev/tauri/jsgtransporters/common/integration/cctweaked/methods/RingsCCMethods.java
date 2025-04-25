package dev.tauri.jsgtransporters.common.integration.cctweaked.methods;

import dev.tauri.jsg.integration.cctweaked.methods.AbstractCCMethods;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.integration.cctweaked.CCDevicesRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RingsCCMethods extends AbstractCCMethods<RingsAbstractBE> {
    public RingsCCMethods(BlockEntity ringsTile) {
        super((RingsAbstractBE) ringsTile, CCDevicesRegistry.RINGS);
    }
}
