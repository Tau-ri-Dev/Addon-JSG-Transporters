package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.block.IItemBlock;
import dev.tauri.jsg.item.ITabbedItem;
import dev.tauri.jsg.item.JSGBlockItem;
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

import static dev.tauri.jsgtransporters.JSGTransporters.MOD_ID;

public class BlockRegistry {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    /**
     * TRANSPORT RINGS
     */
    public static final RegistryObject<Block> RING_ANCIENT = REGISTER.register("ring_ancient", RingsAncient::new);
    public static final RegistryObject<Block> RING_GOAULD = REGISTER.register("ring_goauld", RingsGoauld::new);
    public static final RegistryObject<Block> RING_ORI = REGISTER.register("ring_ori", RingsOri::new);
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
                        RegistryObject<CreativeModeTab> tab = null;
                        if (i.get() instanceof ITabbedItem t) {
                            tab = t.getTab();
                        }
                        if (i.get() instanceof IItemBlock itemBlock)
                            return itemBlock.getItemBlock();
                        return new JSGBlockItem(i.get(), new Item.Properties(), tab);
                    });
        }
        REGISTER.register(bus);
    }
}
