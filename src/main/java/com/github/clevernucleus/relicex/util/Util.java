package com.github.clevernucleus.relicex.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.attribute.AttributeProperties;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.relicex.RelicEx;
import com.github.clevernucleus.relicex.impl.Modifiers;
import com.github.clevernucleus.relicex.impl.UUIDCache;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public final class Util {
	public static float randomAttribute(Map<IPlayerAttribute, Double> attributeMapIn) {
		RandDistribution<IPlayerAttribute> randomAttribute = new RandDistribution<IPlayerAttribute>(null);
		RandDistribution<Pair<Double, Float>> randomValue = new RandDistribution<Pair<Double, Float>>(new Pair<Double, Float>(0.0D, 1.0F));
		
		for(Map.Entry<Identifier, IPlayerAttribute> entry : ExAPI.REGISTRY.get().attributes().entrySet()) {
			IPlayerAttribute attribute = entry.getValue();
			
			if(attribute.hasProperty(AttributeProperties.PROPERTY_WEIGHT)) {
				float weight = 0.4F + attribute.getProperty(AttributeProperties.PROPERTY_WEIGHT);
				randomAttribute.add(attribute, Math.max(weight, 0.95F));
			}
		}
		
		IPlayerAttribute attribute = randomAttribute.getDistributedRandom();
		
		double min = attribute.getProperty(AttributeProperties.PROPERTY_MIN_ROLL);
		double max = attribute.getProperty(AttributeProperties.PROPERTY_MAX_ROLL);
		float weightF = 0.4F + attribute.getProperty(AttributeProperties.PROPERTY_WEIGHT);
		float attributeWeight = Math.max(weightF, 0.95F);
		
		if(min <= 0.0D) return 1.0F;
		
		for(double i = min; i < max; i += min) {
			float weight = 1.0F - (float)(i / max);
			randomValue.add(new Pair<Double, Float>(i, weight), weight);
		}
		
		Pair<Double, Float> result = randomValue.getDistributedRandom();
		double attributeValue = result.getLeft();
		float valueWeight = result.getRight();
		float totalWeight = attributeWeight * valueWeight;
		
		if(!attributeMapIn.containsKey(attribute)) {
			attributeMapIn.put(attribute, attributeValue);
			return totalWeight;
		}
		
		return 1.0F;
	}
	
	public static int weightedRange(final int size) {
		RandDistribution<Integer> rand = new RandDistribution<Integer>(1);
		
		for(int i = 1; i < size + 1; i++) {
			float weight = 1.0F - ((float)i / (float)size);
			rand.add(i, weight);
		}
		
		return rand.getDistributedRandom();
	}
	
	public static void createRelic(ItemStack stack) {
		if(ExAPI.REGISTRY.get().attributes().isEmpty()) return;
		CompoundTag tag = stack.getOrCreateTag();
		
		if(tag.contains("Attributes")) return;
		
		Map<IPlayerAttribute, Double> attributeMap = new HashMap<IPlayerAttribute, Double>();
		float weight = 1.0F;
		int n = weightedRange(5);
		
		for(int i = 0; i < n; i++) {
			weight *= randomAttribute(attributeMap);
		}
		
		writeAttributeMap(attributeMap, tag);
		Rareness rareness = Rareness.from(weight);
		rareness.write(tag);
	}
	
	public static void appendBytes(Map<IPlayerAttribute, Byte> uuidMapIn, CompoundTag tagIn, PlayerEntity player) {
		if(uuidMapIn == null || tagIn == null || player == null) return;
		if(!tagIn.contains("Attributes") || player.world.isClient) return;
		
		UUIDCache cache = RelicEx.CACHE.get(player);
		ListTag list = tagIn.getList("Attributes", NbtType.COMPOUND);
		
		for(int i = 0; i < list.size(); i++) {
			CompoundTag tag = list.getCompound(i);
			String name = tag.getString("Name");
			Identifier key = new Identifier(name);
			IPlayerAttribute attribute = ExAPI.REGISTRY.get().getAttribute(key);
			
			if(attribute != null) {
				if(tag.contains("UUID")) {
					byte id = tag.getByte("UUID");
					
					uuidMapIn.put(attribute, id);
				} else {
					Collection<Byte> used = cache.used();
					byte id = Modifiers.getUnused(used);
					
					tag.putByte("UUID", id);
					uuidMapIn.put(attribute, id);
					cache.add(id);
				}
			}
		}
	}
	
	public static void removeBytes(Map<IPlayerAttribute, Byte> uuidMapIn, CompoundTag tagIn, PlayerEntity player) {
		if(uuidMapIn == null || tagIn == null || player == null) return;
		if(!tagIn.contains("Attributes") || player.world.isClient) return;
		
		UUIDCache cache = RelicEx.CACHE.get(player);
		ListTag list = tagIn.getList("Attributes", NbtType.COMPOUND);
		
		for(int i = 0; i < list.size(); i++) {
			CompoundTag tag = list.getCompound(i);
			String name = tag.getString("Name");
			Identifier key = new Identifier(name);
			IPlayerAttribute attribute = ExAPI.REGISTRY.get().getAttribute(key);
			
			if(attribute != null) {
				byte id = tag.getByte("UUID");
				
				cache.remove(id);
				uuidMapIn.put(attribute, id);
				tag.remove("UUID");
			}
		}
	}
	
	public static void writeAttributeMap(Map<IPlayerAttribute, Double> attributeMapIn, CompoundTag tagIn) {
		if(attributeMapIn == null || tagIn == null) return;
		
		ListTag list = new ListTag();
		
		for(Map.Entry<IPlayerAttribute, Double> entry : attributeMapIn.entrySet()) {
			CompoundTag tag = new CompoundTag();
			tag.putString("Name", entry.getKey().registryKey().toString());
			tag.putDouble("Value", entry.getValue());
			list.add(tag);
		}
		
		tagIn.put("Attributes", list);
	}
	
	public static void readAttributeMap(Map<IPlayerAttribute, Double> attributeMapIn, CompoundTag tagIn) {
		if(attributeMapIn == null || tagIn == null) return;
		if(!tagIn.contains("Attributes")) return;
		
		ListTag list = tagIn.getList("Attributes", NbtType.COMPOUND);
		
		for(int i = 0; i < list.size(); i++) {
			CompoundTag tag = list.getCompound(i);
			String name = tag.getString("Name");
			double value = tag.getDouble("Value");
			Identifier key = new Identifier(name);
			IPlayerAttribute attribute = ExAPI.REGISTRY.get().getAttribute(key);
			
			if(attribute != null) {
				attributeMapIn.put(attribute, value);
			}
		}
	}
}
