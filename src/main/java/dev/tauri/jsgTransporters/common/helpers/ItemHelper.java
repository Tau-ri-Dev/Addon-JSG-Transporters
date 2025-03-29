package dev.tauri.jsgTransporters.common.helpers;

import com.mojang.blaze3d.platform.InputConstants;
import dev.tauri.jsg.item.JSGItem;
import dev.tauri.jsg.item.JSGSpawnEggItem;
import dev.tauri.jsgTransporters.common.registry.ItemRegistry;
import dev.tauri.jsg.util.I18n;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

import static dev.tauri.jsgTransporters.JSGTransporters.MOD_ID;

@SuppressWarnings("unused")
public class ItemHelper {

    public static RegistryObject<JSGSpawnEggItem> createSpawnEgg(String entityName, Supplier<? extends EntityType<? extends Mob>> entityType, int color1, int color2) {
        return ItemRegistry.REGISTER.register(entityName + "_spawn_egg", () -> new JSGSpawnEggItem(entityType, color1, color2, new Item.Properties()));
    }

    public static RegistryObject<JSGItem> createGenericItem(String name, RegistryObject<CreativeModeTab> tab) {
        return ItemRegistry.REGISTER.register(name, () -> new JSGItem(new Item.Properties(), tab));
    }

    public static RegistryObject<JSGItem> createFoodItem(String name, FoodProperties props, RegistryObject<CreativeModeTab> tab) {
        return ItemRegistry.REGISTER.register(name, () -> new JSGItem(new Item.Properties().food(props), tab));
    }

    public static RegistryObject<JSGItem> createGenericItemWithGenericTooltip(String name, RegistryObject<CreativeModeTab> tab) {
        return createGenericItem(name,
                () -> List.of(Component.translatable("item." + MOD_ID + "." + name + ".tooltip").withStyle(ChatFormatting.GRAY)),
                I18n.getAdvancedTooltip("item." + MOD_ID + "." + name + ".tooltip.extended", (i, line) -> line.withStyle(ChatFormatting.GRAY)), tab);
    }

    public interface TooltipFunction {
        List<Component> run();
    }

