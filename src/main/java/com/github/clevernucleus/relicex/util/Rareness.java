package com.github.clevernucleus.relicex.util;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.clevernucleus.relicex.RelicEx;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

public enum Rareness {
	COMMON("common", 1.0F, 0.0F, Formatting.WHITE),
	UNCOMMON("uncommon", 0.5F, 0.1F, Formatting.DARK_GRAY),
	RARE("rare", 0.4F, 0.2F, Formatting.DARK_GREEN),
	EPIC("epic", 0.31F, 0.3F, Formatting.DARK_AQUA),
	MYTHICAL("mythical", 0.22F, 0.4F, Formatting.DARK_RED),
	LEGENDARY("legendary", 0.15F, 0.5F, Formatting.DARK_PURPLE),
	IMMORTAL("immortal", 0.1F, 0.6F, Formatting.GOLD);
	
	private static final Map<String, Rareness> VALUES = Arrays.stream(Rareness.values()).collect(Collectors.toMap(Rareness::toString, type -> type));
	private final Formatting formatting;
	private final String key;
	private final float weight, predicate;
	
	private Rareness(final String key, final float weight, final float predicate, final Formatting formatting) {
		this.key = key;
		this.weight = weight;
		this.predicate = predicate;
		this.formatting = formatting;
	}
	
	public static Rareness fromKey(final String keyIn) {
		return VALUES.getOrDefault(keyIn, (Rareness)null);
	}
	
	public static Rareness fromWeight(final float weightIn) {
		float weight = MathHelper.clamp(weightIn, 0.0F, 1.0F);
		
		if(weight >= UNCOMMON.weight()) return COMMON;
		if(weight < UNCOMMON.weight() && weight >= RARE.weight()) return UNCOMMON;
		if(weight < RARE.weight() && weight >= EPIC.weight()) return RARE;
		if(weight < EPIC.weight() && weight >= MYTHICAL.weight()) return EPIC;
		if(weight < MYTHICAL.weight() && weight >= LEGENDARY.weight()) return MYTHICAL;
		if(weight < LEGENDARY.weight() && weight >= IMMORTAL.weight()) return LEGENDARY;
		if(weight < IMMORTAL.weight()) return IMMORTAL;
		
		return COMMON;
	}
	
	public float weight() {
		return this.weight;
	}
	
	public float predicate() {
		return this.predicate;
	}
	
	public Text formatted() {
		return (new TranslatableText("rareness." + RelicEx.MODID + "." + this.key)).formatted(this.formatting);
	}
	
	@Override
	public String toString() {
		return this.key;
	}
}
