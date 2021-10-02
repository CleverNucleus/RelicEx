package com.github.clevernucleus.relicex.impl;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import com.github.clevernucleus.relicex.impl.relic.SlotKey;

public final class UUIDCacheManager {
	private static final Map<SlotKey, UUID[]> CACHE = new EnumMap<SlotKey, UUID[]>(SlotKey.class);
	
	static {
		CACHE.put(SlotKey.HEAD, new UUID[] {
			UUID.fromString("f36cf735-f203-404d-819e-e1cba2d11cea"), 
			UUID.fromString("2538cac4-0b64-45e7-b5b2-cf2cb85c842a"), 
			UUID.fromString("8d714b18-ea24-4149-b473-8597a212cd8c"), 
			UUID.fromString("7ff891f3-3550-4082-a841-2acf75107b38"), 
			UUID.fromString("338026c9-d927-4d60-b287-dd7276b44c61"), 
			UUID.fromString("3cc4b85a-1eb8-48c8-87ba-c053fb56f484"), 
			UUID.fromString("166e8047-89fa-4aeb-9391-2c2e7d8cbf53"), 
			UUID.fromString("ac4430ad-4be7-451d-98ea-9d2e5cd0688a")
		});
		CACHE.put(SlotKey.BODY, new UUID[] {
			UUID.fromString("13ec6507-9500-412a-a4c7-e6d2ea834b7c"), 
			UUID.fromString("bff12b2e-cc76-4052-a9da-55d8499dbc28"), 
			UUID.fromString("f8277823-0186-4459-9376-eeb7fb3731de"), 
			UUID.fromString("2e2f0ea8-cb95-4432-8cf1-4689946b99e1"), 
			UUID.fromString("47f9973b-4e1f-4257-9fc4-1e95723d8afc"), 
			UUID.fromString("e3acff1d-f2eb-43e3-a1b7-c843ab5b2a0c"), 
			UUID.fromString("8e51a142-7af8-43f3-9d46-051e81296cbb"), 
			UUID.fromString("0c33a7e8-0ffe-4a89-85f0-03eb72442f64")
		});
		CACHE.put(SlotKey.CHEST, new UUID[] {
			UUID.fromString("186029a8-c84b-40c9-a587-1296eb412ba8"), 
			UUID.fromString("eba8af9f-a183-476f-bd9d-3b8a169e01a5"), 
			UUID.fromString("0b22dcd5-8e3b-4524-ab3a-59e37b2cf037"), 
			UUID.fromString("01aca4db-6e53-4c78-9dfa-ce1b3516f9d3"), 
			UUID.fromString("b313dfb2-ccd1-4c9a-b947-240c6c5aaf59"), 
			UUID.fromString("b09d38ec-0f8b-44f4-9b33-994b52938a69"), 
			UUID.fromString("46878d97-8326-41f8-b811-f1328c2d18ac"), 
			UUID.fromString("34a533dd-3ffc-4d0b-8cd1-5075c1fbda62")
		});
		CACHE.put(SlotKey.HAND, new UUID[] {
			UUID.fromString("498e69a5-9759-43c1-9532-1bb825c00722"), 
			UUID.fromString("5e5706ec-13dc-4e01-a771-7a12988f6d9a"), 
			UUID.fromString("731851a5-bb38-40a0-9dc1-edcf06750a59"), 
			UUID.fromString("0d06684a-35ea-46eb-8eb9-c6bb03d01cf3"), 
			UUID.fromString("089abf21-dbde-4023-a111-d61bcdf348e2"), 
			UUID.fromString("f80e05cb-ec09-469d-a604-df28c48e8e9c"), 
			UUID.fromString("f54f095f-0641-4c5e-82e2-05573da19937"), 
			UUID.fromString("aac4ef59-f319-4f3f-9ead-94b0f64aef79")
		});
		CACHE.put(SlotKey.OFFHAND, new UUID[] {
			UUID.fromString("63f4382f-012b-4cac-b559-49f3ea7501cc"), 
			UUID.fromString("7f2d86fc-097c-4913-9d4f-36241f93e79a"), 
			UUID.fromString("c64483bd-2a21-4dde-9812-3dc49b259f1e"), 
			UUID.fromString("1432a25a-e514-4d86-b0eb-d84820dcc2b8"), 
			UUID.fromString("91a1f7e2-1129-4b5b-8688-6044fc1f0139"), 
			UUID.fromString("e09eb91b-728b-40bf-896f-24ee4ba097cd"), 
			UUID.fromString("4425904d-a9d7-497e-bb0c-d9862b1d9d61"), 
			UUID.fromString("278b8d06-ec46-4fea-9292-605dc0900b50")
		});
	}
	
	public static UUID[] get(final SlotKey key) {
		return CACHE.getOrDefault(key, new UUID[] {UUID.randomUUID()});
	}
}
