package com.github.clevernucleus.relicex.item;

import java.util.List;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerAttributes;
import com.github.clevernucleus.playerex.api.attribute.AttributeData;
import com.github.clevernucleus.relicex.RelicEx;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TomeItem extends Item {
	public TomeItem() {
		super((new Item.Settings()).group(ItemGroup.MATERIALS));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add((new TranslatableText("tooltip.relicex.tome")).formatted(Formatting.GRAY));
	}
	
	@Override
	public Rarity getRarity(ItemStack stack) {
		return Rarity.UNCOMMON;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		AttributeData data = ExAPI.DATA.get(user);
		ItemStack stack = user.getStackInHand(hand);
		
		if(world.isClient) {
			user.playSound(RelicEx.LEVEL_REFUND, SoundCategory.NEUTRAL, 0.75F, 1.0F);
		} else {
			data.add(PlayerAttributes.LEVEL.get(), 1);
			stack.decrement(1);
		}
		
		return super.use(world, user, hand);
	}
}
