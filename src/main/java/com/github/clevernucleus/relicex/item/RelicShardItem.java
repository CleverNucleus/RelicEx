package com.github.clevernucleus.relicex.item;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class RelicShardItem extends Item {
	public RelicShardItem() {
		super((new Item.Settings()).fireproof().group(ItemGroup.MATERIALS));
	}
	
	@Override
	public Rarity getRarity(ItemStack stack) {
		return Rarity.UNCOMMON;
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		
		if(!world.isClient) {
			int i = 3 + world.random.nextInt(5) + world.random.nextInt(5);
			ExperienceOrbEntity.spawn((ServerWorld)world, user.getPos(), i);
		}
		
		world.playSound(user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 1.0F, 1.0F, true);
		
		if(!user.isCreative()) {
			itemStack.decrement(1);
		}
		
		return TypedActionResult.success(itemStack, world.isClient);
	}
}
