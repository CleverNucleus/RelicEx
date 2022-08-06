package com.github.clevernucleus.relicex.impl;

import java.util.Map;
import java.util.UUID;

import com.github.clevernucleus.dataattributes.api.util.Maths;

public enum SlotKey {
	HEAD("head", UUID.fromString("a13445e4-52da-4636-ae11-1219aa778d34")),
	CHEST("chest", UUID.fromString("a07239d7-69c8-44f8-8d43-fb7cfab03326")),
	NECKLACE("chest/necklace", UUID.fromString("8047a968-91ec-4989-9ced-23bbc124d9e3")),
	HAND("hand/ring", UUID.fromString("7a5c1339-b72f-4e87-b61d-aee0edcb230a")),
	OFFHAND("offhand/ring", UUID.fromString("8c45fc9a-270e-4520-8977-21359a15354a"));
	
	private static final Map<String, SlotKey> VALUES = Maths.enumLookupMap(SlotKey.values(), SlotKey::toString);
	private final String key;
	private final UUID uuid;
	
	private SlotKey(final String key, final UUID uuid) {
		this.key = key;
		this.uuid = uuid;
	}
	
	public static SlotKey from(final String key) {
		return VALUES.get(key);
	}
	
	public UUID uuid() {
		return this.uuid;
	}
	
	@Override
	public String toString() {
		return this.key;
	}
}
