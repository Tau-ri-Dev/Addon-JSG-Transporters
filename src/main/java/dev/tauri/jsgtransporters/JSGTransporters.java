package dev.tauri.jsgtransporters;

import dev.tauri.jsg.core.JSGAddon;
import dev.tauri.jsg.core.JSGAddons;
import dev.tauri.jsg.core.LoggerWrapper;
import dev.tauri.jsg.core.common.integration.Integrations;
import dev.tauri.jsg.core.common.registry.helper.RegistryHelper;
import dev.tauri.jsgtransporters.client.ClientConstants;
import dev.tauri.jsgtransporters.common.config.JSGTConfig;
import dev.tauri.jsgtransporters.common.integration.cctweaked.CCDevicesRegistry;
import dev.tauri.jsgtransporters.common.integration.oc2.OCDevicesRegistry;
import dev.tauri.jsgtransporters.common.packet.JSGTPacketHandler;
import dev.tauri.jsgtransporters.common.registry.JSGTRegistriesInit;
import dev.tauri.jsgtransporters.common.rings.network.RingsNetwork;
import dev.tauri.jsgtransporters.common.worldgen.JSGTTemplatePoolInjectors;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(JSGTransporters.MOD_ID)
public class JSGTransporters implements JSGAddon {
    public static final String MOD_ID = "jsg_transporters";
    public static final String MOD_NAME = "JSG: Rings and Transporters";
    public static Logger logger;

    public static String MOD_VERSION = "";
    public static final String MC_VERSION = "1.20.1";

    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(JSGTransporters.MOD_ID);

    public JSGTransporters() {
        logger = new LoggerWrapper("[jsg transporters] ", LoggerFactory.getLogger(MOD_NAME));

        ModList.get().getModContainerById(MOD_ID).ifPresentOrElse(container -> MOD_VERSION = MC_VERSION + "-" + container.getModInfo().getVersion().getQualifier(), () -> {
        });
        JSGTransporters.logger.info("Loading {} version {}", MOD_NAME, JSGTransporters.MOD_VERSION);

        JSGTConfig.load();
        JSGTConfig.register();

        var eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Constants.init();
        JSGTRegistriesInit.init();

        JSGTPacketHandler.init();

        JSGTTemplatePoolInjectors.register();

        JSGTRegistriesInit.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);

        Integrations.OC2.addOnLoad(OCDevicesRegistry::load);
        Integrations.CCT.addOnLoad(CCDevicesRegistry::load);

        JSGAddons.registerAddon(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        var currentServer = event.getServer();
        new RingsNetwork().register(currentServer.overworld().getDataStorage());
    }

    @Override
    public String getName() {
        return MOD_NAME;
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

    @Override
    public String getVersion() {
        return MOD_VERSION;
    }

    @Override
    public void onJSGCoreLoad() {
        ClientConstants.load();
    }
}
