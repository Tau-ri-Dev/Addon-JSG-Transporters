package dev.tauri.jsgtransporters.common.integration.oc.methods;

import dev.tauri.jsgcore.common.integration.ComputerDeviceProvider;
import dev.tauri.jsgcore.common.integration.oc.methods.AbstractOCMethods;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.integration.oc.OCDevicesRegistry;

public class RingsOCMethods extends AbstractOCMethods<RingsAbstractBE> {
    public RingsOCMethods(ComputerDeviceProvider ringsTile) {
        super((RingsAbstractBE) ringsTile, OCDevicesRegistry.RINGS);
    }
}
