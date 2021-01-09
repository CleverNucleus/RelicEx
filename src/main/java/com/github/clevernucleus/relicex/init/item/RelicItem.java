package com.github.clevernucleus.relicex.init.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class RelicItem extends Item {
	public RelicItem() {
		super(new Item.Properties().maxStackSize(1).group(ItemGroup.MISC));
	}
	
}
