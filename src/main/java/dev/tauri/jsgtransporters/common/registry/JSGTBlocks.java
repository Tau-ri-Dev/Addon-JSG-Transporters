package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.block.controller.RingsAncientCPBlock;
import dev.tauri.jsgtransporters.common.block.controller.RingsGoauldCPBlock;
import dev.tauri.jsgtransporters.common.block.controller.RingsOriCPBlock;
import dev.tauri.jsgtransporters.common.block.rings.RingsAncient;
import dev.tauri.jsgtransporters.common.block.rings.RingsGoauld;
import dev.tauri.jsgtransporters.common.block.rings.RingsOri;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class JSGTBlocks {
    private static final DeferredRegister<Block> REGISTER = JSGTransporters.REGISTRY_HELPER.block();

    public static final RegistryObject<Block> RINGS_ANCIENT = REGISTER.register("rings_ancient_block", RingsAncient::new);
    public static final RegistryObject<Block> RINGS_GOAULD = REGISTER.register("rings_goauld_block", RingsGoauld::new);
    public static final RegistryObject<Block> RINGS_ORI = REGISTER.register("rings_ori_block", RingsOri::new);

    public static final RegistryObject<Block> RINGS_CP_GOAULD = REGISTER.register("rings_goauld_control_panel_block", RingsGoauldCPBlock::new);
    public static final RegistryObject<Block> RINGS_CP_ORI = REGISTER.register("rings_ori_control_panel_block", RingsOriCPBlock::new);
    public static final RegistryObject<Block> RINGS_CP_ANCIENT = REGISTER.register("rings_ancient_control_panel_block", RingsAncientCPBlock::new);

    public static void init() {
    }
}
