package dev.tauri.jsgtransporters.common.integration.oc2.methods;

import dev.tauri.jsg.integration.ComputerDeviceProvider;
import dev.tauri.jsg.integration.oc2.methods.AbstractOCMethods;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.integration.oc2.OCDevicesRegistry;

public class RingsOCMethods extends AbstractOCMethods<RingsAbstractBE> {
    public RingsOCMethods(ComputerDeviceProvider ringsTile) {
        super((RingsAbstractBE) ringsTile, OCDevicesRegistry.RINGS);
    }
}
