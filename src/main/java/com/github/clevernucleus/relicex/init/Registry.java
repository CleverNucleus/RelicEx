package com.github.clevernucleus.relicex.init;

import com.github.clevernucleus.relicex.ExCurios;
import com.github.clevernucleus.relicex.init.item.*;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.SlotTypeMessage;

/**
 * Mod registry. Holds all registry objects added by RelicEx.
 */
@Mod.EventBusSubscriber(modid = RelicEx.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {
	
	/** For gold coloured tooltips. */
	public static final Rarity IMMORTAL = Rarity.create("immortal", TextFormatting.GOLD);
	/** Item register. */
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RelicEx.MODID);
	/** Necklace loot item. */
	public static final RegistryObject<Item> NECKLACE_RELIC = ITEMS.register("necklace_item", () -> new RelicItem());
	/** Body loot item. */
	public static final RegistryObject<Item> BODY_RELIC = ITEMS.register("body_item", () -> new RelicItem());
	/** Head loot item. */
	public static final RegistryObject<Item> HEAD_RELIC = ITEMS.register("head_item", () -> new RelicItem());
	/** Ring loot item. */
	public static final RegistryObject<Item> RING_RELIC = ITEMS.register("ring_item", () -> new RelicItem());
	
	/**
	 * Event registering curios slots
	 * @param par0
	 */
	@SubscribeEvent
	public static void registerCurios(final InterModEnqueueEvent par0) {
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("necklace").build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("body").build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("head").build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("ring").build());
	}
}
