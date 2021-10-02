package com.github.clevernucleus.relicex.util;

import java.util.Collection;

import com.google.common.collect.Sets;

import net.minecraft.entity.attribute.EntityAttributeModifier;

public final class WeightProperty {
	private static final Collection<Character> CHARS = Sets.newHashSet('r', 'a', 'x', 'y', 'z', 'u', 'v', 'w');
	private float rarity;
	private float addition;
	private float additionMinRoll;
	private float additionMaxRoll;
	private float additionIncrement;
	private float multiplyTotal;
	private float multiplyTotalMinRoll;
	private float multiplyTotalMaxRoll;
	private float multiplyTotalIncrement;
	
	public WeightProperty(final String[] stringsIn) {
		for(String string : stringsIn) {
			char prefix = string.charAt(0);
			float value = parse(string, prefix);
			
			switch(prefix) {
				case 'r' : this.rarity = value * 0.01F;
				continue;
				case 'a' : {
					this.addition = value * 0.01F;
					this.multiplyTotal = 1.0F - (value * 0.01F);
				}
				continue;
				case 'x' : this.additionMinRoll = value;
				continue;
				case 'y' : this.additionMaxRoll = value;
				continue;
				case 'z' : this.additionIncrement = value;
				continue;
				case 'u' : this.multiplyTotalMinRoll = value;
				continue;
				case 'v' : this.multiplyTotalMaxRoll = value;
				continue;
				case 'w' : this.multiplyTotalIncrement = value;
				break;
			}
		}
	}
	
	private static float parse(final String stringIn, final char prefixIn) {
		if(!CHARS.contains(prefixIn)) return 0.0F;
		
		final String string = stringIn.replace(String.valueOf(prefixIn), "");
		final float value;
		
		try {
			value = Float.parseFloat(string);
		} catch(NumberFormatException e) {
			return 0.0F;
		}
		
		return value;
	}
	
	public float rarity() {
		return this.rarity;
	}
	
	public void processValues(Processor processor) {
		RandDistribution<EntityAttributeModifier.Operation> distributor = new RandDistribution<EntityAttributeModifier.Operation>(EntityAttributeModifier.Operation.ADDITION);
		distributor.add(EntityAttributeModifier.Operation.ADDITION, this.addition);
		distributor.add(EntityAttributeModifier.Operation.MULTIPLY_TOTAL, this.multiplyTotal);
		
		EntityAttributeModifier.Operation operation = distributor.getDistributedRandom();
		
		switch(operation) {
			case ADDITION : 
			default : {
				processor.consume(operation, this.addition, this.additionMinRoll, this.additionMaxRoll, this.additionIncrement);
				break;
			}
			case MULTIPLY_BASE : 
			case MULTIPLY_TOTAL : {
				processor.consume(operation, this.multiplyTotal, this.multiplyTotalMinRoll, this.multiplyTotalMaxRoll, this.multiplyTotalIncrement);
				break;
			}
		}
	}
	
	@FunctionalInterface
	public interface Processor {
		void consume(final EntityAttributeModifier.Operation operation, final float weight, final double minRoll, final double maxRoll, final double incr);
	}
}
