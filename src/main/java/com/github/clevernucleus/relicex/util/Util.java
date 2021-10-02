package com.github.clevernucleus.relicex.util;

import org.apache.commons.lang3.mutable.MutableFloat;

import com.github.clevernucleus.relicex.RelicEx;
import com.github.clevernucleus.relicex.impl.relic.EntityAttributeCollection;
import com.mojang.datafixers.util.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public final class Util {
	
	public static float randomAttribute(EntityAttributeCollection collectionIn) {
		RandDistribution<Identifier> distributor = new RandDistribution<Identifier>(null);
		RandDistribution<Pair<Double, Float>> values = new RandDistribution<Pair<Double, Float>>(new Pair<Double, Float>(0.0D, 1.0F));
		
		for(Identifier identifier : RelicEx.RARITY_MANAGER.keys()) {
			WeightProperty weight = RelicEx.RARITY_MANAGER.weight(identifier);
			distributor.add(identifier, weight.rarity());
		}
		
		Identifier identifier = distributor.getDistributedRandom();
		if(identifier == null) return 0.0F;
		
		WeightProperty weight = RelicEx.RARITY_MANAGER.weight(identifier);
		if(weight == null) return 1.0F;
		
		MutableFloat result = new MutableFloat(1.0F);
		
		weight.processValues((op, mult, min, max, incr) -> {
			for(double i = min; i < max; i += incr) {
				float w = 1.0F - (float)(i / max);
				values.add(new Pair<Double, Float>(i, w), w);
			}
			
			Pair<Double, Float> pair = values.getDistributedRandom();
			double value = pair.getFirst();
			float multiplier = pair.getSecond();
			
			if(!collectionIn.contains(identifier)) {
				collectionIn.put(identifier, value, op);
				result.setValue((0.5F * multiplier) + (0.2F * mult));
			}
		});
		
		return result.floatValue() + (0.3F * weight.rarity());
	}
	
	public static void createRelic(final ItemStack stackIn) {
		if(stackIn == null) return;
		
		NbtCompound tag = stackIn.getOrCreateNbt();
		
		if(tag.contains("Attributes")) return;
		
		EntityAttributeCollection collection = new EntityAttributeCollection();
		float weight = randomAttribute(collection);
		float random = (float)Math.random();
		
		for(int i = 0; i < 5; i++) {
			if(random > weight) continue;
			
			weight *= randomAttribute(collection);
		}
		
		collection.writeToNbt(tag);
		
		Rareness rareness = Rareness.fromWeight(weight);
		tag.putString("Rareness", rareness.toString());
	}
}
