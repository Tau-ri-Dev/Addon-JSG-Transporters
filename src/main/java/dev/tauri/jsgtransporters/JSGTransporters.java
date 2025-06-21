package dev.tauri.jsgtransporters;

import dev.tauri.jsg.JSG;
import dev.tauri.jsg.api.JSGAddon;
import dev.tauri.jsg.integration.Integrations;
import dev.tauri.jsgtransporters.client.screen.RingsGui;
import dev.tauri.jsgtransporters.common.advancements.JSGTAdvancements;
import dev.tauri.jsgtransporters.common.config.JSGTConfig;
import dev.tauri.jsgtransporters.common.integration.cctweaked.CCDevicesRegistry;
import dev.tauri.jsgtransporters.common.integration.oc2.OCDevicesRegistry;
import dev.tauri.jsgtransporters.common.packet.JSGTPacketHandler;
import dev.tauri.jsgtransporters.common.raycaster.AncientCPRaycaster;
import dev.tauri.jsgtransporters.common.raycaster.GoauldCPRaycaster;
import dev.tauri.jsgtransporters.common.raycaster.OriCPRaycaster;
import dev.tauri.jsgtransporters.common.registry.*;
import dev.tauri.jsgtransporters.common.rings.network.AddressTypeRegistry;
import dev.tauri.jsgtransporters.common.rings.network.RingsNetwork;
import dev.tauri.jsgtransporters.common.rings.network.SymbolTypeRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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

    public JSGTransporters() {
        logger = LoggerFactory.getLogger(MOD_NAME);

        ModList.get().getModContainerById(MOD_ID).ifPresentOrElse(container -> MOD_VERSION = MC_VERSION + "-" + container.getModInfo().getVersion().getQualifier(), () -> {
        });
        JSGTransporters.logger.info("Loading JSG:Transporters Addon version {}", JSGTransporters.MOD_VERSION);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        Constants.load();

        JSGTConfig.load();
        JSGTConfig.register();

        ItemRegistry.register(modEventBus);
        BlockRegistry.register(modEventBus);
        TabRegistry.register(modEventBus);
        BlockEntityRegistry.register(modEventBus);
        SoundRegistry.register(modEventBus);
        MenuTypeRegistry.register(modEventBus);
        modEventBus.addListener(BlockEntityRegistry::registerBERs);


        AddressTypeRegistry.register();
        SymbolTypeRegistry.register();
        GoauldCPRaycaster.register();
        OriCPRaycaster.register();
        AncientCPRaycaster.register();
        JSGTPacketHandler.init();

        Integrations.OC2.addOnLoad(OCDevicesRegistry::load);
        Integrations.CCT.addOnLoad(CCDevicesRegistry::load);

        JSG.registerAddon(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        var currentServer = event.getServer();
        new RingsNetwork().register(currentServer.overworld().getDataStorage());
    }


    public void onCommonSetup(FMLClientSetupEvent event) {
        JSGTAdvancements.register();
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

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> MenuScreens.register(MenuTypeRegistry.RINGS_MENU_TYPE.get(), RingsGui::new));
        }
    }
}
