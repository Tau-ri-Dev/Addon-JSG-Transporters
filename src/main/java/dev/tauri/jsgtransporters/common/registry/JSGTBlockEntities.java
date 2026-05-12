package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.core.common.registry.helper.RegistryHelper;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.client.renderer.blockentity.controller.RingsAncientCPRenderer;
import dev.tauri.jsgtransporters.client.renderer.blockentity.controller.RingsGoauldCPRenderer;
import dev.tauri.jsgtransporters.client.renderer.blockentity.controller.RingsOriCPRenderer;
import dev.tauri.jsgtransporters.client.renderer.blockentity.rings.RingsAncientRenderer;
import dev.tauri.jsgtransporters.client.renderer.blockentity.rings.RingsGoauldRenderer;
import dev.tauri.jsgtransporters.client.renderer.blockentity.rings.RingsOriRenderer;
import dev.tauri.jsgtransporters.common.blockentity.controller.RingsAncientCPBE;
import dev.tauri.jsgtransporters.common.blockentity.controller.RingsGoauldCPBE;
import dev.tauri.jsgtransporters.common.blockentity.controller.RingsOriCPBE;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAncientBE;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsGoauldBE;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsOriBE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class JSGTBlockEntities {
    /**
     * TRANSPORT RINGS
     */
    public static final RegistryObject<BlockEntityType<RingsAncientBE>> RINGS_ANCIENT_BE = JSGTransporters.REGISTRY_HELPER.be().register("rings_ancient_block", RegistryHelper.beSupplier(RingsAncientBE::new, JSGTBlocks.RINGS_ANCIENT));
    public static final RegistryObject<BlockEntityType<RingsGoauldBE>> RINGS_GOAULD_BE = JSGTransporters.REGISTRY_HELPER.be().register("rings_goauld_block", RegistryHelper.beSupplier(RingsGoauldBE::new, JSGTBlocks.RINGS_GOAULD));
    public static final RegistryObject<BlockEntityType<RingsOriBE>> RINGS_ORI_BE = JSGTransporters.REGISTRY_HELPER.be().register("rings_ori_block", RegistryHelper.beSupplier(RingsOriBE::new, JSGTBlocks.RINGS_ORI));

    public static final RegistryObject<BlockEntityType<RingsGoauldCPBE>> RINGS_CP_GOAULD_BE = JSGTransporters.REGISTRY_HELPER.be().register("rings_goauld_control_panel_block", RegistryHelper.beSupplier(RingsGoauldCPBE::new, JSGTBlocks.RINGS_CP_GOAULD));
    public static final RegistryObject<BlockEntityType<RingsOriCPBE>> RINGS_CP_ORI_BE = JSGTransporters.REGISTRY_HELPER.be().register("rings_ori_control_panel_block", RegistryHelper.beSupplier(RingsOriCPBE::new, JSGTBlocks.RINGS_CP_ORI));
    public static final RegistryObject<BlockEntityType<RingsAncientCPBE>> RINGS_CP_ANCIENT_BE = JSGTransporters.REGISTRY_HELPER.be().register("rings_ancient_control_panel_block", RegistryHelper.beSupplier(RingsAncientCPBE::new, JSGTBlocks.RINGS_CP_ANCIENT));

    public static void init() {
        JSGTransporters.REGISTRY_HELPER.beRenderers(() -> List.of(
                new RegistryHelper.BlockEntityRendererPair<>(RINGS_GOAULD_BE.get(), RingsGoauldRenderer::new),
                new RegistryHelper.BlockEntityRendererPair<>(RINGS_ORI_BE.get(), RingsOriRenderer::new),
                new RegistryHelper.BlockEntityRendererPair<>(RINGS_ANCIENT_BE.get(), RingsAncientRenderer::new),

                new RegistryHelper.BlockEntityRendererPair<>(RINGS_CP_GOAULD_BE.get(), RingsGoauldCPRenderer::new),
                new RegistryHelper.BlockEntityRendererPair<>(RINGS_CP_ORI_BE.get(), RingsOriCPRenderer::new),
                new RegistryHelper.BlockEntityRendererPair<>(RINGS_CP_ANCIENT_BE.get(), RingsAncientCPRenderer::new)
        ));
    }
}
