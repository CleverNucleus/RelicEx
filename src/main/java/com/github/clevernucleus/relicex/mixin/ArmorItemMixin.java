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

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.world.World;

@Mixin(ArmorItem.class)
abstract class ArmorItemMixin extends Item implements ItemHelper {
	
	@Shadow
	@Final
	protected EquipmentSlot slot;
	
	private ArmorItemMixin(Settings settings) { super(settings); }
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		NbtCompound tag = stack.getNbt();
		
		if(tag == null || !tag.contains(EntityAttributeCollection.KEY_RARENESS, NbtElement.STRING_TYPE)) return;
		Rareness rareness = Rareness.fromKey(tag.getString(EntityAttributeCollection.KEY_RARENESS));
		tooltip.add(rareness.formatted());
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
		NbtCompound tag = stack.getOrCreateNbt();
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = ArrayListMultimap.create();
		Multimap<EntityAttribute, EntityAttributeModifier> fallbacks = super.getAttributeModifiers(stack, slot);
		EntityAttributeCollection.readFromNbt(tag, this.slot.getName(), modifiers, fallbacks);
		
		return slot == this.slot ? (modifiers.isEmpty() ? fallbacks : modifiers) : fallbacks;
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
