package com.github.clevernucleus.relicex;

import com.github.clevernucleus.relicex.init.Registry;
import com.github.clevernucleus.relicex.util.CommonConfig;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RelicEx.MODID)
public class RelicEx {
	public static final String MODID = "relicex";
	
	public RelicEx() {
		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.COMMON_SPEC);
		Registry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
