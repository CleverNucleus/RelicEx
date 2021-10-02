package com.github.clevernucleus.relicex;

import java.util.Collection;
import java.util.HashSet;

import com.github.clevernucleus.dataattributes.api.event.ServerSyncedEvent;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.relicex.impl.RarityManager;
import com.github.clevernucleus.relicex.impl.item.RelicItem;
import com.github.clevernucleus.relicex.impl.relic.RelicType;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class RelicEx implements ModInitializer {
	public static final String MODID = "relicex";
	public static final String WEIGHT_PROPERTY = "weight";
	public static final RarityManager RARITY_MANAGER = new RarityManager();
	public static final Collection<Item> ITEMS = new HashSet<Item>();
	
	public static final SoundEvent LEVEL_REFUND_SOUND = new SoundEvent(new Identifier(ExAPI.MODID, "level_refund"));
	public static final SoundEvent POTION_USE_SOUND = new SoundEvent(new Identifier(ExAPI.MODID, "potion_use"));
	
	public static final Item AMULET_RELIC = register("amulet_relic", new RelicItem(RelicType.AMULET));
	public static final Item RING_RELIC = register("ring_relic", new RelicItem(RelicType.RING));
	
	private static Item register(final String keyIn, Item itemIn) {
		itemIn = Registry.register(Registry.ITEM, new Identifier(MODID, keyIn), itemIn);
		ITEMS.add(itemIn);
		
		return itemIn;
	}
	
	@Override
	public void onInitialize() {
		Registry.register(Registry.SOUND_EVENT, LEVEL_REFUND_SOUND.getId(), LEVEL_REFUND_SOUND);
		Registry.register(Registry.SOUND_EVENT, POTION_USE_SOUND.getId(), POTION_USE_SOUND);
		
		ServerSyncedEvent.EVENT.register(server -> RARITY_MANAGER.onRarityLoaded());
	}
}