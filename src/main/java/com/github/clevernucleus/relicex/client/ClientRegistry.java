package com.github.clevernucleus.relicex.client;

import com.github.clevernucleus.relicex.RelicEx;
import com.github.clevernucleus.relicex.init.Registry;
import com.github.clevernucleus.relicex.util.Rareness;

import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Client events repository on the MOD event bus.
 */
@Mod.EventBusSubscriber(modid = RelicEx.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegistry {
	
	private static final ResourceLocation RARENESS = new ResourceLocation(RelicEx.MODID, "rareness");
	private static final IItemPropertyGetter RELIC = (par0, par1, par2) -> {
		if(!par0.hasTag() || !par0.getTag().contains("Rareness")) return 0.0F;
		
		Rareness var0 = Rareness.read(par0.getTag());
		
		return var0.getProperty();
	};
	
	/**
	 * Event handling initial client setup.
	 * @param par0
	 */
	@SubscribeEvent
	public static void onClientLoad(final net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent par0) {
		ItemModelsProperties.registerProperty(Registry.AMULET_RELIC.get(), RARENESS, RELIC);
		ItemModelsProperties.registerProperty(Registry.BODY_RELIC.get(), RARENESS, RELIC);
		ItemModelsProperties.registerProperty(Registry.HEAD_RELIC.get(), RARENESS, RELIC);
		ItemModelsProperties.registerProperty(Registry.RING_RELIC.get(), RARENESS, RELIC);
	}
}
