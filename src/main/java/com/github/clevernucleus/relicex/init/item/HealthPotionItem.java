package com.github.clevernucleus.relicex.init.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class HealthPotionItem extends Item implements ILoot {
	private float amount, weight;
	
	public HealthPotionItem(final float par0, final float par1) {
		super(new Item.Properties().maxStackSize(1).group(ItemGroup.BREWING));
		
		this.amount = par0;
		this.weight = par1;
	}
	
	@Override
	public float weight() {
		return this.weight;
	}
	
	@Override
	public void inventoryTick(ItemStack par0, World par1, Entity par2, int par3, boolean par4) {
		if(par1.isRemote) return;
		if(!(par2 instanceof PlayerEntity)) return;
		
		PlayerEntity var0 = (PlayerEntity)par2;
		
		var0.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 2F, 2F);
		var0.heal(this.amount);
		par0.shrink(1);
	}
}
