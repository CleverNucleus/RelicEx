package com.github.clevernucleus.relicex.init;

import com.github.clevernucleus.relicex.RelicEx;
import com.github.clevernucleus.relicex.init.capability.Data;
import com.github.clevernucleus.relicex.init.capability.IData;
import com.github.clevernucleus.relicex.init.item.HealthPotionItem;
import com.github.clevernucleus.relicex.init.item.RelicItem;
import com.github.clevernucleus.relicex.util.CommonConfig;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
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
	
	/** Capability access. */
	@CapabilityInject(IData.class)
	public static final Capability<IData> DATA = null;
	
	/** For gold coloured tooltips. */
	public static final Rarity IMMORTAL = Rarity.create("immortal", TextFormatting.GOLD);
	/** Item register. */
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RelicEx.MODID);
	/** Necklace loot item. */
	public static final RegistryObject<Item> AMULET_RELIC = ITEMS.register("amulet_relic", () -> new RelicItem());
	/** Body loot item. */
	public static final RegistryObject<Item> BODY_RELIC = ITEMS.register("body_relic", () -> new RelicItem());
	/** Head loot item. */
	public static final RegistryObject<Item> HEAD_RELIC = ITEMS.register("head_relic", () -> new RelicItem());
	/** Ring loot item. */
	public static final RegistryObject<Item> RING_RELIC = ITEMS.register("ring_relic", () -> new RelicItem());
	/** Small Health Potion */
	public static final RegistryObject<Item> SMALL_HEALTH_POTION = ITEMS.register("small_health_potion", () -> new HealthPotionItem(4F, CommonConfig.COMMON.weightSmallHealthPotion.get().floatValue()));
	/** Medium Health Potion */
	public static final RegistryObject<Item> MEDIUM_HEALTH_POTION = ITEMS.register("medium_health_potion", () -> new HealthPotionItem(6F, CommonConfig.COMMON.weightMediumHealthPotion.get().floatValue()));
	/** Large Health Potion */
	public static final RegistryObject<Item> LARGE_HEALTH_POTION = ITEMS.register("large_health_potion", () -> new HealthPotionItem(8F, CommonConfig.COMMON.weightLargeHealthPotion.get().floatValue()));
	
	/**
	 * Mod initialisation event.
	 * @param par0
	 */
	@SubscribeEvent
	public static void commonSetup(final net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent par0) {
		CapabilityManager.INSTANCE.register(IData.class, new Capability.IStorage<IData>() {
			
			@Override
			public INBT writeNBT(Capability<IData> par0, IData par1, Direction par2) {
				return par1.write();
			}
			
			@Override
			public void readNBT(Capability<IData> par0, IData par1, Direction par2, INBT par3) {
				par1.read((CompoundNBT)par3);
			}
		}, Data::new);
	}
	
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
