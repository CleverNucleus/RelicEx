package com.github.clevernucleus.relicex.util;

import com.github.clevernucleus.relicex.RelicEx;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * A custom rarity enumeration.
 */
public enum Rareness {
	COMMON("common", 70, 0.0F, TextFormatting.WHITE),
	UNCOMMON("uncommon", 50, 0.1F, TextFormatting.DARK_GRAY),
	RARE("rare", 30, 0.2F, TextFormatting.DARK_GREEN),
	EPIC("epic", 20, 0.3F, TextFormatting.DARK_AQUA),
	MYTHICAL("mythical", 10, 0.4F, TextFormatting.DARK_RED),
	LEGENDARY("legendary", 5, 0.5F, TextFormatting.DARK_PURPLE),
	IMMORTAL("immortal", 0, 0.6F, TextFormatting.GOLD);
	
	private static final Rareness[] VALUES = values();
	private TextFormatting colour;
	private String name;
	private int weight;
	private float property;
	
	private Rareness(final String par0, final int par1, final float par2, final TextFormatting par3) {
		this.name = par0;
		this.weight = par1;
		this.property = par2;
		this.colour = par3;
	}
	
	/**
	 * @param par0 Input name.
	 * @return The Rareness object that matches the input; otherwise {@link Rareness#COMMON}.
	 */
	public static Rareness fromName(final String par0) {
		for(Rareness var : VALUES) {
			if(par0.equals(var.toString())) return var;
		}
		
		return COMMON;
	}
	
	/**
	 * @param par0 input percentage (0 - 1).
	 * @return Uses boundaries to get the correct rareness.
	 */
	public static Rareness range(final float par0) {
		for(int var = 0; var < VALUES.length; var++) {
			int var1 = (var - 1) % VALUES.length;
			float var2 = (float)VALUES[var].getWeight() / 100F;
			float var3 = var1 < 0 ? 1.0F : (float)VALUES[var1].getWeight() / 100F;
			
			if(par0 > var2 && par0 <= var3) return VALUES[var];
		}
		
		return COMMON;
	}
	
	/**
	 * @param par0 Input tag.
	 * @return Reads the Rareness from the input tag.
	 */
	public static Rareness read(CompoundNBT par0) {
		if(par0 == null) return COMMON;
		if(!par0.contains("Rareness")) return COMMON;
		
		return fromName(par0.getString("Rareness"));
	}
	
	/**
	 * @return A tag with this rareness.
	 */
	public CompoundNBT write() {
		CompoundNBT var0 = new CompoundNBT();
		
		var0.putString("Rareness", this.name);
		
		return var0;
	}
	
	/**
	 * @return The weighting.
	 */
	public int getWeight() {
		return this.weight;
	}
	
	/**
	 * @return The property needed for textures.
	 */
	public float getProperty() {
		return this.property;
	}
	
	/**
	 * @return The text colour.
	 */
	public TextFormatting getColour() {
		return this.colour;
	}
	
	/**
	 * @return The display text for this rareness.
	 */
	public ITextComponent getDisplayText() {
		return new StringTextComponent(this.colour + (new TranslationTextComponent(RelicEx.MODID + ".rareness." + this.name)).getString());
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
