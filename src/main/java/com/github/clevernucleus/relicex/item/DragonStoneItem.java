package com.github.clevernucleus.relicex.item;

import java.util.List;
import java.util.UUID;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerData;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DragonStoneItem extends Item {
	public DragonStoneItem() {
		super((new Item.Settings()).maxCount(1).group(ItemGroup.MATERIALS));
	}
	
	private static boolean safety(PlayerEntity user, ItemStack stack) {
		GameProfile profile = user.getGameProfile();
		
		if(profile == null) return false;
		UUID uuid = profile.getId();
		
		if(uuid == null) return false;
		NbtCompound tag = stack.getOrCreateNbt();
		
		if(tag.contains("Users", NbtElement.LIST_TYPE)) {
			NbtList users = tag.getList("Users", NbtElement.INT_ARRAY_TYPE);
			
			for(int i = 0; i < users.size(); i++) {
				UUID uuid2 = NbtHelper.toUuid(users.get(i));
				if(uuid.equals(uuid2)) return true;
			}
			
			users.add(NbtHelper.fromUuid(uuid));
		} else {
			NbtList users = new NbtList();
			users.add(NbtHelper.fromUuid(uuid));
			tag.put("Users", users);
		}
		
		return false;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("tooltip.relicex.dragon_stone").formatted(Formatting.GRAY));
	}
	
	@Override
	public Rarity getRarity(ItemStack stack) {
		return Rarity.EPIC;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		user.getItemCooldownManager().set(this, 20);
		return DataAttributesAPI.ifPresent(user, ExAPI.LEVEL, super.use(world, user, hand), value -> {
			if(!(value > 0.0D)) return super.use(world, user, hand);
			ItemStack itemStack = user.getStackInHand(hand);
			
			if(safety(user, itemStack)) {
				if(world.isClient) {
					user.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.NEUTRAL, 0.75F, 1.0F);
				} else {
					PlayerData playerData = ExAPI.PLAYER_DATA.get(user);
					playerData.reset();
					
					if(!user.isCreative()) {
						itemStack.decrement(1);
					}
				}
				
				return TypedActionResult.success(itemStack, world.isClient);
			}
			
			if(world.isClient) {
				user.sendMessage(Text.translatable("message.relicex.dragon_stone"), true);
			}
			
			return super.use(world, user, hand);
		});
	}
}
