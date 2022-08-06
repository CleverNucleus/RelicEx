package com.github.clevernucleus.relicex;

import java.util.ArrayList;
import java.util.List;

import com.github.clevernucleus.dataattributes.api.event.AttributesReloadedEvent;
import com.github.clevernucleus.relicex.config.RelicExConfig;
import com.github.clevernucleus.relicex.impl.RarityManager;
import com.github.clevernucleus.relicex.impl.RelicType;
import com.github.clevernucleus.relicex.item.ArmorRelicItem;
import com.github.clevernucleus.relicex.item.DragonStoneItem;
import com.github.clevernucleus.relicex.item.HealthPotionItem;
import com.github.clevernucleus.relicex.item.OrbOfRegretItem;
import com.github.clevernucleus.relicex.item.RelicItem;
import com.github.clevernucleus.relicex.item.RelicShardItem;
import com.github.clevernucleus.relicex.item.TomeItem;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.item.Item;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RelicEx implements ModInitializer {
	public static final String MODID = "relicex";
	public static final String WEIGHT_PROPERTY = "weight";
	public static final RarityManager RARITY_MANAGER = new RarityManager();
	public static final List<Item> RELICS = new ArrayList<>();
	public static final List<Item> POTIONS = new ArrayList<>();
	public static final SoundEvent LEVEL_REFUND_SOUND = new SoundEvent(new Identifier(MODID, "level_refund"));
	public static final SoundEvent POTION_USE_SOUND = new SoundEvent(new Identifier(MODID, "potion_use"));
	
	public static final Item SMALL_HEALTH_POTION = registerPotion("small_health_potion", new HealthPotionItem(4.0F));
	public static final Item MEDIUM_HEALTH_POTION = registerPotion("medium_health_potion", new HealthPotionItem(6.0F));
	public static final Item LARGE_HEALTH_POTION = registerPotion("large_health_potion", new HealthPotionItem(8.0F));
	public static final Item TOME = register("tome", new TomeItem());
	public static final Item DRAGON_STONE = register("dragon_stone", new DragonStoneItem());
	public static final Item LESSER_ORB_OF_REGRET = register("lesser_orb_of_regret", new OrbOfRegretItem(false));
	public static final Item GREATER_ORB_OF_REGRET = register("greater_orb_of_regret", new OrbOfRegretItem(true));
	public static final Item AMULET_RELIC = registerRelic("amulet_relic", new RelicItem(RelicType.AMULET));
	public static final Item RING_RELIC = registerRelic("ring_relic", new RelicItem(RelicType.RING));
	public static final Item HEAD_RELIC = registerRelic("head_relic", new ArmorRelicItem(RelicType.HEAD));
	public static final Item CHEST_RELIC = registerRelic("chest_relic", new ArmorRelicItem(RelicType.BODY));
	public static final Item RELIC_SHARD = register("relic_shard", new RelicShardItem());
	
	private static Item registerRelic(final String keyIn, Item itemIn) {
		RELICS.add(itemIn = Registry.register(Registry.ITEM, new Identifier(MODID, keyIn), itemIn));
		return itemIn;
	}
	
	private static Item registerPotion(final String keyIn, Item itemIn) {
		POTIONS.add(itemIn = Registry.register(Registry.ITEM, new Identifier(MODID, keyIn), itemIn));
		return itemIn;
	}
	
	private static Item register(final String keyIn, Item itemIn) {
		return Registry.register(Registry.ITEM, new Identifier(MODID, keyIn), itemIn);
	}
	
	private static void addLoot(ResourceManager resourceManager, LootManager lootManager, Identifier id, LootTable.Builder tableBuilder, LootTableSource source) {
		if(config().dragonDropsStone && id.toString().equals("minecraft:entities/ender_dragon")) {
			LootPool.Builder stone = LootPool.builder().with(ItemEntry.builder(DRAGON_STONE));
			tableBuilder.pool(stone.build());
		}
		
		if(!config().chestsHaveLoot || !id.toString().contains(":chests/")) return;
		LootPool.Builder relics = LootPool.builder().rolls(ConstantLootNumberProvider.create(1)).conditionally(RandomChanceLootCondition.builder(0.01F * (float)config().chestsHaveRelicChance));
		RELICS.forEach(item -> relics.with(ItemEntry.builder(item)));
		
		LootPool.Builder lesserOrb = LootPool.builder().rolls(ConstantLootNumberProvider.create(1)).conditionally(RandomChanceLootCondition.builder(0.01F * (float)config().chestsHaveLesserOrbChance));
		lesserOrb.with(ItemEntry.builder(LESSER_ORB_OF_REGRET));
		
		LootPool.Builder greaterOrb = LootPool.builder().rolls(ConstantLootNumberProvider.create(1)).conditionally(RandomChanceLootCondition.builder(0.01F * (float)config().chestsHaveGreaterOrbChance));
		greaterOrb.with(ItemEntry.builder(GREATER_ORB_OF_REGRET));
		
		LootPool.Builder tome = LootPool.builder().rolls(ConstantLootNumberProvider.create(1)).conditionally(RandomChanceLootCondition.builder(0.01F * (float)config().chestsHaveTomeChance));
		tome.with(ItemEntry.builder(TOME));
		
		tableBuilder.pool(relics.build()).pool(lesserOrb.build()).pool(greaterOrb.build()).pool(tome.build());
	}
	
	@Override
	public void onInitialize() {
		AutoConfig.register(RelicExConfig.class, GsonConfigSerializer::new);
		Registry.register(Registry.SOUND_EVENT, LEVEL_REFUND_SOUND.getId(), LEVEL_REFUND_SOUND);
		Registry.register(Registry.SOUND_EVENT, POTION_USE_SOUND.getId(), POTION_USE_SOUND);
		AttributesReloadedEvent.EVENT.register(RARITY_MANAGER::onPropertiesLoaded);
		LootTableEvents.MODIFY.register(RelicEx::addLoot);
	}
	
	public static RelicExConfig config() {
		return AutoConfig.getConfigHolder(RelicExConfig.class).get();
	}
}
