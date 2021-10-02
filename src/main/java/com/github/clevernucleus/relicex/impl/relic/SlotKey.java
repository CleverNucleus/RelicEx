package com.github.clevernucleus.relicex.impl.relic;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum SlotKey {
	HEAD("head"), 
	BODY("body"), 
	CHEST("chest"), 
	HAND("hand"), 
	OFFHAND("offhand");
	
	private static final Map<String, SlotKey> VALUES = Arrays.stream(SlotKey.values()).collect(Collectors.toMap(SlotKey::toString, type -> type));
	private final String key;
	
	private SlotKey(final String key) {
		this.key = key;
	}
	
	public static SlotKey from(final String key) {
		return VALUES.getOrDefault(key, (SlotKey)null);
	}
	
	@Override
	public String toString() {
		return this.key;
	}
}
