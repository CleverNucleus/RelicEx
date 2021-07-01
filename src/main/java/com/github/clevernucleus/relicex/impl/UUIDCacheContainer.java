package com.github.clevernucleus.relicex.impl;

import com.github.clevernucleus.relicex.RelicEx;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;

public final class UUIDCacheContainer implements EntityComponentInitializer {
	
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(RelicEx.CACHE, UUIDCacheComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
	}
}
