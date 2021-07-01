package com.github.clevernucleus.relicex.impl;

import java.util.Collection;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface UUIDCache extends Component {
	
	void add(byte id);
	
	void remove(byte id);
	
	Collection<Byte> used();
}
