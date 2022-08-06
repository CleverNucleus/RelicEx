package com.github.clevernucleus.relicex.impl;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public enum RelicType {
	HEAD(EquipmentSlot.HEAD, tooltip -> {}),
	BODY(EquipmentSlot.CHEST, tooltip -> {}),
	AMULET((EquipmentSlot)null, tooltip -> {
		tooltip.remove(new TranslatableText("trinkets.tooltip.slots.single", new TranslatableText("trinkets.slot.chest.necklace").formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
		appendTooltip(tooltip, new TranslatableText("trinkets.tooltip.attributes.all").formatted(Formatting.GRAY));
	}),
	RING((EquipmentSlot)null, tooltip -> {
		tooltip.remove(new TranslatableText("trinkets.tooltip.attributes.single", new TranslatableText("trinkets.slot.offhand.ring").formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
		tooltip.remove(new TranslatableText("trinkets.tooltip.attributes.single", new TranslatableText("trinkets.slot.hand.ring").formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
		appendTooltip(tooltip, new TranslatableText("trinkets.tooltip.slots.single", new TranslatableText("trinkets.slot.offhand.ring").formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
		
		List<Text> distinct = tooltip.stream().distinct().collect(Collectors.toList());
		tooltip.clear();
		tooltip.addAll(distinct);
	});
	
	private final EquipmentSlot slot;
	private final Consumer<List<Text>> tooltip;
	
	private RelicType(final EquipmentSlot slot, final Consumer<List<Text>> tooltip) {
		this.slot = slot;
		this.tooltip = tooltip;
	}
	
	private static void appendTooltip(final List<Text> tooltip, final Text text) {
		int index = Math.max(0, tooltip.indexOf(text));
		tooltip.set(index, LiteralText.EMPTY);
		tooltip.add(index + 1, (new TranslatableText("tooltip.relicex.worn")).formatted(Formatting.GRAY));
	}
	
	public EquipmentSlot slot() {
		return this.slot;
	}
	
	public Consumer<List<Text>> tooltip() {
		return this.tooltip;
	}
}
