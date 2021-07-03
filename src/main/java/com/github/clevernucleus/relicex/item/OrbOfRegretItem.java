package com.github.clevernucleus.relicex.item;

import java.util.List;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerAttributes;
import com.github.clevernucleus.playerex.api.attribute.AttributeData;
import com.github.clevernucleus.playerex.api.attribute.IAttribute;
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

public class OrbOfRegretItem extends Item {
	private static final IAttribute[] ATTRIBUTES = new IAttribute[] {PlayerAttributes.CONSTITUTION, PlayerAttributes.STRENGTH, PlayerAttributes.DEXTERITY, PlayerAttributes.INTELLIGENCE, PlayerAttributes.LUCKINESS};
	private final boolean greater;
	
	public OrbOfRegretItem(final boolean greater) {
		super((new Item.Settings()).maxCount(1).group(ItemGroup.MATERIALS));
		this.greater = greater;
	}
	
	private static int total(AttributeData data) {
		int total = 0;
		
		for(IAttribute attribute : ATTRIBUTES) {
			int amount = Math.round((float)((com.github.clevernucleus.playerex.impl.attribute.AttributeDataManager)data).getValue(attribute.get()));
			total += amount;
		}
		
		return total;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		String type = this.greater ? "greater_orb_of_regret" : "lesser_orb_of_regret";
		tooltip.add((new TranslatableText("tooltip.relicex." + type)).formatted(Formatting.GRAY));
	}
	
	@Override
	public Rarity getRarity(ItemStack stack) {
		return this.greater ? Rarity.EPIC : Rarity.RARE;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		AttributeData data = ExAPI.DATA.get(user);
		ItemStack stack = user.getStackInHand(hand);
		double levels = data.get(PlayerAttributes.LEVEL.get());
		int total = total(data);
		boolean success = levels > 0.0D && total > 0;
		
		if(!success) return super.use(world, user, hand);
		if(world.isClient) {
			user.playSound(RelicEx.LEVEL_REFUND, SoundCategory.NEUTRAL, 0.75F, 1.0F);
		} else {
			int points = this.greater ? total : 1;
			data.addRefundPoints(points);
			stack.decrement(1);
		}
		
		return super.use(world, user, hand);
	}
}