    public static RegistryObject<JSGItem> createGenericItem(String name, @Nonnull TooltipFunction tooltip, @Nullable I18n.AdvancedTooltip tooltipAdvanced, RegistryObject<CreativeModeTab> tab) {
        return ItemRegistry.REGISTER.register(name, () -> new JSGItem(new Item.Properties(), tab) {
            @Override
            @ParametersAreNonnullByDefault
            public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
                applyToolTip(tooltip.run(), tooltipAdvanced, components, tooltipFlag);
            }
        });
    }

    public static RegistryObject<JSGItem> createDurabilityItem(String name, int durability, boolean shouldStayInCrafting, RegistryObject<CreativeModeTab> tab) {
        return createDurabilityItem(name, durability, shouldStayInCrafting, 64, tab);
    }

    public static RegistryObject<JSGItem> createDurabilityItem(String name, int durability, boolean shouldStayInCrafting, int maxStack, RegistryObject<CreativeModeTab> tab) {
        return createDurabilityItem(name, durability, shouldStayInCrafting, maxStack, null, null, tab);
    }

    public static RegistryObject<JSGItem> createDurabilityItemWithGenericTooltip(String name, int durability, boolean shouldStayInCrafting, int maxStack, RegistryObject<CreativeModeTab> tab) {
        return createDurabilityItem(name, durability, shouldStayInCrafting, maxStack,
                List.of(Component.translatable("item." + MOD_ID + "." + name + ".tooltip").withStyle(ChatFormatting.GRAY)),
                I18n.getAdvancedTooltip("item." + MOD_ID + "." + name + ".tooltip.extended", (i, line) -> line.withStyle(ChatFormatting.GRAY)), tab);
    }

    public static void applyGenericToolTip(String itemName, List<Component> components, TooltipFlag tooltipFlag) {
        applyToolTip(
                List.of(Component.translatable("item." + MOD_ID + "." + itemName + ".tooltip").withStyle(ChatFormatting.GRAY)),
                I18n.getAdvancedTooltip("item." + MOD_ID + "." + itemName + ".tooltip.extended", (i, line) -> line.withStyle(ChatFormatting.GRAY)),
                components, tooltipFlag
        );
    }

    public static void applyToolTip(@Nullable List<Component> tooltip, @Nullable I18n.AdvancedTooltip tooltipAdvanced, List<Component> components, TooltipFlag tooltipFlag) {
        if (tooltip == null) return;
        int key = InputConstants.KEY_LSHIFT;
        components.addAll(tooltip);
        boolean isKeyDown = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key);
        if ((isKeyDown || tooltipFlag.isAdvanced()) && tooltipAdvanced != null && tooltipAdvanced.formatLines() != null) {
            int width = tooltipAdvanced.getWidth() + 2;
            components.add(Component.literal(" ".repeat(width)).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.STRIKETHROUGH));
            components.addAll(tooltipAdvanced.formatLines());
            components.add(Component.literal(" ".repeat(width)).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.STRIKETHROUGH));
        } else if (tooltipAdvanced != null && tooltipAdvanced.formatLines() != null) {
            String text = Component.translatable("tooltip.general.hold_shift").getString();
            text = text.replaceAll("%key%", InputConstants.Type.KEYSYM.getOrCreate(key).getDisplayName().getString());
            components.add(Component.literal(text).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }

    public static RegistryObject<JSGItem> createDurabilityItem(String name, int durability, boolean shouldStayInCrafting, int maxStack, @Nullable List<Component> tooltip, @Nullable I18n.AdvancedTooltip tooltipAdvanced, RegistryObject<CreativeModeTab> tab) {
        return ItemRegistry.REGISTER.register(name, () -> {
            Item.Properties props = new Item.Properties().stacksTo(maxStack);
            if (durability > 0)
                props.durability(durability);
            return new JSGItem(props, tab) {

                @Override
                @ParametersAreNonnullByDefault
                public boolean doesSneakBypassUse(ItemStack stack, LevelReader world, BlockPos pos, Player player) {
                    return true;
                }

                @Override
                public boolean isDamageable(ItemStack stack) {
                    return true;
                }

                @Override
                @ParametersAreNonnullByDefault
                public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
                    applyToolTip(tooltip, tooltipAdvanced, components, tooltipFlag);
                    components.add(Component.empty());
                    components.add(Component.literal(String.format("%.2f", (((double) (getMaxDamage(stack) - getDamage(stack)) / ((double) getMaxDamage(stack))) * 100)) + "%").withStyle(ChatFormatting.GRAY));
                }

                @Nonnull
                @Override
                public ItemStack getDefaultInstance() {
                    ItemStack itemStack = new ItemStack(this);
                    setDamage(itemStack, 0);
                    return itemStack;
                }

                @Override
                public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, @Nonnull Enchantment enchantment) {
                    return (enchantment instanceof DigDurabilityEnchantment);
                }

                @Override
                @SuppressWarnings("deprecation")
                public int getEnchantmentValue() {
                    return 3;
                }

                @Override
                public boolean hasCraftingRemainingItem(@Nonnull ItemStack stack) {
                    return shouldStayInCrafting;
                }

                @Override
                public void setDamage(ItemStack stack, int damage) {
                    super.setDamage(stack, damage);
                    if (getMaxDamage(stack) <= damage) stack.setCount(0);
                }

                @Override
                public int getMaxDamage(ItemStack stack) {
                    return durability;
                }

                @Nonnull
                @Override
                public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
                    ItemStack it = itemStack.copy();
                    it.setDamageValue(itemStack.getDamageValue() + 1);
                    return it;
                }
            };
        });

    }

}
