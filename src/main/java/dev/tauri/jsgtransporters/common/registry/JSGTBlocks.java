package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgcore.common.registry.helper.DeferredRegisterWrapper;
import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.block.controller.RingsAncientCPBlock;
import dev.tauri.jsgtransporters.common.block.controller.RingsGoauldCPBlock;
import dev.tauri.jsgtransporters.common.block.controller.RingsOriCPBlock;
import dev.tauri.jsgtransporters.common.block.lantean_transporter.LanteanTransporterAccessPanel;
import dev.tauri.jsgtransporters.common.block.lantean_transporter.LanteanTransporterFrame;
import dev.tauri.jsgtransporters.common.block.lantean_transporter.LanteanTransporterPlatform;
import dev.tauri.jsgtransporters.common.block.lantean_transporter.LanteanTransporterPaneling;
import dev.tauri.jsgtransporters.common.block.lantean_transporter.LanteanTransporterPylon;
import dev.tauri.jsgtransporters.common.block.lantean_transporter.LanteanTransporterScreen;
import dev.tauri.jsgtransporters.common.block.rings.RingsAncient;
import dev.tauri.jsgtransporters.common.block.rings.RingsGoauld;
import dev.tauri.jsgtransporters.common.block.rings.RingsOri;
import net.minecraft.world.level.block.Block;

public class JSGTBlocks {
    private static final DeferredRegisterWrapper<Block> REGISTER = JSGTransporters.REGISTRY_HELPER.block();

    // TRANSPORT RINGS
    public static final RegistryObjectWrapper<Block> RINGS_ANCIENT = REGISTER.register("ancient_transport_rings", RingsAncient::new);
    public static final RegistryObjectWrapper<Block> RINGS_GOAULD = REGISTER.register("goauld_transport_rings", RingsGoauld::new);
    public static final RegistryObjectWrapper<Block> RINGS_ORI = REGISTER.register("ori_transport_rings", RingsOri::new);

    // TRANSPORT RINGS CONTROL PANELS
    public static final RegistryObjectWrapper<Block> RINGS_CP_GOAULD = REGISTER.register("goauld_rings_control_panel", RingsGoauldCPBlock::new);
    public static final RegistryObjectWrapper<Block> RINGS_CP_ORI = REGISTER.register("ori_rings_control_panel", RingsOriCPBlock::new);
    public static final RegistryObjectWrapper<Block> RINGS_CP_ANCIENT = REGISTER.register("ancient_rings_control_panel", RingsAncientCPBlock::new);

    // LANTEAN TRANSPORTER
    public static final RegistryObjectWrapper<Block> LANTEAN_TRANSPORTER_ACCESS_PANEL = REGISTER.register("lantean_transporter_access_panel", LanteanTransporterAccessPanel::new);
    public static final RegistryObjectWrapper<Block> LANTEAN_TRANSPORTER_FRAME = REGISTER.register("lantean_transporter_frame", LanteanTransporterFrame::new);
    public static final RegistryObjectWrapper<Block> LANTEAN_TRANSPORTER_PLATFORM = REGISTER.register("lantean_transporter_platform", LanteanTransporterPlatform::new);
    public static final RegistryObjectWrapper<Block> LANTEAN_TRANSPORTER_PANELING = REGISTER.register("lantean_transporter_paneling", LanteanTransporterPaneling::new);
    public static final RegistryObjectWrapper<Block> LANTEAN_TRANSPORTER_PYLON = REGISTER.register("lantean_transporter_pylon", LanteanTransporterPylon::new);
    public static final RegistryObjectWrapper<Block> LANTEAN_TRANSPORTER_SCREEN = REGISTER.register("lantean_transporter_screen", LanteanTransporterScreen::new);

    public static void init() {
    }
}
