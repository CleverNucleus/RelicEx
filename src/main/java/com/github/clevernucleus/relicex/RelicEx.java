package com.github.clevernucleus.relicex;

import com.github.clevernucleus.relicex.init.Registry;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RelicEx.MODID)
public class RelicEx {
	public static final String MODID = "relicex";
	
	public ExCurios() {
		Registry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
