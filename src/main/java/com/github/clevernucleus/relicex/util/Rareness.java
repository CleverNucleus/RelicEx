package com.github.clevernucleus.relicex.util;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.clevernucleus.relicex.RelicEx;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

public enum Rareness {
	COMMON("common", 1.0F, 0.0F, Formatting.WHITE),
	UNCOMMON("uncommon", 0.6F, 0.1F, Formatting.DARK_GRAY),
	RARE("rare", 0.5F, 0.2F, Formatting.DARK_GREEN),
	EPIC("epic", 0.4F, 0.3F, Formatting.DARK_AQUA),
	MYTHICAL("mythical", 0.2F, 0.4F, Formatting.DARK_RED),
	LEGENDARY("legendary", 0.1F, 0.5F, Formatting.DARK_PURPLE),
	IMMORTAL("immortal", 0.05F, 0.6F, Formatting.GOLD);
	
	private static final Map<String, Rareness> VALUES = Arrays.stream(Rareness.values()).collect(Collectors.toMap(Rareness::toString, type -> type));
	private Formatting colour;
	private String name;
	private float weight, property;
	
	private Rareness(final String name, final float weight, final float property, final Formatting colour) {
		this.name = name;
		this.weight = weight;
		this.property = property;
		this.colour = colour;
	}
	
	public static Rareness from(final float valueIn) {
		final float value = MathHelper.clamp(valueIn, 0.0F, 1.0F);
		
		if(value == 0.0F || value == 1.0F) return Rareness.COMMON;
		if(value < Rareness.IMMORTAL.weight()) return Rareness.IMMORTAL;
		if(value < Rareness.LEGENDARY.weight()) return Rareness.LEGENDARY;
		if(value < Rareness.MYTHICAL.weight()) return Rareness.MYTHICAL;
		if(value < Rareness.EPIC.weight()) return Rareness.EPIC;
		if(value < Rareness.RARE.weight()) return Rareness.RARE;
		if(value < Rareness.UNCOMMON.weight()) return Rareness.UNCOMMON;
		return Rareness.COMMON;
	}
	
	public static Rareness read(CompoundTag tag) {
		if(tag == null) return Rareness.COMMON;
		
		return VALUES.getOrDefault(tag.getString("Rareness"), Rareness.COMMON);
	}
	
	public void write(CompoundTag tag) {
		if(tag == null) return;
		
		tag.putString("Rareness", this.name);
	}
	
	public float weight() {
		return this.weight;
	}
	
	public float property() {
		return this.property;
	}
	
	public Text text() {
		TranslatableText text = new TranslatableText("rareness." + RelicEx.MODID + "." + this.name);
		return text.formatted(this.colour);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
