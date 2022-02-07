package com.github.clevernucleus.relicex;

import java.util.Map;

import com.github.clevernucleus.relicex.item.HealthPotionItem;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * TODO:
 *  - Reimplement everything in previous version
 *  - Add method to recycle relics
 *  - default drops should be rarer - maybe 7%.
 * 
 * 
 * @author CleverNucleus
 *
 */
public class RelicEx implements ModInitializer {
	public static final String MODID = "relicex";
	public static final String WEIGHT_PROPERTY = "weight";
	public static final Map<String, Item> ITEMS = new Object2ObjectArrayMap<String, Item>();
	public static final SoundEvent LEVEL_REFUND_SOUND = new SoundEvent(new Identifier(MODID, "level_refund"));
	public static final SoundEvent POTION_USE_SOUND = new SoundEvent(new Identifier(MODID, "potion_use"));
	
	public static final Item SMALL_HEALTH_POTION = register("small_health_potion", new HealthPotionItem(4.0F));
	public static final Item MEDIUM_HEALTH_POTION = register("medium_health_potion", new HealthPotionItem(6.0F));
	public static final Item LARGE_HEALTH_POTION = register("large_health_potion", new HealthPotionItem(8.0F));
	
	private static Item register(final String keyIn, Item itemIn) {
		return ITEMS.computeIfAbsent(keyIn, key -> Registry.register(Registry.ITEM, new Identifier(MODID, key), itemIn));
	}
	
	@Override
	public void onInitialize() {
		Registry.register(Registry.SOUND_EVENT, LEVEL_REFUND_SOUND.getId(), LEVEL_REFUND_SOUND);
		Registry.register(Registry.SOUND_EVENT, POTION_USE_SOUND.getId(), POTION_USE_SOUND);
	}
}
