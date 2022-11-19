package com.github.clevernucleus.relicex.item;

import java.util.List;
import java.util.UUID;

import com.github.clevernucleus.dataattributes.api.item.ItemHelper;
import com.github.clevernucleus.relicex.impl.EntityAttributeCollection;
import com.github.clevernucleus.relicex.impl.Rareness;
import com.github.clevernucleus.relicex.impl.RelicType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class RelicItem extends TrinketItem implements ItemHelper {
	private final RelicType type;
	
	public RelicItem(final RelicType type) {
		super((new Item.Settings()).maxCount(1).group(ItemGroup.COMBAT));
		this.type = type;
	}
	
	public void amendTooltip(final List<Text> tooltip) {
		this.type.tooltip().accept(tooltip);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		NbtCompound tag = stack.getNbt();
		
		if(tag == null || !tag.contains(EntityAttributeCollection.KEY_RARENESS, NbtType.STRING)) return;
		Rareness rareness = Rareness.fromKey(tag.getString(EntityAttributeCollection.KEY_RARENESS));
		tooltip.add(rareness.formatted());
	}
	
	@Override
	public void onStackCreated(ItemStack itemStack, int count) {
		NbtCompound tag = itemStack.getOrCreateNbt();
		EntityAttributeCollection collection = new EntityAttributeCollection();
		collection.writeToNbt(tag);
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		SlotType slotType = slot.inventory().getSlotType();
		String key = slotType.getGroup() + "/" + slotType.getName();
		NbtCompound tag = stack.getOrCreateNbt();
		var modifiers = super.getModifiers(stack, slot, entity, uuid);
		EntityAttributeCollection.readFromNbt(tag, key, modifiers, ArrayListMultimap.create());
		
		return modifiers;
	}
	
	@Override
	public SoundEvent getEquipSound(ItemStack itemStack) {
		return super.getEquipSound();
	}
}
