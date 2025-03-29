package dev.tauri.jsgtransporters.common.block.rings;

import dev.tauri.jsg.block.TickableBEBlock;
import dev.tauri.jsg.item.ITabbedItem;
import dev.tauri.jsgtransporters.common.registry.TabRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public abstract class RingsAbstractBlock extends TickableBEBlock implements ITabbedItem {

    public static final Properties RINGS_BASE_PROPS = Properties.of();

    public RingsAbstractBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public RegistryObject<CreativeModeTab> getTab() {
        return TabRegistry.TAB_RINGS;
    }
}
