package com.github.clevernucleus.relicex.impl;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public enum RelicType {
	HEAD(EquipmentSlot.HEAD, tooltip -> {}),
	BODY(EquipmentSlot.CHEST, tooltip -> {}),
	AMULET((EquipmentSlot)null, tooltip -> {
		tooltip.remove(Text.translatable("trinkets.tooltip.slots.single", Text.translatable("trinkets.slot.chest.necklace").formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
		appendTooltip(tooltip, Text.translatable("trinkets.tooltip.attributes.all").formatted(Formatting.GRAY));
	}),
	RING((EquipmentSlot)null, tooltip -> {
		tooltip.remove(Text.translatable("trinkets.tooltip.attributes.single", Text.translatable("trinkets.slot.offhand.ring").formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
		tooltip.remove(Text.translatable("trinkets.tooltip.attributes.single", Text.translatable("trinkets.slot.hand.ring").formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
		appendTooltip(tooltip, Text.translatable("trinkets.tooltip.slots.single", Text.translatable("trinkets.slot.offhand.ring").formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
		
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
		tooltip.set(index, Text.empty());
		tooltip.add(index + 1, Text.translatable("tooltip.relicex.worn").formatted(Formatting.GRAY));
	}
	
	public EquipmentSlot slot() {
		return this.slot;
	}
	
	public Consumer<List<Text>> tooltip() {
		return this.tooltip;
	}
}
