package com.github.clevernucleus.relicex.client;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.api.attribute.PlayerAttributes;
import com.github.clevernucleus.relicex.RelicEx;
import com.github.clevernucleus.relicex.init.item.RelicItem;
import com.github.clevernucleus.relicex.util.Rareness;
import com.github.clevernucleus.relicex.util.Util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Events holder on the FORGE bus for client side hooks.
 */
@Mod.EventBusSubscriber(modid = RelicEx.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {
	private static final Function<Double, String> DECIMAL = var -> (new DecimalFormat("##.##")).format(var);
	private static final Function<Double, String> DEFAULT = var -> " +" + (new DecimalFormat("##.##")).format(var);
	private static final Supplier<Map<IPlayerAttribute, Function<Double, String>>> FORMAT = () -> {
		Map<IPlayerAttribute, Function<Double, String>> var0 = new HashMap<>();
		Function<Double, String> var1 = var -> " +" + DECIMAL.apply(var * 100D) + "%";
		Function<Double, String> var2 = var -> " +" + DECIMAL.apply(var * 10D);
		
		var0.put(PlayerAttributes.HEALTH_REGEN, var -> " +" + DECIMAL.apply(var * 20D) + "/s");
		var0.put(PlayerAttributes.HEALTH_REGEN_AMP, var1);
		var0.put(PlayerAttributes.KNOCKBACK_RESISTANCE, var2);
		var0.put(PlayerAttributes.DAMAGE_REDUCTION, var2);
		var0.put(PlayerAttributes.FIRE_RESISTANCE, var2);
		var0.put(PlayerAttributes.LAVA_RESISTANCE, var2);
		var0.put(PlayerAttributes.EXPLOSION_RESISTANCE, var2);
		var0.put(PlayerAttributes.FALLING_RESISTANCE, var2);
		var0.put(PlayerAttributes.POISON_RESISTANCE, var2);
		var0.put(PlayerAttributes.WITHER_RESISTANCE, var2);
		var0.put(PlayerAttributes.DROWNING_RESISTANCE, var2);
		var0.put(PlayerAttributes.MOVEMENT_SPEED, var -> " +" + DECIMAL.apply(var * 20D));
		var0.put(PlayerAttributes.MELEE_CRIT_DAMAGE, var1);
		var0.put(PlayerAttributes.MELEE_CRIT_CHANCE, var1);
		var0.put(PlayerAttributes.GRAVITY, var -> " -" + DECIMAL.apply(var));
		var0.put(PlayerAttributes.EVASION, var1);
		var0.put(PlayerAttributes.RANGED_CRIT_DAMAGE, var1);
		var0.put(PlayerAttributes.RANGED_CRIT_CHANCE, var1);
		var0.put(PlayerAttributes.LIFESTEAL, var1);
		
		return var0;
	};
	
	private static ITextComponent formatAttribute(final IPlayerAttribute par0, final double par1) {
		String var0 = FORMAT.get().getOrDefault(par0, DEFAULT).apply(par1);
		TranslationTextComponent var1 = new TranslationTextComponent(par0.get().getAttributeName());
		
		return new StringTextComponent(TextFormatting.GRAY + var0 + " " + var1.getString());
	}
	
	/**
	 * Event drawing item tooltips.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onDrawTooltip(final net.minecraftforge.event.entity.player.ItemTooltipEvent par0) {
		ItemStack var0 = par0.getItemStack();
		
		if(var0.isEmpty()) return;
		if(var0.getItem() instanceof RelicItem) {
			if(var0.hasTag() && var0.getTag().contains("Attributes") && var0.getTag().contains("Rareness")) {
				CompoundNBT var2 = var0.getTag();
				Rareness var3 = Rareness.read(var2);
				
				par0.getToolTip().add(new StringTextComponent(""));
				par0.getToolTip().add(var3.getDisplayText());
				
				for(Map.Entry<IPlayerAttribute, Double> var : Util.tagMap(var2).entrySet()) {
					par0.getToolTip().add(formatAttribute(var.getKey(), var.getValue()));
				}
			}
		}
	}
}
