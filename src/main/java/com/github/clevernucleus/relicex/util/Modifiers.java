package com.github.clevernucleus.relicex.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Modifiers {
	private static final Map<Integer, UUID> MAP = new HashMap<>();
	
	static {
		MAP.put(0, UUID.fromString("a3d73279-e87d-4740-ad03-96a5df91b314"));
		MAP.put(1, UUID.fromString("c5b55d6f-328a-4ba9-b22b-5c3eb062fb48"));
		MAP.put(2, UUID.fromString("62e9c25f-d345-426f-9b55-575ebff87100"));
		MAP.put(3, UUID.fromString("53e1f895-d4ea-4e40-ab94-87655eb3c930"));
		MAP.put(4, UUID.fromString("8891e087-addb-4122-8f4a-9271ab452141"));
		MAP.put(5, UUID.fromString("4fd1b09f-b46b-4b06-8130-7b64488bf0a7"));
		MAP.put(6, UUID.fromString("e9185eaf-7211-4f89-b69f-8e72a411fafd"));
		MAP.put(7, UUID.fromString("d75da3c4-3e3a-4e8b-9d32-a81ea7df7823"));
		MAP.put(8, UUID.fromString("1ceb5c41-6e1b-465f-af70-838c8beb49c5"));
		MAP.put(9, UUID.fromString("4822202c-cf7d-4a0e-a311-25c70c29f663"));
		MAP.put(10, UUID.fromString("2b5907db-55a0-4a4b-8664-48b3d33e5701"));
		MAP.put(11, UUID.fromString("f54cc042-63ec-44c2-baad-2cbfc44930d6"));
		MAP.put(12, UUID.fromString("bca3858c-bd04-49be-8950-189472f59859"));
		MAP.put(13, UUID.fromString("1e7bd5a7-d685-43b5-a584-c6e3875f32da"));
		MAP.put(14, UUID.fromString("114fc411-da6a-4780-9fbe-fdae27454f78"));
		MAP.put(15, UUID.fromString("6257f554-dd64-48bd-9404-089ffe4752f2"));
		MAP.put(16, UUID.fromString("c1305b84-685a-4c1a-9307-7c6205469409"));
		MAP.put(17, UUID.fromString("4ec85407-fe22-4623-a176-46bc6e30ffd2"));
		MAP.put(18, UUID.fromString("2a7f3aac-c36f-4637-b3e0-58e602289c00"));
		MAP.put(19, UUID.fromString("0514a1b6-7b3a-4ef8-87e8-50649e98cf8d"));
	}
	
	/**
	 * @param par0 input int
	 * @return a UUID corresponding to this int. If none exist, creates a random one.
	 */
	public static UUID get(final int par0) {
		return MAP.getOrDefault(Integer.valueOf(par0), UUID.randomUUID());
	}
	
	/**
	 * @param par0 input collection of ints.
	 * @return the next int key that is available (input list is a list of used keys).
	 */
	public static int getUnused(Collection<Integer> par0) {
		Set<Integer> var0 = new HashSet<>();
		
		MAP.keySet().forEach(var0::add);
		par0.forEach(var0::remove);
		
		Integer[] var1 = var0.toArray(new Integer[var0.size()]);
		
		return var0.isEmpty() ? -1 : var1[0].intValue();
	}
}
