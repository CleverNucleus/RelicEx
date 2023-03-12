package com.github.clevernucleus.relicex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

import org.slf4j.Logger;

import com.github.clevernucleus.armorrenderlib.api.ArmorRenderLib;
import com.github.clevernucleus.armorrenderlib.api.ArmorRenderProvider;
import com.github.clevernucleus.dataattributes.api.event.AttributesReloadedEvent;
import com.github.clevernucleus.relicex.impl.EntityAttributeCollection;
import com.github.clevernucleus.relicex.impl.Rareness;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.mojang.logging.LogUtils;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

public class RelicExClient implements ClientModInitializer {
	private static final Identifier RARENESS = new Identifier(RelicEx.MODID, "rareness");
	private static final RarenessColor RARENESS_COLOR = new RarenessColor();
	
	private static float predicate(ItemStack itemStack, ClientWorld world, LivingEntity livingEntity, int i) {
		NbtCompound tag = itemStack.getNbt();
		
		if(tag == null || !tag.contains(EntityAttributeCollection.KEY_RARENESS, NbtElement.STRING_TYPE)) return 0.0F;
		Rareness rareness = Rareness.fromKey(tag.getString(EntityAttributeCollection.KEY_RARENESS));
		return rareness.predicate();
	}
	
	private static ArmorRenderProvider render(final ItemStack itemStack, final LivingEntity livingEntity, final EquipmentSlot slot) {
		NbtCompound tag = itemStack.getNbt();
		Function<Rareness, String> texture = rareness -> RelicEx.MODID + ":textures/models/armor/" + rareness.toString() + ".png";
		
		if(tag == null || !tag.contains(EntityAttributeCollection.KEY_RARENESS, NbtElement.STRING_TYPE)) return s -> s.accept(texture.apply(Rareness.COMMON), 0xFFFFFF, false);
		return s -> s.accept(texture.apply(Rareness.fromKey(tag.getString(EntityAttributeCollection.KEY_RARENESS))), 0xFFFFFF, false);
	}
	
	public static Formatting getColor(final Rareness rareness, final Formatting fallback) {
		return RelicExClient.RARENESS_COLOR.data.getOrDefault(rareness, fallback);
	}
	
	@Override
	public void onInitializeClient() {
		RelicEx.RELICS.forEach(item -> ModelPredicateProviderRegistry.register(item, RARENESS, RelicExClient::predicate));
		ArmorRenderLib.register(RelicExClient::render, RelicEx.HEAD_RELIC, RelicEx.CHEST_RELIC);
		AttributesReloadedEvent.EVENT.register(RelicEx.RARITY_MANAGER::onPropertiesLoaded);
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(RARENESS_COLOR);
	}
	
	private static final class RarenessFormatting {
		@Expose protected Map<Rareness, Formatting> values;
	}
	
	private static final class RarenessColor implements SimpleResourceReloadListener<Map<Rareness, Formatting>> {
		private static final Gson GSON = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
		private static final int PATH_SUFFIX_LENGTH = ".json".length();
		private static final Logger LOGGER = LogUtils.getLogger();
		private static final String DIRECTORY = "rareness";
		private static final Identifier ID = new Identifier(RelicEx.MODID, DIRECTORY);
		
		protected Map<Rareness, Formatting> data = new HashMap<Rareness, Formatting>();
		
		protected RarenessColor() {}
		
		@Override
		public CompletableFuture<Map<Rareness, Formatting>> load(ResourceManager manager, Profiler profiler, Executor executor) {
			return CompletableFuture.supplyAsync(() -> {
				Map<Identifier, RarenessFormatting> cache = new HashMap<Identifier, RarenessFormatting>();
				int length = DIRECTORY.length() + 1;
				
				for(Map.Entry<Identifier, Resource> entry : manager.findResources(DIRECTORY, id -> id.getPath().endsWith("colors.json")).entrySet()) {
					Identifier resource = entry.getKey();
					String path = resource.getPath();
					Identifier identifier = new Identifier(resource.getNamespace(), path.substring(length, path.length() - PATH_SUFFIX_LENGTH));
					
					try {
						BufferedReader reader = entry.getValue().getReader();
						
						try {
							RarenessFormatting json = JsonHelper.deserialize(GSON, (Reader)reader, RarenessFormatting.class);
							
							if(json != null) {
								RarenessFormatting object = cache.put(identifier, json);
								
								if(object == null) continue;
								throw new IllegalStateException("Duplicate asset file ignored with ID " + identifier);
							}
							
							LOGGER.error("Couldn't load asset file {} from {} as it's null or empty", (Object)identifier, (Object)resource);
						} finally {
							if(reader == null) continue;
							((Reader)reader).close();
						}
					} catch(IOException | IllegalArgumentException exception) {
						LOGGER.error("Couldn't parse asset file {} from {}", identifier, resource, exception);
					}
				}
				
				Map<Rareness, Formatting> data = new HashMap<Rareness, Formatting>();
				cache.forEach((id, json) -> json.values.forEach(data::put));
				
				return data;
			}, executor);
		}
		
		@Override
		public CompletableFuture<Void> apply(Map<Rareness, Formatting> data, ResourceManager manager, Profiler profiler, Executor executor) {
			return CompletableFuture.runAsync(() -> {
				this.data = data;
			}, executor);
		}
		
		@Override
		public Identifier getFabricId() {
			return ID;
		}
	}
}
