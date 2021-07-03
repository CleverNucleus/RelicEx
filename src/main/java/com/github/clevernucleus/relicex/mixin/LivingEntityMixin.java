package com.github.clevernucleus.relicex.mixin;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.relicex.RelicEx;
import com.github.clevernucleus.relicex.item.HealthPotionItem;
import com.github.clevernucleus.relicex.item.RelicItem;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Monster;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	
	@Inject(method = "dropLoot", at = @At("TAIL"))
	private void loot(DamageSource source, boolean causedByPlayer, CallbackInfo info) {
		LivingEntity entity = (LivingEntity)(Object)this;
		
		if(entity != null && entity instanceof Monster) {
			Random rand = new Random();
			Collection<Item> drops = new HashSet<Item>();
			float relics = (float)RelicEx.config().monstersDropRelicsChance * 0.01F;
			float heals = (float)RelicEx.config().monstersDropHealsChance * 0.01F;
			float greaterOrb = (float)RelicEx.config().monstersDropGreaterOrbChance * 0.01F;
			float lesserOrb = (float)RelicEx.config().monstersDropLesserOrbChance * 0.01F;
			float tome = (float)RelicEx.config().monstersDropTome * 0.01F;
			
			if(rand.nextFloat() < relics) {
				List<Item> items = RelicEx.ITEMS.stream().filter(item -> item instanceof RelicItem).collect(Collectors.toList());
				drops.add(items.get(rand.nextInt(items.size())));
			}
			
			if(rand.nextFloat() < heals) {
				List<Item> items = RelicEx.ITEMS.stream().filter(item -> item instanceof HealthPotionItem).collect(Collectors.toList());
				drops.add(items.get(rand.nextInt(items.size())));
			}
			
			if(rand.nextFloat() < greaterOrb) {
				drops.add(RelicEx.GREATER_ORB_OF_REGRET);
			}
			
			if(rand.nextFloat() < lesserOrb) {
				drops.add(RelicEx.LESSER_ORB_OF_REGRET);
			}
			
			if(rand.nextFloat() < tome) {
				drops.add(RelicEx.TOME);
			}
			
			for(Item drop : drops) {
				ItemStack stack = new ItemStack(drop, 1);
				entity.dropStack(stack);
				System.out.println("Yeet loot");
			}
		}
	}
}
