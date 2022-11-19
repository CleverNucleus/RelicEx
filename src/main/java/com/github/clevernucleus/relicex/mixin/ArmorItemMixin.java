package com.github.clevernucleus.relicex.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.github.clevernucleus.dataattributes.api.item.ItemHelper;
import com.github.clevernucleus.relicex.impl.EntityAttributeCollection;
import com.github.clevernucleus.relicex.impl.Rareness;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;

@Mixin(ArmorItem.class)
abstract class ArmorItemMixin extends Item implements ItemHelper {
	
	@Shadow
	@Final
	protected EquipmentSlot slot;
	
	@Shadow
	@Final
	private Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
	
	private ArmorItemMixin(Settings settings) { super(settings); }
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		NbtCompound tag = stack.getNbt();
		
		if(tag == null || !tag.contains(EntityAttributeCollection.KEY_RARENESS, NbtType.STRING)) return;
		Rareness rareness = Rareness.fromKey(tag.getString(EntityAttributeCollection.KEY_RARENESS));
		tooltip.add(rareness.formatted());
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
		NbtCompound tag = stack.getOrCreateNbt();
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = ArrayListMultimap.create();
		EntityAttributeCollection.readFromNbt(tag, this.slot.getName(), modifiers, this.attributeModifiers);
		
		return slot == this.slot ? (modifiers.isEmpty() ? this.attributeModifiers : modifiers) : super.getAttributeModifiers(stack, slot);
	}
	
	@Override
	public int getProtection(ItemStack itemStack) {
		return (int)EntityAttributeCollection.getValueIfArmor(itemStack.getOrCreateNbt(), EntityAttributes.GENERIC_ARMOR, ((ArmorItem)(Object)this).getProtection());
	}
	
	@Override
	public float getToughness(ItemStack itemStack) {
		return (int)EntityAttributeCollection.getValueIfArmor(itemStack.getOrCreateNbt(), EntityAttributes.GENERIC_ARMOR_TOUGHNESS, ((ArmorItem)(Object)this).getToughness());
	}
}
