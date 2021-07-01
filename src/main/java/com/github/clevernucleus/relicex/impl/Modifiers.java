package com.github.clevernucleus.relicex.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class Modifiers {
	private static final Map<Byte, UUID> MODIFIERS = new HashMap<Byte, UUID>();
	
	static {
		MODIFIERS.put(Byte.valueOf((byte)0), UUID.fromString("c0ad6784-b089-4e57-9a07-633fc4ae2c69"));
		MODIFIERS.put(Byte.valueOf((byte)1), UUID.fromString("10961175-5d64-4ebf-91f5-e9fa4044ff46"));
		MODIFIERS.put(Byte.valueOf((byte)2), UUID.fromString("25eb2d07-86e0-4f95-bcf5-90bd0e373cce"));
		MODIFIERS.put(Byte.valueOf((byte)3), UUID.fromString("4e5b569d-3d30-43d6-b2b6-40be6ccb96c5"));
		MODIFIERS.put(Byte.valueOf((byte)4), UUID.fromString("17988b06-cd4e-406e-b28a-d5885299a798"));
		MODIFIERS.put(Byte.valueOf((byte)5), UUID.fromString("30835c52-9370-4374-9934-1bf588f36bec"));
		MODIFIERS.put(Byte.valueOf((byte)6), UUID.fromString("b05571ee-64b4-4d9a-8194-9531cce7d863"));
		MODIFIERS.put(Byte.valueOf((byte)7), UUID.fromString("58c1ef48-e8a1-4897-9b2a-32f3a306d9df"));
		MODIFIERS.put(Byte.valueOf((byte)8), UUID.fromString("2a3dd823-6678-40b0-a32e-fdd8d8a09cf6"));
		MODIFIERS.put(Byte.valueOf((byte)9), UUID.fromString("ccb42c71-ad17-487d-a4ee-2f50306778d1"));
		MODIFIERS.put(Byte.valueOf((byte)10), UUID.fromString("d641cfa4-43b4-4ad6-b6e5-6ed43d156abe"));
		MODIFIERS.put(Byte.valueOf((byte)11), UUID.fromString("f0bb811c-fa7b-4a6f-870d-c8f1d27543f5"));
		MODIFIERS.put(Byte.valueOf((byte)12), UUID.fromString("32413133-9257-4ad9-bd74-3077a5d8aaac"));
		MODIFIERS.put(Byte.valueOf((byte)13), UUID.fromString("251f7a2a-55de-40e2-b78d-27d2b9c437cf"));
		MODIFIERS.put(Byte.valueOf((byte)14), UUID.fromString("94f256bf-1a1b-4b27-8bd8-03a0a5e5e661"));
		MODIFIERS.put(Byte.valueOf((byte)15), UUID.fromString("4171c281-1f41-41ee-a172-8a5ab9f24999"));
		MODIFIERS.put(Byte.valueOf((byte)16), UUID.fromString("b6bb872b-9d6c-4c1f-8805-48201cfdf4e2"));
		MODIFIERS.put(Byte.valueOf((byte)17), UUID.fromString("257c1873-9b05-4531-83c4-9c92e71c7d87"));
		MODIFIERS.put(Byte.valueOf((byte)18), UUID.fromString("5904b9b8-d2b7-4dfb-97a9-c0f1cb7d6412"));
		MODIFIERS.put(Byte.valueOf((byte)19), UUID.fromString("84106336-1ffe-4911-8b4b-3d46b8a69fcc"));
		MODIFIERS.put(Byte.valueOf((byte)20), UUID.fromString("a311c8a7-97a6-49ab-824e-0fe7f7af5fe5"));
		MODIFIERS.put(Byte.valueOf((byte)21), UUID.fromString("2d2e6278-7e3a-4e33-9bd6-dfda23ebbda2"));
		MODIFIERS.put(Byte.valueOf((byte)22), UUID.fromString("8e25c591-cbc2-4e83-b726-d68c2be4ef15"));
		MODIFIERS.put(Byte.valueOf((byte)23), UUID.fromString("174585d7-fad0-461a-a67d-8c8db6a35fcf"));
		MODIFIERS.put(Byte.valueOf((byte)24), UUID.fromString("331c9cc5-5c21-4449-90f0-e2a4020a2b1e"));
	}
	
	public static UUID get(final byte key) {
		return MODIFIERS.getOrDefault(Byte.valueOf(key), UUID.randomUUID());
	}
	
	public static byte getUnused(Collection<Byte> used) {
		Set<Byte> bytes = new HashSet<Byte>();
		
		MODIFIERS.keySet().forEach(bytes::add);
		used.forEach(bytes::remove);
		Byte[] array = bytes.toArray(new Byte[bytes.size()]);
		
		return bytes.isEmpty() ? -1 : array[0].byteValue();
	}
}
