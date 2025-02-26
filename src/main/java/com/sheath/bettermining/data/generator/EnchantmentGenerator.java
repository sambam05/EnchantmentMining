package com.sheath.bettermining.data.generator;

import com.sheath.bettermining.enchantments.VeinMinerEnchantmentEffect;
import com.sheath.bettermining.init.EnchantmentInit;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.enchantment.Enchantment;
import java.util.concurrent.CompletableFuture;

public class EnchantmentGenerator extends FabricDynamicRegistryProvider {
    public EnchantmentGenerator(net.fabricmc.fabric.api.datagen.v1.FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registeries, Entries entries) {
        RegistryWrapper<Item> itemLookup = registeries.getOrThrow(RegistryKeys.ITEM);

        register(entries, Enchantment.builder(
                        Enchantment.definition(
                                itemLookup.getOrThrow(ItemTags.PICKAXES),
                                15, //Weight
                                1, //Max Level
                                Enchantment.leveledCost(1,5), //Base cost
                                Enchantment.leveledCost(1,10),//Max cost
                                4, //Anvil Cost
                                AttributeModifierSlot.HAND
                        ))
                .addEffect(EnchantmentEffectComponentTypes.HIT_BLOCK,

                        new VeinMinerEnchantmentEffect(64))
        );
    }


    private void register(Entries entries, Enchantment.Builder builder) {
        entries.add(EnchantmentInit.VEIN_MINER, builder.build(EnchantmentInit.VEIN_MINER.getValue()));
    }

    @Override
    public String getName() {
        return "VeinMinerEnchantmentGenerator";
    }
}
