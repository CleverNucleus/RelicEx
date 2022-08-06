package com.github.clevernucleus.relicex.impl;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.compress.utils.Sets;

import com.github.clevernucleus.dataattributes.api.util.Maths;
import com.github.clevernucleus.dataattributes.api.util.RandDistribution;

import net.minecraft.entity.attribute.EntityAttributeModifier;

public final class WeightProperty {
	private static final Collection<Character> CHARS = Sets.newHashSet('r', 'a', 'x', 'y', 'z', 'u', 'v', 'w');
	private final Map<EntityAttributeModifier.Operation, Float[]> values = new EnumMap<>(EntityAttributeModifier.Operation.class);
	private float rarity;
	
	public WeightProperty(final String[] stringsIn) {
		Float[] addition = new Float[4];
		Float[] multiplyTotal = new Float[4];
		
		for(String string : stringsIn) {
			char prefix = string.charAt(0);
			float value = parse(string, prefix);
			
			switch(prefix) {
				case 'r' : this.rarity = value * 0.01F;
				continue;
				case 'a' : {
					addition[0] = value * 0.01F;
					multiplyTotal[0] = 1.0F - addition[0];
				}
				continue;
				case 'x' : addition[1] = value;
				continue;
				case 'y' : addition[2] = value;
				continue;
				case 'z' : addition[3] = value;
				continue;
				case 'u' : multiplyTotal[1] = value;
				continue;
				case 'v' : multiplyTotal[2] = value;
				continue;
				case 'w' : multiplyTotal[3] = value;
				break;
			}
		}
		
		this.values.put(EntityAttributeModifier.Operation.ADDITION, addition);
		this.values.put(EntityAttributeModifier.Operation.MULTIPLY_TOTAL, multiplyTotal);
	}
	
	private static float parse(final String stringIn, final char prefixIn) {
		if(!CHARS.contains(prefixIn)) return 0.0F;
		final String string = stringIn.replace(String.valueOf(prefixIn), "");
		return Maths.parse(string);
	}
	
	public float rarity() {
		return this.rarity;
	}
	
	public float processValue(final Processor processor) {
		RandDistribution<EntityAttributeModifier.Operation> distributor = new RandDistribution<EntityAttributeModifier.Operation>(EntityAttributeModifier.Operation.ADDITION);
		this.values.forEach((key, value) -> distributor.add(key, value[0]));
		EntityAttributeModifier.Operation operation = distributor.getDistributedRandom();
		Float[] values = this.values.get(operation);
		return processor.consume(operation, values[0], values[1], values[2], values[3]);
	}
	
	@FunctionalInterface
	public interface Processor {
		float consume(final EntityAttributeModifier.Operation operation, final float weight, final double minRoll, final double maxRoll, final double increment);
	}
}
