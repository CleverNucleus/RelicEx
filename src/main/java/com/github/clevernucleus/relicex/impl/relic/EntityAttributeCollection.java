package com.github.clevernucleus.relicex.impl.relic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.github.clevernucleus.relicex.impl.UUIDCacheManager;
import com.google.common.collect.Multimap;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class EntityAttributeCollection {
	private final List<EntityAttributeModifierValue> values;
	
	public EntityAttributeCollection() {
		this.values = new ArrayList<EntityAttributeModifierValue>();
	}
	
	public int size() {
		return this.values.size();
	}
	
	public boolean contains(final Identifier identifierIn) {
		return this.values.stream().map(EntityAttributeModifierValue::key).anyMatch(identifierIn::equals);
	}
	
	public void put(final Identifier identifierIn, final double valueIn, final EntityAttributeModifier.Operation operation) {
		EntityAttributeModifierValue entityAttributeModifierValue = new EntityAttributeModifierValue(this.values.size(), identifierIn, "RelicEx Modifier", valueIn, operation);
		
		this.values.add(entityAttributeModifierValue);
	}
	
	public void get(final SlotKey key, final Multimap<EntityAttribute, EntityAttributeModifier> modifiers) {
		UUID[] uuid = UUIDCacheManager.get(key);
		
		for(EntityAttributeModifierValue value : this.values) {
			EntityAttribute attribute = Registry.ATTRIBUTE.get(value.key());
			
			if(attribute == null) continue;
			
			EntityAttributeModifier modifier = value.modifier(uuid);
			modifiers.put(attribute, modifier);
		}
	}
	
	public void writeToNbt(NbtCompound tag) {
		NbtList list = new NbtList();
		
		for(EntityAttributeModifierValue value : this.values) {
			NbtCompound entry = new NbtCompound();
			value.writeToNbt(entry);
			list.add(entry);
		}
		
		tag.put("Attributes", list);
	}
	
	public void readFromNbt(NbtCompound tag) {
		if(!tag.contains("Attributes")) return;
		
		NbtList list = tag.getList("Attributes", NbtType.COMPOUND);
		
		for(int i = 0; i < list.size(); i++) {
			NbtCompound entry = list.getCompound(i);
			EntityAttributeModifierValue value = EntityAttributeModifierValue.readFromNbt(entry);
			
			this.values.add(value);
		}
	}
	
	public static final class EntityAttributeModifierValue {
		private final Identifier key;
		private final double value;
		private final int index;
		private final EntityAttributeModifier.Operation operation;
		private final String name;
		
		private EntityAttributeModifierValue(final int index, final Identifier key, final String name, final double value, final EntityAttributeModifier.Operation operation) {
			this.index = index;
			this.key = key;
			this.name = name;
			this.value = value;
			this.operation = operation;
		}
		
		public Identifier key() {
			return this.key;
		}
		
		public EntityAttributeModifier modifier(final UUID[] uuid) {
			return new EntityAttributeModifier(uuid[this.index], this.name, this.value, this.operation);
		}
		
		public int index() {
			return this.index;
		}
		
		public void writeToNbt(NbtCompound tag) {
			tag.putString("Identifier", this.key.toString());
			tag.putString("Name", this.name);
			tag.putInt("Operation", this.operation.getId());
			tag.putDouble("Value", this.value);
			tag.putInt("Index", this.index);
		}
		
		public static EntityAttributeModifierValue readFromNbt(NbtCompound tag) {
			Identifier key = new Identifier(tag.getString("Identifier"));
			String name = tag.getString("Name");
			EntityAttributeModifier.Operation operation = EntityAttributeModifier.Operation.fromId(tag.getInt("Operation"));
			double value = tag.getDouble("Value");
			int index = tag.getInt("Index");
			
			EntityAttributeModifierValue entityAttributeModifierValue = new EntityAttributeModifierValue(index, key, name, value, operation);
			
			return entityAttributeModifierValue;
		}
	}
}
