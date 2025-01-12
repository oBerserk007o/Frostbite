package com.frostbyte.frostbite.item;

import com.frostbyte.frostbite.Frostbite;
import com.frostbyte.frostbite.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Frostbite.MOD_ID);

    public static final Supplier<CreativeModeTab> FROSTBITE_TAB =
            CREATIVE_MODE_TABS.register("frostbite_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.frostbite.frostbite_tab"))
                    .icon(() -> new ItemStack(ModItems.METAL_COG.get()))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModItems.ADVANCED_CLOCK);
                        output.accept(ModItems.METAL_COG);

                        output.accept(ModBlocks.BLACK_BLOCK);

                        output.accept(ModItems.FUR_HELMET);
                        output.accept(ModItems.FUR_CHESTPLATE);
                        output.accept(ModItems.FUR_LEGGINGS);
                        output.accept(ModItems.FUR_BOOTS);

                        output.accept(ModItems.EXPLODING_SNOWBALL);
                        output.accept(ModItems.HARDENED_SNOWBALL);
                        output.accept(ModItems.PACKED_HARDENED_SNOWBALL);
                        output.accept(ModItems.BLUE_HARDENED_SNOWBALL);

                        output.accept(ModItems.DRAINING_STAFF);
                    })).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
