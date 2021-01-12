package com.github.clevernucleus.relicex.init.item;

import javax.annotation.Nullable;

import org.apache.logging.log4j.util.TriConsumer;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttributes;
import com.github.clevernucleus.playerex.api.attribute.PlayerAttributes;
import com.github.clevernucleus.relicex.init.capability.Data;
import com.github.clevernucleus.relicex.init.capability.IData;
import com.github.clevernucleus.relicex.util.Modifiers;
import com.github.clevernucleus.relicex.util.Util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class RelicItem extends Item {
	public RelicItem() {
		super(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS));
	}
	
	private void wrap(LivingEntity par0, TriConsumer<PlayerEntity, IPlayerAttributes, IData> par1) {
		if(!(par0 instanceof PlayerEntity)) return;
		
		PlayerEntity var0 = (PlayerEntity)par0;
		
		ExAPI.playerAttributes(var0).ifPresent(var1 -> Data.get(var0).ifPresent(var2 -> par1.accept(var0, var1, var2)));
	}
	
	private void onEquipped(PlayerEntity par0, IPlayerAttributes par1, IData par2, ItemStack par3) {
		if(!par3.hasTag() || !par3.getTag().contains("Attributes")) return;
		
		for(INBT var : par3.getTag().getList("Attributes", 10)) {
			CompoundNBT var0 = (CompoundNBT)var;
			IPlayerAttribute var1 = PlayerAttributes.fromRegistryName(var0.getString("Name"));
			
			double var2 = var0.getDouble("Value");
			int var3 = (var0.contains("UUID") ? var0.getInt("UUID") : Modifiers.getUnused(par2.values()));
			
			AttributeModifier var4 = new AttributeModifier(Modifiers.get(var3), var1.registryName() + "_add_" + var3, var2, AttributeModifier.Operation.ADDITION);
			
			par1.applyModifier(par0, var1, var4);
			par2.put(var3);
			var0.putInt("UUID", var3);
		}
	}
	
	private void onUnequipped(PlayerEntity par0, IPlayerAttributes par1, IData par2, ItemStack par3) {
		if(!par3.hasTag() || !par3.getTag().contains("Attributes")) return;
		
		for(INBT var : par3.getTag().getList("Attributes", 10)) {
			CompoundNBT var0 = (CompoundNBT)var;
			IPlayerAttribute var1 = PlayerAttributes.fromRegistryName(var0.getString("Name"));
			
			if(!var0.contains("UUID")) continue;
			
			double var2 = var0.getDouble("Value");
			int var3 = var0.getInt("UUID");
			
			AttributeModifier var4 = new AttributeModifier(Modifiers.get(var3), var1.registryName() + "_add_" + var3, var2, AttributeModifier.Operation.ADDITION);
			
			par1.removeModifier(par0, var1, var4);
			par2.remove(var3);
			var0.remove("UUID");
		}
	}
	
	@Override
	public void inventoryTick(ItemStack par0, World par1, Entity par2, int par3, boolean par4) {
		if(par0.hasTag() && par0.getTag().contains("Attributes") && par0.getTag().contains("Rareness")) return;
		
		Util.createRandomRelic(par0);
	}
	
	@Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack par0, @Nullable CompoundNBT par1) {
		return new ICapabilityProvider() {
			private final LazyOptional<ICurio> var0 = LazyOptional.of(() -> new ICurio() {
				
				@Override
				public void onEquip(String par, int par1, LivingEntity par2) {
					wrap(par2, (var0, var1, var2) -> onEquipped(var0, var1, var2, par0));
				}
				
				@Override
				public void onUnequip(String par, int par1, LivingEntity par2) {
					wrap(par2, (var0, var1, var2) -> onUnequipped(var0, var1, var2, par0));
				}
			});
			
			@Override
			public <T> LazyOptional<T> getCapability(Capability<T> par0, Direction par1) {
				return CuriosCapability.ITEM.orEmpty(par0, var0);
			}
		};
	}
}
