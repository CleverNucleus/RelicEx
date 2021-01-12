package com.github.clevernucleus.relicex.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.github.clevernucleus.playerex.api.Limit;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.api.attribute.PlayerAttributes;
import com.mojang.datafixers.util.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

public class Util {
	
	/**
	 * @return A weighted random IPlayerAttribute instance, with a weighted random value and resultant chance in the format: [IPlayerInstance, [Value, Chance]].
	 */
	public static Pair<IPlayerAttribute, Pair<Double, Float>> randomAttribute() {
		RandDistribution<Pair<IPlayerAttribute, Float>> var0 = new RandDistribution<Pair<IPlayerAttribute, Float>>(null);
		
		for(IPlayerAttribute var : PlayerAttributes.attributes()) {
			if(var.type() == IPlayerAttribute.Type.ALL || var.type() == IPlayerAttribute.Type.GAME) {
				float var1 = (float)var.limit().weight();
				
				var0.add(Pair.of(var, var1), var1);
			}
		}
		
		Pair<IPlayerAttribute, Float> var1 = var0.getDistributedRandom();
		IPlayerAttribute var2 = var1.getFirst();
		Limit var3 = var2.limit();
		
		if(var3.increment() <= 0D) return Pair.of(null, Pair.of(0D, 0F));
		
		RandDistribution<Pair<Double, Float>> var4 = new RandDistribution<Pair<Double,Float>>(Pair.of(0D, 0F));
		
		for(double var = var3.minValue(); var < var3.maxValue(); var += var3.increment()) {
			float var5 = 1F - (float)(var / var3.maxValue());
			
			var4.add(Pair.of(var, var5), var5);
		}
		
		Pair<Double, Float> var5 = var4.getDistributedRandom();
		
		float var6 = (0.1F + var1.getSecond().floatValue()) * var5.getSecond().floatValue();
		
		return Pair.of(var2, Pair.of(var5.getFirst(), var6));
	}
	
	/**
	 * @return Creates a tag containing random attributes with weighting and rareness.
	 */
	public static CompoundNBT createRelicTag() {
		Map<IPlayerAttribute, Double> var0 = new HashMap<>();
		Pair<IPlayerAttribute, Pair<Double, Float>> var1 = randomAttribute();
		
		var0.put(var1.getFirst(), var1.getSecond().getFirst());
		
		float var2 = var1.getSecond().getSecond();
		
		while((float)(new Random()).nextInt(100) / 100F < var2 && var0.size() < 6) {
			Pair<IPlayerAttribute, Pair<Double, Float>> var3 = randomAttribute();
			IPlayerAttribute var4 = var3.getFirst();
			
			if(!var0.containsKey(var4)) {
				var0.put(var4, var3.getSecond().getFirst());
				var2 *= var3.getSecond().getSecond().floatValue();
			}
		}
		
		Rareness var3 = Rareness.range(var2);
		CompoundNBT var4 = var3.write();
		ListNBT var5 = new ListNBT();
		
		for(Map.Entry<IPlayerAttribute, Double> var : var0.entrySet()) {
			CompoundNBT var6 = new CompoundNBT();
			
			var6.putString("Name", var.getKey().toString());
			var6.putDouble("Value", var.getValue());
			var5.add(var6);
		}
		
		var4.put("Attributes", var5);
		
		return var4;
	}
	
	/**
	 * @param par0
	 * @return The input stack but with relic data on it.
	 */
	public static ItemStack createRandomRelic(ItemStack par0) {
		CompoundNBT var0 = createRelicTag();
		
		par0.setTag(var0);
		
		return par0;
	}
	
	/**
	 * @param par0
	 * @return Converts a tag holding attributes to a map.
	 */
	public static Map<IPlayerAttribute, Double> tagMap(final CompoundNBT par0) {
		Map<IPlayerAttribute, Double> var0 = new HashMap<>();
		
		if(par0 == null) return var0;
		if(!par0.contains("Attributes")) return var0;
		
		for(INBT var : par0.getList("Attributes", 10)) {
			CompoundNBT var1 = (CompoundNBT)var;
			IPlayerAttribute var2 = PlayerAttributes.fromRegistryName(var1.getString("Name"));
			
			var0.put(var2, var1.getDouble("Value"));
		}
		
		return var0;
	}
}
