package com.github.clevernucleus.relicex.client;

import com.github.clevernucleus.armormodelpredicate.ArmorModelPredicateProviderRegistry;
import com.github.clevernucleus.armormodelpredicate.Provider;
import com.github.clevernucleus.dataattributes.api.event.client.ClientSyncedEvent;
import com.github.clevernucleus.relicex.RelicEx;
import com.github.clevernucleus.relicex.impl.item.ArmorRelicItem;
import com.github.clevernucleus.relicex.impl.item.RelicItem;
import com.github.clevernucleus.relicex.util.Rareness;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public final class RelicExClient implements ClientModInitializer {
	private static final Identifier RARENESS = new Identifier(RelicEx.MODID, "rareness");
	
	private static float modelPredicateProvider(ItemStack itemStack, ClientWorld world, LivingEntity entity, int i) {
		if(!itemStack.hasNbt() || !itemStack.getNbt().contains("Rareness")) return 0.0F;
		
		Rareness rareness = Rareness.fromKey(itemStack.getNbt().getString("Rareness"));
		return rareness.predicate();
	}
	
	private static Provider armorModelPredicateProvider(final Identifier defaultIdentifier, final LivingEntity entity, final ItemStack itemStack, final ArmorMaterial material, final boolean legs) {
		if(!itemStack.hasNbt() || !itemStack.getNbt().contains("Rareness")) return provider -> provider.accept(defaultIdentifier, 1.0F, 1.0F, 1.0F);
		
		Rareness rareness = Rareness.fromKey(itemStack.getNbt().getString("Rareness"));
		String path = "textures/models/armor/" + rareness.toString() + ".png";
		Identifier identifier = new Identifier(RelicEx.MODID, path);
		
		return provider -> provider.accept(identifier, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public void onInitializeClient() {
		RelicEx.ITEMS.stream().filter(item -> item instanceof RelicItem || item instanceof ArmorRelicItem).forEach(item -> FabricModelPredicateProviderRegistry.register(item, RARENESS, RelicExClient::modelPredicateProvider));
		ArmorModelPredicateProviderRegistry.register(RelicEx.HEAD_RELIC, RelicExClient::armorModelPredicateProvider);
		ArmorModelPredicateProviderRegistry.register(RelicEx.BODY_RELIC, RelicExClient::armorModelPredicateProvider);
		ClientSyncedEvent.EVENT.register(server -> RelicEx.RARITY_MANAGER.onRarityLoaded());
	}
}