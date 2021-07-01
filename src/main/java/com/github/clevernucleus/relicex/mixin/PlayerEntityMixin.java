package com.github.clevernucleus.relicex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@SuppressWarnings("deprecation")
	@Inject(method = "dropInventory", at = @At("TAIL"))
	private void dropInventory(CallbackInfo info) {
		if(!this.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
			Inventory inv = TrinketsApi.TRINKETS.get(this).getInventory();
			
			for(int i = 0; i < inv.size(); i++) {
				ItemStack stack = inv.getStack(i);
				
				if(!stack.isEmpty()) {
					Item item = stack.getItem();
					
					if(item instanceof Trinket) {
						Trinket trinket = (Trinket)item;
						trinket.onUnequip((PlayerEntity)(Object)this, stack);
					}
				}
			}
		}
	}
}
