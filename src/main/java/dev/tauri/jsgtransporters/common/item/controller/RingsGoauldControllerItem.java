package dev.tauri.jsgtransporters.common.item.controller;

import dev.tauri.jsgcore.client.renderer.AbstractItemBEWLR;
import dev.tauri.jsgtransporters.client.renderer.item.controller.RingsGoauldCPBEWLR;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class RingsGoauldControllerItem extends ControllerItem {
    public RingsGoauldControllerItem(Block pBlock) {
        super(pBlock);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public IClientItemExtensions getItemBEWLR() {
        return AbstractItemBEWLR.create(RingsGoauldCPBEWLR::new);
    }
}
