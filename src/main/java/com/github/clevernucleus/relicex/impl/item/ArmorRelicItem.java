package com.github.clevernucleus.relicex.impl.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.github.clevernucleus.relicex.impl.relic.EntityAttributeCollection;
import com.github.clevernucleus.relicex.impl.relic.RelicType;
import com.github.clevernucleus.relicex.impl.relic.SlotKey;
import com.github.clevernucleus.relicex.util.Rareness;
import com.github.clevernucleus.relicex.util.Util;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public final class ArmorRelicItem extends ArmorItem {
	private static final DispenserBehavior RELIC_DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
		
		@Override
		protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
			return ArmorRelicItem.dispenseRelic(pointer, stack) ? stack : super.dispenseSilently(pointer, stack);
		}
	};
	
	private static boolean dispenseRelic(BlockPointer pointer, ItemStack armor) {
		BlockPos blockPos = pointer.getPos().offset((Direction)pointer.getBlockState().get(DispenserBlock.FACING));
		List<LivingEntity> list = pointer.getWorld().getEntitiesByClass(LivingEntity.class, new Box(blockPos), EntityPredicates.EXCEPT_SPECTATOR.and(new EntityPredicates.Equipable(armor)));
		
		if(list.isEmpty()) {
			return false;
		} else {
			LivingEntity livingEntity = (LivingEntity)list.get(0);
			
			EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(armor);
			ItemStack itemStack = armor.split(1);
			
			if(!Util.checkHasAttributes(livingEntity, itemStack, equipmentSlot)) return false;
			
			livingEntity.equipStack(equipmentSlot, itemStack);
			
			if(livingEntity instanceof MobEntity) {
				((MobEntity)livingEntity).setEquipmentDropChance(equipmentSlot, 2.0F);
				((MobEntity)livingEntity).setPersistent();
			}
			
			return true;
		}
	}
	
	public ArmorRelicItem(RelicType relicType) {
		super(ArmorMaterials.CHAIN, relicType == RelicType.HEAD ? EquipmentSlot.HEAD : EquipmentSlot.CHEST, (new Item.Settings()).maxCount(1).group(ItemGroup.COMBAT));
		
		DispenserBlock.registerBehavior(this, RELIC_DISPENSER_BEHAVIOR);
	}
	
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create();
		EntityAttributeCollection collection = new EntityAttributeCollection();
		NbtCompound tag = stack.getOrCreateNbt();
		collection.readFromNbt(tag);
		collection.get(this.slot == EquipmentSlot.HEAD ? SlotKey.HEAD : SlotKey.BODY, modifiers);
		
		return slot == this.slot ? modifiers : ImmutableMultimap.of();
	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		NbtCompound tag = stack.getNbt();
		
		if(tag == null) return;
		
		Rareness rareness = Rareness.fromKey(tag.getString("Rareness"));
		tooltip.add(rareness.formatted());
	}
	
	@Override
	public int getEnchantability() {
		return ArmorMaterials.GOLD.getEnchantability();
	}
	
	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return false;
	}
	
	@Override
	public int getProtection() {
		return 0;
	}
	
	@Override
	public float getToughness() {
		return 0.0F;
	}
	
	@Override
	@Nullable
	public SoundEvent getEquipSound() {
		return this.getMaterial().getEquipSound();
	}
}
