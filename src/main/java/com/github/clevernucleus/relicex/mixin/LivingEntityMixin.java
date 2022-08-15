package com.github.clevernucleus.relicex.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.dataattributes.api.util.RandDistribution;
import com.github.clevernucleus.relicex.RelicEx;
import com.github.clevernucleus.relicex.config.RelicExConfig;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Monster;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {
	
	@Inject(method = "dropLoot", at = @At("TAIL"))
	private void relicex_dropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity)(Object)this;
		
		if(!(livingEntity instanceof Monster)) return;
		RelicExConfig config = RelicEx.config();
		Random random = new Random();
		
		if(config.dropsOnlyFromPlayerKills && !causedByPlayer) return;
		float chance = 0.01F * (float)config.mobsDropLootChance;
		float roll = (source.getAttacker() instanceof LivingEntity) ? DataAttributesAPI.ifPresent((LivingEntity)source.getAttacker(), () -> EntityAttributes.GENERIC_LUCK, chance, value -> chance * (1.0F + (float)(double)value)) : chance;
		
		if(!(random.nextFloat() < roll)) return;
		RandDistribution<Item> distributor = new RandDistribution<Item>(Items.AIR);
		distributor.add(RelicEx.RELICS.get(random.nextInt(RelicEx.RELICS.size())), 0.01F * (float)config.mobDropIsRelicChance);
		distributor.add(RelicEx.LESSER_ORB_OF_REGRET, 0.01F * (float)config.mobDropIsLesserOrbChance);
		distributor.add(RelicEx.GREATER_ORB_OF_REGRET, 0.01F * (float)config.mobDropIsGreaterOrbChance);
		distributor.add(RelicEx.TOME, 0.01F * (float)config.mobDropIsTomeChance);
		distributor.add(RelicEx.POTIONS.get(random.nextInt(RelicEx.POTIONS.size())), 0.01F * (float)config.mobDropIsPotionChance);
		
		Item item = distributor.getDistributedRandom();
		livingEntity.dropStack(new ItemStack(item, 1));
	}
}
