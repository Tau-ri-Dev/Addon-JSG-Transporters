package dev.tauri.jsgtransporters.common.item.controller;

import dev.tauri.jsg.core.client.renderer.AbstractItemBEWLR;
import dev.tauri.jsgtransporters.client.renderer.item.controller.RingsOriCPBEWLR;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class RingsOriControllerItem extends ControllerItem {
    public RingsOriControllerItem(Block pBlock) {
        super(pBlock);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public IClientItemExtensions getItemBEWLR() {
        return AbstractItemBEWLR.create(RingsOriCPBEWLR::new);
    }
}
