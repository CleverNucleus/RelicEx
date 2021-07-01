package com.github.clevernucleus.relicex.mixin.client;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.clevernucleus.playerex.api.attribute.AttributeProperties;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.api.util.Maths;
import com.github.clevernucleus.relicex.item.RelicItem;
import com.github.clevernucleus.relicex.util.Rareness;
import com.github.clevernucleus.relicex.util.Util;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	private static String value(IPlayerAttribute attribute, double value) {
		DecimalFormat format = new DecimalFormat("###.##");
		String text = format.format(value);
		String suffix = attribute.hasProperty(AttributeProperties.PROPERTY_PERCENT) ? "%" : "";
		String result = text + suffix;
		
		return (value > 0) ? "+" + result : result;
	}
	
	private static void relicTooltip(PlayerEntity player, TooltipContext context, ItemStack stack, List<Text> list) {
		list.clear();
		
		MutableText header = (new LiteralText("")).append(stack.getName()).formatted(stack.getRarity().formatting);
		
		if(stack.hasCustomName()) {
			header.formatted(Formatting.ITALIC);
		}
		
		list.add(header);
		
		if(stack.hasTag()) {
			CompoundTag tag = stack.getTag();
			Rareness rareness = Rareness.read(tag);
			Map<IPlayerAttribute, Double> attributeMap = new HashMap<IPlayerAttribute, Double>();
			Util.readAttributeMap(attributeMap, tag);
			list.add(rareness.text());
			list.add(LiteralText.EMPTY);
			list.add((new TranslatableText("tooltip.relicex.worn")).formatted(Formatting.GRAY));
			
			for(Map.Entry<IPlayerAttribute, Double> entry : attributeMap.entrySet()) {
				IPlayerAttribute attribute = entry.getKey();
				double value = Maths.shownValue(attribute, entry.getValue());
				MutableText text = new LiteralText(value(attribute, value) + " ");
				TranslatableText append = new TranslatableText(attribute.translationKey());
				text.append(append);
				text.formatted(Formatting.BLUE);
				list.add(text);
			}
		}
		
		if(context.isAdvanced()) {
			list.add((new LiteralText(Registry.ITEM.getId(stack.getItem()).toString())).formatted(Formatting.DARK_GRAY));
			
			if(stack.hasTag()) {
				list.add((new TranslatableText("item.nbt_tags", new Object[] {stack.getTag().getKeys().size()})).formatted(Formatting.DARK_GRAY));
			}
		}
	}
	
	@Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
	private void tooltipFix(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info) {
		List<Text> list = info.getReturnValue();
		ItemStack stack = (ItemStack)(Object)this;
		
		if(stack != null) {
			Item item = stack.getItem();
			
			if(item instanceof RelicItem) {
				relicTooltip(player, context, stack, list);
				info.cancel();
			}
		}
	}
}
