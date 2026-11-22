package dev.tauri.jsgtransporters.common.item.lantean_transporter;

import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import dev.tauri.jsgcore.common.item.JSGBlockItem;
import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;

public class LanteanTransporterScreenItem extends JSGBlockItem {
    public LanteanTransporterScreenItem(Block block, Properties properties,
            @Nullable List<Supplier<RegistryObjectWrapper<CreativeModeTab>>> tabs) {
        super(block, properties, tabs);
    }
}
