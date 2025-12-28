package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.api.block.util.IItemBlock;
import dev.tauri.jsg.api.item.ITabbedItem;
import dev.tauri.jsg.api.item.JSGBlockItem;
import dev.tauri.jsgtransporters.common.block.controller.RingsAncientCPBlock;
import dev.tauri.jsgtransporters.common.block.controller.RingsGoauldCPBlock;
import dev.tauri.jsgtransporters.common.block.controller.RingsOriCPBlock;
import dev.tauri.jsgtransporters.common.block.rings.RingsAncient;
import dev.tauri.jsgtransporters.common.block.rings.RingsGoauld;
import dev.tauri.jsgtransporters.common.block.rings.RingsOri;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static dev.tauri.jsgtransporters.JSGTransporters.MOD_ID;

public class BlockRegistry {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    /**
     * TRANSPORT RINGS
     */
    public static final RegistryObject<Block> RINGS_ANCIENT = REGISTER.register("rings_ancient_block", RingsAncient::new);
    public static final RegistryObject<Block> RINGS_GOAULD = REGISTER.register("rings_goauld_block", RingsGoauld::new);
    public static final RegistryObject<Block> RINGS_ORI = REGISTER.register("rings_ori_block", RingsOri::new);

    public static final RegistryObject<Block> RINGS_CP_GOAULD = REGISTER.register("rings_goauld_control_panel_block", RingsGoauldCPBlock::new);
    public static final RegistryObject<Block> RINGS_CP_ORI = REGISTER.register("rings_ori_control_panel_block", RingsOriCPBlock::new);
    public static final RegistryObject<Block> RINGS_CP_ANCIENT = REGISTER.register("rings_ancient_control_panel_block", RingsAncientCPBlock::new);
    /**
     * ATLANTIS TRANSPORTER
     */

    /**
     * OBELISK TRANSPORTER
     */


    public static void register(IEventBus bus) {
        for (RegistryObject<Block> i : REGISTER.getEntries().stream().toList()) {
            ItemRegistry.REGISTER.register(i.getId().getPath(),
                    () -> {
                        List<RegistryObject<CreativeModeTab>> tabs = List.of();
                        if (i.get() instanceof ITabbedItem t) {
                            tabs = t.getTabs();
                        }
                        if (i.get() instanceof IItemBlock itemBlock)
                            return itemBlock.getItemBlock();
                        return new JSGBlockItem(i.get(), new Item.Properties(), tabs);
                    });
        }
        REGISTER.register(bus);
    }
}
