package com.github.clevernucleus.relicex.init.capability;

import java.util.Random;

import com.github.clevernucleus.relicex.RelicEx;
import com.github.clevernucleus.relicex.init.Registry;
import com.github.clevernucleus.relicex.init.item.ILoot;
import com.github.clevernucleus.relicex.init.item.RelicItem;
import com.github.clevernucleus.relicex.util.CommonConfig;
import com.github.clevernucleus.relicex.util.RandDistribution;
import com.github.clevernucleus.relicex.util.Util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RelicEx.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DataEvents {
	
	/**
	 * Event for attaching capabilities.
	 * @param par0
	 */
	@SubscribeEvent
    public static void onCapabilityAttachEntity(final net.minecraftforge.event.AttachCapabilitiesEvent<Entity> par0) {
		if(par0.getObject() instanceof PlayerEntity) {
			par0.addCapability(new ResourceLocation(RelicEx.MODID, "data"), new CapabilityProvider());
		}
	}
	
	/**
	 * Event firing when the player gets cloned.
	 * @param par0
	 */
	@SubscribeEvent
    public static void onPlayerEntityCloned(final net.minecraftforge.event.entity.player.PlayerEvent.Clone par0) {
		PlayerEntity var0 = par0.getPlayer();
		PlayerEntity var1 = par0.getOriginal();
		
		if(var0.world.isRemote) return;
		
		try {
			Data.get(var0).ifPresent(par1 -> Data.get(var1).ifPresent(par2 -> par1.read(par2.write())));
		} catch(Exception parE) {}
	}
	
	/**
	 * Event to add random drops to instances of IMobs.
	 * @param par0
	 */
	@SubscribeEvent
    public static void onLootDrop(final net.minecraftforge.event.entity.living.LivingDropsEvent par0) {
		LivingEntity var0 = par0.getEntityLiving();
		
		if(var0 instanceof IMob) {
			Random var1 = new Random();
			
			if(var1.nextInt(100) > (int)(CommonConfig.COMMON.dropChance.get().floatValue() * 100F)) return;
			
			RandDistribution<Item> var2 = new RandDistribution<Item>(Items.AIR);
			
			for(RegistryObject<Item> var : Registry.ITEMS.getEntries()) {
				Item var3 = var.get();
				
				if(var3 instanceof ILoot) {
					var2.add(var3, ((ILoot)var3).weight());
				}
			}
			
			Item var3 = var2.getDistributedRandom();
			ItemStack var4 = new ItemStack(var3);
			
			if(var3 instanceof RelicItem) {
				Util.createRandomRelic(var4);
			}
			
			par0.getDrops().add(new ItemEntity(var0.world, var0.getPosX(), var0.getPosY(), var0.getPosZ(), var4));
		}
	}
}
