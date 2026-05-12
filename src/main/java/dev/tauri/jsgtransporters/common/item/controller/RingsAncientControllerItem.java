package dev.tauri.jsgtransporters.common.item.controller;

import dev.tauri.jsg.core.client.renderer.AbstractItemBEWLR;
import dev.tauri.jsgtransporters.client.renderer.item.controller.RingsAncientCPBEWLR;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class RingsAncientControllerItem extends ControllerItem {
    public RingsAncientControllerItem(Block pBlock) {
        super(pBlock);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public IClientItemExtensions getItemBEWLR() {
        return AbstractItemBEWLR.create(RingsAncientCPBEWLR::new);
    }
}
