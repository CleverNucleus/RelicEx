package com.github.clevernucleus.relicex;

import java.util.function.Function;

import com.github.clevernucleus.armorrenderlib.api.ArmorRenderLib;
import com.github.clevernucleus.armorrenderlib.api.ArmorRenderProvider;
import com.github.clevernucleus.dataattributes.api.event.AttributesReloadedEvent;
import com.github.clevernucleus.relicex.impl.EntityAttributeCollection;
import com.github.clevernucleus.relicex.impl.Rareness;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class RelicExClient implements ClientModInitializer {
	private static final Identifier RARENESS = new Identifier(RelicEx.MODID, "rareness");
	
	private static float predicate(ItemStack itemStack, ClientWorld world, LivingEntity livingEntity, int i) {
		NbtCompound tag = itemStack.getNbt();
		
		if(tag == null || !tag.contains(EntityAttributeCollection.KEY_RARENESS, NbtType.STRING)) return 0.0F;
		Rareness rareness = Rareness.fromKey(tag.getString(EntityAttributeCollection.KEY_RARENESS));
		return rareness.predicate();
	}
	
	private static ArmorRenderProvider render(final ItemStack itemStack, final LivingEntity livingEntity, final EquipmentSlot slot) {
		NbtCompound tag = itemStack.getNbt();
		Function<Rareness, String> texture = rareness -> RelicEx.MODID + ":textures/models/armor/" + rareness.toString() + ".png";
		
		if(tag == null || !tag.contains(EntityAttributeCollection.KEY_RARENESS, NbtType.STRING)) return s -> s.accept(texture.apply(Rareness.COMMON), 0xFFFFFF, false);
		return s -> s.accept(texture.apply(Rareness.fromKey(tag.getString(EntityAttributeCollection.KEY_RARENESS))), 0xFFFFFF, false);
	}
	
	@Override
	public void onInitializeClient() {
		RelicEx.RELICS.forEach(item -> ModelPredicateProviderRegistry.register(item, RARENESS, RelicExClient::predicate));
		ArmorRenderLib.register(RelicExClient::render, RelicEx.HEAD_RELIC, RelicEx.CHEST_RELIC);
		AttributesReloadedEvent.EVENT.register(RelicEx.RARITY_MANAGER::onPropertiesLoaded);
	}
}
