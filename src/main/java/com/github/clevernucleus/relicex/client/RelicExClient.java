package com.github.clevernucleus.relicex.client;

import com.github.clevernucleus.dataattributes.api.event.client.ClientSyncedEvent;
import com.github.clevernucleus.relicex.RelicEx;
import com.github.clevernucleus.relicex.impl.item.RelicItem;
import com.github.clevernucleus.relicex.util.Rareness;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public final class RelicExClient implements ClientModInitializer {
	private static final Identifier RARENESS = new Identifier(RelicEx.MODID, "rareness");
	
	private static float modelPredicateProvider(ItemStack itemStack, ClientWorld world, LivingEntity entity, int i) {
		if(!itemStack.hasNbt() || !itemStack.getNbt().contains("Rareness")) return 0.0F;
		
		Rareness rareness = Rareness.fromKey(itemStack.getNbt().getString("Rareness"));
		return rareness.predicate();
	}
	
	@Override
	public void onInitializeClient() {
		RelicEx.ITEMS.stream().filter(item -> item instanceof RelicItem).forEach(item -> FabricModelPredicateProviderRegistry.register(item, RARENESS, RelicExClient::modelPredicateProvider));
		ClientSyncedEvent.EVENT.register(server -> RelicEx.RARITY_MANAGER.onRarityLoaded());
	}
}