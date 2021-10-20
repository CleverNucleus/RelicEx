package com.github.clevernucleus.relicex.impl.item;

import java.util.List;
import java.util.function.Supplier;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.ModifierData;
import com.github.clevernucleus.relicex.RelicEx;
import com.google.common.collect.Lists;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
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

public final class OrbOfRegretItem extends Item {
	private static final List<Supplier<EntityAttribute>> ATTRIBUTES = Lists.newArrayList(ExAPI.CONSTITUTION, ExAPI.STRENGTH, ExAPI.DEXTERITY, ExAPI.INTELLIGENCE, ExAPI.LUCKINESS);
	private final boolean greater;
	
	public OrbOfRegretItem(final boolean greater) {
		super((new Item.Settings()).maxCount(1).group(ItemGroup.MATERIALS));
		
		this.greater = greater;
	}
	
	private static boolean checkHasAttributes(AttributeContainer container) {
		for(Supplier<EntityAttribute> supplier : ATTRIBUTES) {
			EntityAttribute attribute = supplier.get();
			if(!container.hasAttribute(attribute)) return false;
		}
		
		return true;
	}
	
	private static int total(ModifierData data) {
		int total = 0;
		
		for(Supplier<EntityAttribute> supplier : ATTRIBUTES) {
			int amount = Math.round((float)data.get(supplier.get()));
			
			total += amount;
		}
		
		return total;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		final String type = this.greater ? "greater_orb_of_regret" : "lesser_orb_of_regret";
		tooltip.add((new TranslatableText("tooltip.relicex." + type)).formatted(Formatting.GRAY));
	}
	
	@Override
	public Rarity getRarity(ItemStack stack) {
		return this.greater ? Rarity.EPIC : Rarity.RARE;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		AttributeContainer container = user.getAttributes();
		if(!checkHasAttributes(container)) return super.use(world, user, hand);
		
		ModifierData data = ExAPI.DATA.get(user);
		ItemStack stack = user.getStackInHand(hand);
		double levels = container.getValue(ExAPI.LEVEL.get());
		int total = total(data);
		boolean success = levels > 0.0D && total > 0;
		
		if(!success) return super.use(world, user, hand);
		if(world.isClient) {
			user.playSound(RelicEx.LEVEL_REFUND_SOUND, SoundCategory.NEUTRAL, 0.75F, 1.0F);
		} else {
			int points = this.greater ? total : 1;
			data.addRefundPoints(points);
			stack.decrement(1);
		}
		
		return super.use(world, user, hand);
	}
}
