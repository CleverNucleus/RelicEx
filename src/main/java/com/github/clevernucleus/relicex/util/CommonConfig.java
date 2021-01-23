package com.github.clevernucleus.relicex.util;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class CommonConfig {
	
	/** Initialised instance of the forge common config specifications. */
	public static final ForgeConfigSpec COMMON_SPEC;
	
	/** Initialised instance of our common config. */
	public static final Common COMMON;
	
	static {
		final Pair<Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(Common::new);
		
		COMMON_SPEC = common.getRight();
		COMMON = common.getLeft();
	}
	
	/**
	 * The common config file (for the server really, but needs to be available to the client).
	 */
	public static class Common {
		public final DoubleValue dropChance, weightRelic, weightSmallHealthPotion, weightMediumHealthPotion, weightLargeHealthPotion;
		
		public Common(ForgeConfigSpec.Builder par0) {
			par0.push("drops");
			
			this.dropChance = par0.comment("Chance for an IMob to drop a relic or health potion.").defineInRange("dropChance", 0.17F, 0.0F, 1.0F);
			this.weightRelic = par0.comment("Chance for the drop to be a relic item.").defineInRange("weightRelic", 0.25F, 0.0F, 1.0F);
			this.weightSmallHealthPotion = par0.comment("Chance for the drop to be a small health potion.").defineInRange("weightSmallHealthPotion", 0.75F, 0.0F, 1.0F);
			this.weightMediumHealthPotion = par0.comment("Chance for the drop to be a medium health potion.").defineInRange("weightMediumHealthPotion", 0.5F, 0.0F, 1.0F);
			this.weightLargeHealthPotion = par0.comment("Chance for the drop to be a large health potion.").defineInRange("weightLargeHealthPotion", 0.25F, 0.0F, 1.0F);
			
			par0.pop();
		}
	}
}
