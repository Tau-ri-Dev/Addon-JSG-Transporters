package dev.tauri.jsgtransporters.common.block.lantean_transporter;

import java.util.List;
import java.util.function.Supplier;

import dev.tauri.jsgcore.common.block.JSGTabbedBlock;
import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.SoundType;

public class LanteanTransporterPlatform extends JSGTabbedBlock {
    public static final Properties PLATFORM_PROPS = Properties.of()
            .explosionResistance(30f)
            .destroyTime(3f)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .sound(SoundType.METAL);
    public LanteanTransporterPlatform() {
        super(PLATFORM_PROPS);
        this.registerDefaultState(
            defaultBlockState()
        );
    }

    @Override
    public List<Supplier<RegistryObjectWrapper<CreativeModeTab>>> getTabs() {
        // TODO Auto-generated method stub
        return super.getTabs();
    }
}
