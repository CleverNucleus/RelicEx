package com.github.clevernucleus.relicex;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import com.github.clevernucleus.dataattributes.api.event.ServerSyncedEvent;
import com.github.clevernucleus.playerex.api.event.ItemStackEvents;
import com.github.clevernucleus.relicex.impl.RarityManager;
import com.github.clevernucleus.relicex.impl.RelicExConfig;
import com.github.clevernucleus.relicex.impl.item.ArmorRelicItem;
import com.github.clevernucleus.relicex.impl.item.DragonStoneItem;
import com.github.clevernucleus.relicex.impl.item.HealthPotionItem;
import com.github.clevernucleus.relicex.impl.item.OrbOfRegretItem;
import com.github.clevernucleus.relicex.impl.item.RelicItem;
import com.github.clevernucleus.relicex.impl.item.TomeItem;
import com.github.clevernucleus.relicex.impl.relic.RelicType;
import com.github.clevernucleus.relicex.util.Util;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.Item;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * 
 * @author CleverNucleus
 *
 */
public final class RelicEx implements ModInitializer {
	public static final String MODID = "relicex";
	public static final String WEIGHT_PROPERTY = "weight";
	public static final RarityManager RARITY_MANAGER = new RarityManager();
	public static final Collection<Item> ITEMS = new HashSet<Item>();
	public static final SoundEvent LEVEL_REFUND_SOUND = new SoundEvent(new Identifier(MODID, "level_refund"));
	public static final SoundEvent POTION_USE_SOUND = new SoundEvent(new Identifier(MODID, "potion_use"));
	
	public static final Item AMULET_RELIC = register("amulet_relic", new RelicItem(RelicType.AMULET));
	public static final Item RING_RELIC = register("ring_relic", new RelicItem(RelicType.RING));
	public static final Item HEAD_RELIC = register("head_relic", new ArmorRelicItem(RelicType.HEAD));
	public static final Item BODY_RELIC = register("body_relic", new ArmorRelicItem(RelicType.BODY));
	public static final Item SMALL_HEALTH_POTION = register("small_health_potion", new HealthPotionItem(4.0F));
	public static final Item MEDIUM_HEALTH_POTION = register("medium_health_potion", new HealthPotionItem(6.0F));
	public static final Item LARGE_HEALTH_POTION = register("large_health_potion", new HealthPotionItem(8.0F));
	public static final Item GREATER_ORB_OF_REGRET = register("greater_orb_of_regret", new OrbOfRegretItem(true));
	public static final Item LESSER_ORB_OF_REGRET = register("lesser_orb_of_regret", new OrbOfRegretItem(false));
	public static final Item DRAGON_STONE = register("dragon_stone", new DragonStoneItem());
	public static final Item TOME = register("tome", new TomeItem());
	
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
		Registry.register(Registry.SOUND_EVENT, LEVEL_REFUND_SOUND.getId(), LEVEL_REFUND_SOUND);
		Registry.register(Registry.SOUND_EVENT, POTION_USE_SOUND.getId(), POTION_USE_SOUND);
		ServerSyncedEvent.EVENT.register(server -> RARITY_MANAGER.onRarityLoaded());
		ItemStackEvents.ITEMSTACK_CONSTRUCTED.register((item, itemStack, count) -> {
			if(item != null && item instanceof RelicItem || item instanceof ArmorRelicItem) {
				Util.createRelic(itemStack);
			}
		});
		// TODO slated for implementation when PlayerEx fixes modifiers multimap to not be immutable. lol.
		/*
		ItemStackEvents.ITEMSTACK_MODIFIERS.register((itemStack, modifiers, slot) -> {
			Item item = itemStack.getItem();
			
			if(item instanceof ArmorRelicItem) {
				ArmorRelicItem relic = (ArmorRelicItem)item;
				
				modifiers.clear();
				modifiers.putAll(relic.getAttributeModifiers(itemStack, slot));
			}
		});
		*/
		LootTableLoadingCallback.EVENT.register((resourceManager, manager, id, supplier, setter) -> {
			Collection<Identifier> lootTables = manager.getTableIds().stream().filter(i -> i.getPath().startsWith("chests/")).collect(Collectors.toSet());
			
			if(config().chestsHaveLoot) {
				if(lootTables.contains(id)) {
					FabricLootPoolBuilder relics = FabricLootPoolBuilder.builder().rolls(ConstantLootNumberProvider.create(1))
						.withCondition(RandomChanceLootCondition.builder((float)config().chestsDropRelicsChance * 0.01F).build())
						.with(ItemEntry.builder(AMULET_RELIC))
						.with(ItemEntry.builder(RING_RELIC))
						.with(ItemEntry.builder(HEAD_RELIC))
						.with(ItemEntry.builder(BODY_RELIC));
					FabricLootPoolBuilder lesserOrb = FabricLootPoolBuilder.builder().rolls(ConstantLootNumberProvider.create(1))
							.withCondition(RandomChanceLootCondition.builder((float)config().chestsDropLesserOrbChance * 0.01F).build())
							.with(ItemEntry.builder(LESSER_ORB_OF_REGRET));
					FabricLootPoolBuilder greaterOrb = FabricLootPoolBuilder.builder().rolls(ConstantLootNumberProvider.create(1))
							.withCondition(RandomChanceLootCondition.builder((float)config().chestsDropGreaterOrbChance * 0.01F).build())
							.with(ItemEntry.builder(GREATER_ORB_OF_REGRET));
					FabricLootPoolBuilder tome = FabricLootPoolBuilder.builder().rolls(ConstantLootNumberProvider.create(1))
							.withCondition(RandomChanceLootCondition.builder((float)config().chestsDropTome * 0.01F).build())
							.with(ItemEntry.builder(TOME));
					
					supplier.withPool(relics.build()).withPool(lesserOrb.build()).withPool(greaterOrb.build()).withPool(tome.build());
				}
			}
		});
	}
}