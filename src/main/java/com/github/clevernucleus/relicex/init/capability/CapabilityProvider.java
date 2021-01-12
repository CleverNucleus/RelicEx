package com.github.clevernucleus.relicex.init.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.clevernucleus.relicex.init.Registry;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityProvider implements ICapabilitySerializable<INBT> {
	private final LazyOptional<IData> optional;
	private final IData data;
	
	public CapabilityProvider() {
		this.data = new Data();
		this.optional = LazyOptional.of(() -> data);
	}
	
	@Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nullable Capability<T> par0, Direction par1) {
    	return Registry.DATA.orEmpty(par0, optional);
    }
    
    @Override
    public INBT serializeNBT() {
    	return Registry.DATA.writeNBT(data, null);
    }
    
    @Override
    public void deserializeNBT(INBT par0) {
    	Registry.DATA.readNBT(data, null, par0);
    }
}
