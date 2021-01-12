package com.github.clevernucleus.relicex.init.capability;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.clevernucleus.relicex.init.Registry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.LazyOptional;

public class Data implements IData {
	private CompoundNBT tag;
	
	public Data() {
		ListNBT var0 = new ListNBT();
		
		this.tag = new CompoundNBT();
		this.tag.put("Values", var0);
	}
	
	private boolean contains(final int par0) {
		if(this.tag.getList("Values", 1).isEmpty()) return false;
		
		for(INBT var : this.tag.getList("Values", 1)) {
			ByteNBT var0 = (ByteNBT)var;
			
			if(var0.getInt() == par0) return true;
		}
		
		return false;
	}
	
	@Override
	public void put(final int par0) {
		if(contains(par0)) return;
		this.tag.getList("Values", 1).add(ByteNBT.valueOf((byte)par0));
	}
	
	@Override
	public void remove(final int par0) {
		if(!contains(par0)) return;
		
		for(int var = 0; var < this.tag.getList("Values", 1).size(); var++) {
			ByteNBT var0 = (ByteNBT)this.tag.getList("Values", 1).get(var);
			
			if(var0.getInt() == par0) {
				this.tag.getList("Values", 1).remove(var);
			}
		}
	}
	
	@Override
	public Collection<Integer> values() {
		Stream<Integer> var0 = this.tag.getList("Values", 1).stream().map(var -> ((ByteNBT)var).getInt());
		Set<Integer> var1 = var0.collect(Collectors.toSet());
		
		var0.close();
		
		return var1;
	}
	
	@Override
	public CompoundNBT write() {
		return this.tag;
	}
	
	@Override
	public void read(CompoundNBT par0) {
		this.tag = par0;
	}
	
	/**
	 * @param par0 Player instance.
	 * @return The player data capability instance.
	 */
	public static LazyOptional<IData> get(PlayerEntity par0) {
		return par0.getCapability(Registry.DATA, null);
	}
}
