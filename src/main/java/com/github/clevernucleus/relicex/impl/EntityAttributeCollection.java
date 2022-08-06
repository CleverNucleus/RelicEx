package com.github.clevernucleus.relicex.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.dataattributes.api.util.RandDistribution;
import com.github.clevernucleus.relicex.RelicEx;
import com.google.common.collect.Multimap;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public final class EntityAttributeCollection {
	public static final String KEY_ATTRIBUTES = "Attributes";
	public static final String KEY_NAME = "Name";
	public static final String KEY_VALUE = "Value";
	public static final String KEY_OPERATION = "Operation";
	public static final String KEY_RARENESS = "Rareness";
	private final Map<Identifier, Consumer<BiConsumer<Double, Operation>>> values;
	
	public EntityAttributeCollection() {
		this.values = new HashMap<>();
	}
	
	private static float randomAttribute(EntityAttributeCollection collection) {
		RandDistribution<Identifier> distributor = new RandDistribution<>(null);
		RandDistribution<Pair<Double, Float>> values = new RandDistribution<>(new Pair<Double, Float>(0.0D, 1.0F));
		
		for(Identifier identifier : RelicEx.RARITY_MANAGER.keys()) {
			WeightProperty weight = RelicEx.RARITY_MANAGER.weight(identifier);
			distributor.add(identifier, weight.rarity());
		}
		
		Identifier identifier = distributor.getDistributedRandom();
		if(identifier == null) return 1.0F;
		
		WeightProperty weight = RelicEx.RARITY_MANAGER.weight(identifier);
		if(weight == null) return 1.0F;
		
		return weight.processValue((op, mult, min, max, incr) -> {
			for(double i = min; i < max; i += incr) {
				float w = 1.0F - (float)(i / max);
				values.add(new Pair<Double, Float>(i, w), w);
			}
			
			Pair<Double, Float> pair = values.getDistributedRandom();
			double value = pair.getLeft();
			float multiplier = pair.getRight();
			
			if(!collection.values.containsKey(identifier)) {
				collection.values.put(identifier, consumer -> consumer.accept(value, op));
				return (0.5F * multiplier) + (0.2F * mult);
			}
			
			return 1.0F;
		}) + (0.3F * weight.rarity());
	}
	
	public static void readFromNbt(NbtCompound tag, String slot, Multimap<EntityAttribute, EntityAttributeModifier> modifiers) {
		if(!tag.contains(KEY_ATTRIBUTES, NbtType.LIST)) return;
		NbtList list = tag.getList(KEY_ATTRIBUTES, NbtType.COMPOUND);
		
		for(int i = 0; i < list.size(); i++) {
			NbtCompound entry = list.getCompound(i);
			Identifier identifier = new Identifier(entry.getString(KEY_NAME));
			Supplier<EntityAttribute> attribute = DataAttributesAPI.getAttribute(identifier);
			
			if(attribute.get() == null) continue;
			Operation operation = Operation.fromId((int)entry.getByte(KEY_OPERATION));
			EntityAttributeModifier modifier = new EntityAttributeModifier(SlotKey.from(slot).uuid(), "RelicEx Modifier", entry.getDouble(KEY_VALUE), operation);
			modifiers.put(attribute.get(), modifier);
		}
	}
	
	public static float getValueIfArmor(NbtCompound tag, EntityAttribute attributeIn) {
		if(!tag.contains(KEY_ATTRIBUTES, NbtType.LIST)) return 0.0F;
		NbtList list = tag.getList(KEY_ATTRIBUTES, NbtType.COMPOUND);
		
		for(int i = 0; i < list.size(); i++) {
			NbtCompound entry = list.getCompound(i);
			Identifier identifier = new Identifier(entry.getString(KEY_NAME));
			Supplier<EntityAttribute> attribute = DataAttributesAPI.getAttribute(identifier);
			
			if(attribute.get() == null || !attribute.get().equals(attributeIn) || (int)entry.getByte(KEY_OPERATION) != Operation.ADDITION.getId()) continue;
			return (float)entry.getDouble(KEY_VALUE);
		}
		
		return 0.0F;
	}
	
	public void writeToNbt(NbtCompound tag) {
		if(tag.contains(KEY_ATTRIBUTES, NbtType.LIST)) return;
		float weight = randomAttribute(this);
		float random = (float)Math.random();
		
		for(int i = 0; i < 4; i++) {
			if(random > weight) continue;
			weight *= randomAttribute(this);
		}
		
		NbtList list = new NbtList();
		
		this.values.forEach((key, consumer) -> {
			NbtCompound entry = new NbtCompound();
			entry.putString(KEY_NAME, key.toString());
			consumer.accept((value, operation) -> {
				entry.putDouble(KEY_VALUE, value);
				entry.putByte(KEY_OPERATION, (byte)operation.getId());
			});
			list.add(entry);
		});
		
		Rareness rareness = Rareness.fromWeight(weight);
		tag.putString(KEY_RARENESS, rareness.toString());
		tag.put(KEY_ATTRIBUTES, list);
	}
}
