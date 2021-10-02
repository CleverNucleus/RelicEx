package com.github.clevernucleus.relicex.impl.item;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.github.clevernucleus.relicex.impl.relic.EntityAttributeCollection;
import com.github.clevernucleus.relicex.impl.relic.RelicType;
import com.github.clevernucleus.relicex.impl.relic.SlotKey;
import com.github.clevernucleus.relicex.util.Rareness;
import com.google.common.collect.Multimap;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public final class RelicItem extends TrinketItem {
	private final Consumer<List<Text>> tooltipActiion;
	
	public RelicItem(final RelicType type) {
		super((new Item.Settings()).maxCount(1).group(ItemGroup.COMBAT));
		
		switch(type) {
			case AMULET : {
				this.tooltipActiion = tooltip -> {
					tooltip.remove(new TranslatableText("trinkets.tooltip.slots.single", new TranslatableText("trinkets.slot.chest.necklace").formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
		    		appendTooltip(tooltip, new TranslatableText("trinkets.tooltip.attributes.all").formatted(Formatting.GRAY));
				};
				break;
			}
			case RING : {
				this.tooltipActiion = tooltip -> {
					tooltip.remove(new TranslatableText("trinkets.tooltip.attributes.single", new TranslatableText("trinkets.slot.offhand.ring").formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
		            tooltip.remove(new TranslatableText("trinkets.tooltip.attributes.single", new TranslatableText("trinkets.slot.hand.ring").formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
		            appendTooltip(tooltip, new TranslatableText("trinkets.tooltip.slots.single", new TranslatableText("trinkets.slot.offhand.ring").formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
		            
		            List<Text> distinct = tooltip.stream().distinct().collect(Collectors.toList());
		    		tooltip.clear();
		    		tooltip.addAll(distinct);
				};
				break;
			}
			default : this.tooltipActiion = tooltip -> {};
		}
	}
	
	private static void appendTooltip(final List<Text> tooltip, final Text text) {
		int index = Math.max(0, tooltip.indexOf(text));
		
    	tooltip.set(index, LiteralText.EMPTY);
    	tooltip.add(index + 1, (new TranslatableText("tooltip.relicex.worn")).formatted(Formatting.GRAY));
	}
	
	public void amendTooltip(final List<Text> tooltip) {
		this.tooltipActiion.accept(tooltip);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		NbtCompound tag = stack.getNbt();
		
		if(tag == null) return;
		
		Rareness rareness = Rareness.fromKey(tag.getString("Rareness"));
		tooltip.add(rareness.formatted());
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		SlotType slotType = slot.inventory().getSlotType();
		String group = slotType.getGroup();
		SlotKey slotKey = SlotKey.from(group);
		EntityAttributeCollection collection = new EntityAttributeCollection();
		NbtCompound tag = stack.getOrCreateNbt();
		var modifiers = super.getModifiers(stack, slot, entity, uuid);
		
		collection.readFromNbt(tag);
		collection.get(slotKey, modifiers);
		
		return modifiers;
	}
}
