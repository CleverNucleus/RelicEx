package com.github.clevernucleus.relicex.init.capability;

import java.util.Collection;

import net.minecraft.nbt.CompoundNBT;

public interface IData {
	
	void put(final int par0);
	
	void remove(final int par0);
	
	Collection<Integer> values();
	
	CompoundNBT write();
	
	void read(CompoundNBT par0);
}
