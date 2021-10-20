package com.github.clevernucleus.relicex.impl.item;

import java.util.List;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.ModifierData;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

public final class DragonStoneItem extends Item {
	public DragonStoneItem() {
		super((new Item.Settings()).maxCount(1).group(ItemGroup.MATERIALS));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add((new TranslatableText("tooltip.relicex.dragon_stone")).formatted(Formatting.GRAY));
	}
	
	@Override
	public Rarity getRarity(ItemStack stack) {
		return Rarity.EPIC;
	}
	
	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		AttributeContainer container = user.getAttributes();
		EntityAttribute attribute = ExAPI.LEVEL.get();
		if(!container.hasAttribute(attribute)) return super.useOnEntity(stack, user, entity, hand);
		
		ModifierData data = ExAPI.DATA.get(user);
		double levels = container.getValue(attribute);
		boolean success = (levels > 0.0D) && (entity instanceof EndermanEntity);
		
		if(!success) return super.useOnEntity(stack, user, entity, hand);
		if(user.world.isClient) {
			user.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.NEUTRAL, 0.75F, 1.5F);
		} else {
			data.reset();
			
			if(user.getHealth() > user.getMaxHealth()) {
				user.setHealth(user.getMaxHealth());
			}
			
			stack.decrement(1);
		}
		
		return super.useOnEntity(stack, user, entity, hand);
	}
}
