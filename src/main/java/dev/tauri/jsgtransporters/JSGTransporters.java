package dev.tauri.jsgtransporters;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(JSGTransporters.MOD_ID)
public class JSGTransporters {
    public static final String MOD_ID = "jsg_destiny";
    public static final String MOD_NAME = "Just Stargate Mod: Destiny Addon";
    public static Logger logger;

    public static String MOD_VERSION = "";
    public static final String MC_VERSION = "1.20.1";

    public JSGTransporters() {
        logger = LoggerFactory.getLogger(MOD_NAME);

        ModList.get().getModContainerById(MOD_ID).ifPresentOrElse(container -> MOD_VERSION = MC_VERSION + "-" + container.getModInfo().getVersion().getQualifier(), () -> {
        });
        JSGTransporters.logger.info("Loading JSG:Destiny Addon version " + JSGTransporters.MOD_VERSION);
    }
}
