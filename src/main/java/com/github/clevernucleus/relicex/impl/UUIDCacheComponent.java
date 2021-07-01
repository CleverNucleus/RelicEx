package com.github.clevernucleus.relicex.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

public final class UUIDCacheComponent implements UUIDCache {
	private Set<Byte> bytes;
	
	public UUIDCacheComponent(PlayerEntity player) {
		this.bytes = new HashSet<Byte>();
	}
	
	@Override
	public void add(byte id) {
		this.bytes.add(id);
	}
	
	@Override
	public void remove(byte id) {
		this.bytes.remove(id);
	}
	
	@Override
	public Collection<Byte> used() {
		return ImmutableSet.copyOf(this.bytes);
	}
	
	@Override
	public void readFromNbt(CompoundTag tag) {
		if(!tag.contains("Keys")) return;
		
		byte[] arrayIn = tag.getByteArray("Keys");
		
		for(byte key : arrayIn) {
			this.bytes.add(key);
		}
	}
	
	@Override
	public void writeToNbt(CompoundTag tag) {
		Byte[] arrayIn = this.bytes.toArray(new Byte[this.bytes.size()]);
		byte[] arrayOut = new byte[arrayIn.length];
		
		for(int i = 0; i < arrayIn.length; i++) {
			arrayOut[i] = arrayIn[i].byteValue();
		}
		
		tag.putByteArray("Keys", arrayOut);
	}
}
