package com.github.clevernucleus.relicex;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.github.clevernucleus.relicex.impl.RelicExConfig;
import com.github.clevernucleus.relicex.impl.UUIDCache;
import com.github.clevernucleus.relicex.item.*;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketSlots;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class RelicEx implements ModInitializer {
	public static final String MODID = "relicex";
	public static final ComponentKey<UUIDCache> CACHE = ComponentRegistry.getOrCreate(new Identifier(MODID, "uuid_cache"), UUIDCache.class);
	public static final Collection<Item> ITEMS = new HashSet<Item>();
	
	private static final Identifier LEVEL_REFUND_KEY = new Identifier(MODID, "level_refund");
	private static final Identifier POTION_USE_KEY = new Identifier(MODID, "potion_use");
	public static final SoundEvent LEVEL_REFUND = new SoundEvent(LEVEL_REFUND_KEY);
	public static final SoundEvent POTION_USE = new SoundEvent(POTION_USE_KEY);
	
	public static final Item HEAD_RELIC = register("head_relic", new RelicItem(SlotGroups.HEAD, "head"));
	public static final Item BODY_RELIC = register("body_relic", new RelicItem(SlotGroups.CHEST, "body"));
	public static final Item AMULET_RELIC = register("amulet_relic", new RelicItem(SlotGroups.CHEST, Slots.NECKLACE));
	public static final Item RING_RELIC = register("ring_relic", new RelicItem(Arrays.asList(SlotGroups.HAND, SlotGroups.OFFHAND), Collections.singleton(Slots.RING)));
	public static final Item SMALL_HEALTH_POTION = register("small_health_potion", new HealthPotionItem(4.0F));
	public static final Item MEDIUM_HEALTH_POTION = register("medium_health_potion", new HealthPotionItem(6.0F));
	public static final Item LARGE_HEALTH_POTION = register("large_health_potion", new HealthPotionItem(8.0F));
	public static final Item GREATER_ORB_OF_REGRET = register("greater_orb_of_regret", new OrbOfRegretItem(true));
	public static final Item LESSER_ORB_OF_REGRET = register("lesser_orb_of_regret", new OrbOfRegretItem(false));
	public static final Item DRAGON_STONE = register("dragon_stone", new DragonStoneItem());
	public static final Item TOME = register("tome", new TomeItem());
	
	private static final List<Identifier> LOOT_TABLES = Arrays.asList(
			new Identifier("chests/village/village_armorer"),
			new Identifier("chests/village/village_temple"),
			new Identifier("chests/village/village_weaponsmith"),
			new Identifier("chests/abandoned_mineshaft"),
			new Identifier("chests/bastion_hoglin_stable"),
			new Identifier("chests/bastion_treasure"),
			new Identifier("chests/buried_treasure"),
			new Identifier("chests/desert_pyramid"),
			new Identifier("chests/end_city_treasure"),
			new Identifier("chests/jungle_temple"),
			new Identifier("chests/nether_bridge"),
			new Identifier("chests/pillager_outpost"),
			new Identifier("chests/ruined_portal"),
			new Identifier("chests/shipwreck_treasure"),
			new Identifier("chests/stronghold_corridor"),
			new Identifier("chests/underwater_ruin_big"),
			new Identifier("chests/woodland_mansion")
	);
	
	private static Item register(final String keyIn, Item itemIn) {
		itemIn = Registry.register(Registry.ITEM, new Identifier(MODID, keyIn), itemIn);
		ITEMS.add(itemIn);
		
		return itemIn;
	}
	
	public static RelicExConfig config() {
		return AutoConfig.getConfigHolder(RelicExConfig.class).get();
	}
	
	@Override
	public void onInitialize() {
		AutoConfig.register(RelicExConfig.class, GsonConfigSerializer::new);
		
		Registry.register(Registry.SOUND_EVENT, LEVEL_REFUND_KEY, LEVEL_REFUND);
		Registry.register(Registry.SOUND_EVENT, POTION_USE_KEY, POTION_USE);
		
		TrinketSlots.addSlot(SlotGroups.HEAD, "head", new Identifier(MODID, "textures/items/empty_trinket_slot_head.png"));
		TrinketSlots.addSlot(SlotGroups.CHEST, "body", new Identifier(MODID, "textures/items/empty_trinket_slot_body.png"));
		TrinketSlots.addSlot(SlotGroups.CHEST, Slots.NECKLACE, new Identifier("trinkets", "textures/item/empty_trinket_slot_necklace.png"));
		TrinketSlots.addSlot(SlotGroups.HAND, Slots.RING, new Identifier("trinkets", "textures/item/empty_trinket_slot_ring.png"));
		TrinketSlots.addSlot(SlotGroups.OFFHAND, Slots.RING, new Identifier("trinkets", "textures/item/empty_trinket_slot_ring.png"));
		
		LootTableLoadingCallback.EVENT.register((manager, loot, id, table, setter) -> {
			if(LOOT_TABLES.contains(id)) {
				LootPoolEntry.Builder<?> builder = LootTableEntry.builder(new Identifier(MODID, "inject")).weight(1);
				table.pool(LootPool.builder().with(builder));
			}
		});
	}
}
