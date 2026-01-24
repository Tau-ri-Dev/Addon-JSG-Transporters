package dev.tauri.jsgtransporters.common.integration;


import dev.tauri.jsg.integration.jei.JEIAdvancedGuiHandler;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.client.screen.RingsGui;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nonnull;

@JeiPlugin
@SuppressWarnings("unused")
public final class JEIIntegration implements IModPlugin {

    @Override
    public @Nonnull ResourceLocation getPluginUid() {
        return new ResourceLocation(JSGTransporters.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerGuiHandlers(@Nonnull IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(RingsGui.class, new JEIAdvancedGuiHandler());
    }

}
