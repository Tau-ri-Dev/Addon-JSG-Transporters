package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAncientBE;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsGoauldBE;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsOriBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static dev.tauri.jsgtransporters.JSGTransporters.MOD_ID;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);

    /**
     * TRANSPORT RINGS
     */
    public static final RegistryObject<BlockEntityType<RingsAncientBE>> RINGS_ANCIENT_BE = registerBR("rings_ancient_block", RingsAncientBE::new, BlockRegistry.RINGS_ANCIENT);
    public static final RegistryObject<BlockEntityType<RingsGoauldBE>> RINGS_GOAULD_BE = registerBR("rings_goauld_block", RingsGoauldBE::new, BlockRegistry.RINGS_GOAULD);
    public static final RegistryObject<BlockEntityType<RingsOriBE>> RINGS_ORI_BE = registerBR("rings_ori_block", RingsOriBE::new, BlockRegistry.RINGS_ORI);


    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBR(String name, BlockEntityType.BlockEntitySupplier<T> beSupplier, Supplier<? extends Block> blockSupplier) {
        return registerBR(name, beSupplier, List.of(blockSupplier));
    }

    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBR(String name, BlockEntityType.BlockEntitySupplier<T> beSupplier, List<? extends Supplier<? extends Block>> blockSuppliers) {
        return REGISTER.register(name, () -> {
            List<Block> blocks = new ArrayList<>();
            for (var object : blockSuppliers) {
                blocks.add(object.get());
            }
            return BlockEntityType.Builder.of(beSupplier, blocks.toArray(new Block[0])).build(null);
        });
    }


    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }

    @SubscribeEvent
    public static void registerBERs(EntityRenderersEvent.RegisterRenderers event) {
    }
}
