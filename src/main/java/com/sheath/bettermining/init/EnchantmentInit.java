package com.sheath.bettermining.init;

import com.mojang.serialization.MapCodec;
import com.sheath.bettermining.Bettermining;
import com.sheath.bettermining.enchantments.VeinMinerEnchantmentEffect;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class EnchantmentInit {
    public static final RegistryKey<net.minecraft.enchantment.Enchantment> VEIN_MINER = RegistryKey.of(RegistryKeys.ENCHANTMENT, Bettermining.id("veinminer"));
    public static final MapCodec<VeinMinerEnchantmentEffect> vein_miner = register("vein_miner", VeinMinerEnchantmentEffect.CODEC);

    private static RegistryKey<net.minecraft.enchantment.Enchantment> of(String path) {
        Identifier id = Identifier.of("bettermining", path);
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, id);
    }

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String name, MapCodec<T> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Bettermining.id(name), codec);
    }

    public static void register() {
        System.out.println("Registering Vein Miner Effect!");
    }
}

