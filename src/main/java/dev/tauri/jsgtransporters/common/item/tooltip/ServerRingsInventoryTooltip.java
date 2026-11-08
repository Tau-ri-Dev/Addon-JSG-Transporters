package dev.tauri.jsgtransporters.common.item.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record ServerRingsInventoryTooltip(ItemStack stack) implements TooltipComponent {
}
