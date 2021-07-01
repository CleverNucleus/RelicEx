package com.github.clevernucleus.relicex.item;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.attribute.AttributeData;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.relicex.impl.Modifiers;
import com.github.clevernucleus.relicex.util.Util;

import dev.emi.trinkets.api.Trinket;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class RelicItem extends Item implements Trinket {
	private final Collection<String> slotGroups, slots;
	
	public RelicItem(final Collection<String> slotGroups, final Collection<String> slots) {
		super((new Item.Settings()).maxCount(1).group(ItemGroup.COMBAT));
		this.slotGroups = slotGroups;
		this.slots = slots;
	}
	
	public RelicItem(final String slotGroup, final String slot) {
		this(Collections.singleton(slotGroup), Collections.singleton(slot));
	}
	
	@Override
	public boolean canWearInSlot(String group, String slot) {
		return this.slotGroups.contains(group) && this.slots.contains(slot);
	}
	
	@Override
	public void onEquip(PlayerEntity player, ItemStack stack) {
		if(player.world.isClient) return;
		if(!stack.hasTag()) return;
		
		AttributeData data = ExAPI.DATA.get(player);
		CompoundTag tag = stack.getTag();
		Map<IPlayerAttribute, Double> attributeMap = new HashMap<IPlayerAttribute, Double>();
		Map<IPlayerAttribute, Byte> uuidMap = new HashMap<IPlayerAttribute, Byte>();
		Util.readAttributeMap(attributeMap, tag);
		Util.appendBytes(uuidMap, tag, player);
		
		for(Map.Entry<IPlayerAttribute, Double> entry : attributeMap.entrySet()) {
			IPlayerAttribute attribute = entry.getKey();
			double value = entry.getValue();
			byte id = uuidMap.getOrDefault(attribute, (byte)0);
			EntityAttributeModifier modifier = new EntityAttributeModifier(Modifiers.get(id), attribute.registryKey() + ":modifier_" + id, value, EntityAttributeModifier.Operation.ADDITION);
			
			data.applyAttributeModifier(attribute, modifier);
		}
	}
	
	@Override
	public void onUnequip(PlayerEntity player, ItemStack stack) {
		if(player.world.isClient) return;
		if(!stack.hasTag()) return;
		
		AttributeData data = ExAPI.DATA.get(player);
		CompoundTag tag = stack.getTag();
		Map<IPlayerAttribute, Double> attributeMap = new HashMap<IPlayerAttribute, Double>();
		Map<IPlayerAttribute, Byte> uuidMap = new HashMap<IPlayerAttribute, Byte>();
		Util.readAttributeMap(attributeMap, tag);
		Util.removeBytes(uuidMap, tag, player);
		
		for(Map.Entry<IPlayerAttribute, Double> entry : attributeMap.entrySet()) {
			IPlayerAttribute attribute = entry.getKey();
			double value = entry.getValue();
			byte id = uuidMap.getOrDefault(attribute, (byte)0);
			EntityAttributeModifier modifier = new EntityAttributeModifier(Modifiers.get(id), attribute.registryKey() + ":modifier_" + id, value, EntityAttributeModifier.Operation.ADDITION);
			
			data.removeAttributeModifier(attribute, modifier);
		}
	}
}
