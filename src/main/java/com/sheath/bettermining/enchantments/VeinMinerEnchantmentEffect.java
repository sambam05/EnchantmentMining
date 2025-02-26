package com.sheath.bettermining.enchantments;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.enchantment.EnchantmentEffectContext;

import java.util.HashSet;
import java.util.Set;

public record VeinMinerEnchantmentEffect(int range) implements EnchantmentEntityEffect {
    public static final MapCodec<VeinMinerEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    com.mojang.serialization.Codec.INT.fieldOf("range").forGetter(VeinMinerEnchantmentEffect::range)
            ).apply(instance, VeinMinerEnchantmentEffect::new));

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, net.minecraft.entity.Entity target, Vec3d pos) {
        if (context.owner() instanceof net.minecraft.entity.player.PlayerEntity player) {
            BlockPos startPos = BlockPos.ofFloored(pos);
            BlockState targetState = world.getBlockState(startPos);

            Set<BlockPos> toBreak = new HashSet<>();
            findConnectedBlocks(world, startPos, targetState.getBlock(), toBreak, level * range);

            for (BlockPos blockPos : toBreak) {
                world.breakBlock(blockPos, true, player);
                world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, blockPos);
            }
        }
    }

    private void findConnectedBlocks(ServerWorld world, BlockPos pos, Block targetBlock, Set<BlockPos> visited, int limit) {
        if (visited.size() >= limit) return;
        visited.add(pos);

        for (BlockPos offset : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            if (!visited.contains(offset) && world.getBlockState(offset).getBlock() == targetBlock) {
                findConnectedBlocks(world, offset, targetBlock, visited, limit);
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
