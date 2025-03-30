package dev.tauri.jsgtransporters.common.blockentity.rings;

import dev.tauri.jsg.blockentity.util.IUpgradable;
import dev.tauri.jsg.config.ingame.ITileConfig;
import dev.tauri.jsg.config.ingame.JSGConfigOption;
import dev.tauri.jsg.config.ingame.JSGTileEntityConfig;
import dev.tauri.jsg.state.StateTypeEnum;
import dev.tauri.jsgtransporters.common.config.BlockConfigOptionRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RingsClassicBE extends RingsAbstractBE implements IUpgradable, ITileConfig {

    public RingsClassicBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    private ResourceLocation getConfigType() {
        return BlockConfigOptionRegistry.RINGS_COMMON;
    }

    JSGTileEntityConfig config;

    @Override
    public JSGTileEntityConfig getConfig() {
        return config;
    }

    @Override
    public void initConfig() {
        this.config = new JSGTileEntityConfig(getConfigType());
    }


    @Override
    public void setConfig(JSGTileEntityConfig newConfig) {
        boolean changed = false;
        for (JSGConfigOption<?> opt : newConfig.getOptions()) {
            changed = changed || this.config.getOption(opt.getLabel()).setValue(opt.getValue().toString());
        }
        if (changed) {
            setChanged();
        }
    }

    @Override
    public void setConfigAndUpdate(JSGTileEntityConfig newConfig) {
        setConfig(newConfig);
        sendState(StateTypeEnum.GUI_STATE, getState(StateTypeEnum.GUI_STATE));
    }

    @Override
    public String getDeviceType() {
        return "TR_CLASSIC";
    }
}
