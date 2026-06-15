package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.core.client.entity.AddressPageRenderable;
import dev.tauri.jsg.core.common.entity.NotebookPageType;
import dev.tauri.jsg.core.common.util.I18n;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.entity.RingsAddressData;
import dev.tauri.jsgtransporters.common.util.TooltipUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class JSGTNotebookPageTypes {
    public static final RegistryObject<NotebookPageType<RingsAddressData>> RINGS_ADDRESS = JSGTransporters.REGISTRY_HELPER.notebookPage().register("rings_address", () -> new NotebookPageType<>(
            AddressPageRenderable::new,
            RingsAddressData::new,
            ringsAddressData -> ringsAddressData != null ? ringsAddressData.serializeNBT() : null,
            (level, pos, random, data) -> null, // we don't want to generate addresses on cartouches now
            (stack, level, components, flag, data) -> {
                // TODO: Refactor and port this somehow to Core (abstractly)
                var displayIds = TooltipUtils.showAdvancedTooltip(flag);
                String text = I18n.format("item.jsg_core.page_notebook_filled.hold_shift");
                text = text.replaceAll("%key%", TooltipUtils.getShiftKeyName());
                components.add(Component.literal(text).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
                try {
                    if (data == null) return;
                    var stargateAddress = data.getAddress();
                    int[] symbolsToDisplay = data.getSymbolsToDisplay();

                    Map<Integer, Boolean> hashedSymbols = new HashMap<>();
                    for (int symbolId : symbolsToDisplay) {
                        hashedSymbols.put(symbolId, true);
                    }

                    for (int i = 0; i < 8; i++) {
                        if (hashedSymbols.get(i + 1) == null || !hashedSymbols.get(i + 1)) continue;
                        components.add(Component.literal(ChatFormatting.ITALIC + "" + (i < 6 ? ChatFormatting.AQUA : ChatFormatting.DARK_PURPLE) + stargateAddress.get(i).localize() + (displayIds ? (ChatFormatting.GRAY + " (" + stargateAddress.get(i).getId() + ")") : "")));
                    }
                } catch (Exception ignored) {
                }
            }
    ));

    public static void init() {
    }
}
