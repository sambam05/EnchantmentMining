package com.sheath.bettermining.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(ServerPlayerInteractionManager.class)
public class VeinMinerMixin {
    @Unique
    private void breakConnectedOres(ServerWorld world, BlockPos pos, Block targetBlock, PlayerEntity player, Set<BlockPos> visited, int limit) {
        if (visited.size() >= limit) {
            return;
        }
        visited.add(pos);

        for (BlockPos offset : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            if (!visited.contains(offset) && world.getBlockState(offset).getBlock() == targetBlock) {
                world.setBlockState(offset, net.minecraft.block.Blocks.AIR.getDefaultState(), 3); // Break block
                world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, offset);
                breakConnectedOres(world, offset, targetBlock, player, visited, limit);
            }
        }
    }

    @Inject(method = "tryBreakBlock", at = @At("HEAD"))
    private void onBlockBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerEntity player = ((ServerPlayerInteractionManagerAccessor) this).getPlayer();
        World world = player.getWorld();
        BlockState state = world.getBlockState(pos);

        // Get the enchantment from the registry
        RegistryEntry<Enchantment> vein_miner = world.getRegistryManager()
                .getOrThrow(RegistryKeys.ENCHANTMENT)
                .getOrThrow(com.sheath.bettermining.init.EnchantmentInit.VEIN_MINER);

        // Check if the tool has Vein Miner enchantment
        ItemStack tool = player.getMainHandStack();
        if (EnchantmentHelper.getLevel(vein_miner, tool) > 0) {

            // Check if block is an ore (manual check since BlockTags.ORES may not exist)
            if (isOre(state))  {
                breakConnectedOres((ServerWorld) world, pos, state.getBlock(), player, new HashSet<>(), 64);
            }
        }
    }
    @Unique
    private static final Set<Block> ORES = Set.of(
            Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE,
            Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
            Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE
    );

    @Unique
    private static boolean isOre(BlockState state) {
        return ORES.contains(state.getBlock());
    }
}

