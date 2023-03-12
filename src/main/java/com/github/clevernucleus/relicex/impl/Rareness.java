package com.github.clevernucleus.relicex.impl;

import java.util.Map;

import com.github.clevernucleus.dataattributes.api.util.Maths;
import com.github.clevernucleus.relicex.RelicEx;
import com.github.clevernucleus.relicex.RelicExClient;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

public enum Rareness {
	COMMON("common", 1.0F, 0.0F, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, Formatting.WHITE),
	UNCOMMON("uncommon", 0.5F, 0.1F, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, Formatting.DARK_GRAY),
	RARE("rare", 0.4F, 0.2F, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, Formatting.DARK_GREEN),
	EPIC("epic", 0.31F, 0.3F, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, Formatting.DARK_AQUA),
	MYTHICAL("mythical", 0.22F, 0.4F, SoundEvents.ITEM_ARMOR_EQUIP_IRON, Formatting.DARK_RED),
	LEGENDARY("legendary", 0.15F, 0.5F, SoundEvents.ITEM_ARMOR_EQUIP_IRON, Formatting.DARK_PURPLE),
	IMMORTAL("immortal", 0.1F, 0.6F, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, Formatting.GOLD);
	
	private static final Map<String, Rareness> VALUES = Maths.enumLookupMap(Rareness.values(), Rareness::toString);
	/** Colour of the tooltip text */
	private final Formatting formatting;
	/** The equip sound for the rareness */
	private final SoundEvent sound;
	/** The enum key - for mapping and quick call/get */
	private final String key;
	/** Threshold value. If a value is between two weights it is the less rare of the two */
	private final float weight;
	/** Determines what texture should be used */
	private final float predicate;
	
	private Rareness(final String key, final float weight, final float predicate, final SoundEvent sound, final Formatting formatting) {
		this.key = key;
		this.weight = weight;
		this.predicate = predicate;
		this.sound = sound;
		this.formatting = formatting;
	}
	
	public static Rareness fromKey(final String keyIn) {
		return VALUES.getOrDefault(keyIn, COMMON);
	}
	
	// Is there a better (cleaner/more efficient) way of doing this?? It's just threshold analysis
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
	
	public SoundEvent equipSound() {
		return this.sound;
	}
	
	public Text formatted() {
		return Text.translatable("rareness." + RelicEx.MODID + "." + this.key).formatted(RelicExClient.getColor(this, this.formatting));
	}
	
	@Override
	public String toString() {
		return this.key;
	}
}
