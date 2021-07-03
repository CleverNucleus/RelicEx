package com.github.clevernucleus.relicex;

import com.github.clevernucleus.relicex.item.RelicItem;
import com.github.clevernucleus.relicex.util.Rareness;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public final class RelicExClient implements ClientModInitializer {
	private static final Identifier RARENESS = new Identifier(RelicEx.MODID, "rareness");
	
	private static float modelPredicateProvider(ItemStack itemstack, ClientWorld world, LivingEntity entity) {
		if(!itemstack.hasTag() || !itemstack.getTag().contains("Rareness")) return 0.0F;
		
		Rareness rareness = Rareness.read(itemstack.getTag());
		
		return rareness.property();
	}
	
	@Override
	public void onInitializeClient() {
		RelicEx.ITEMS.stream().filter(item -> item instanceof RelicItem).forEach(item -> FabricModelPredicateProviderRegistry.register(item, RARENESS, RelicExClient::modelPredicateProvider));
	}
}
