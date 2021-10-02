package com.github.clevernucleus.relicex.mixin.client;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.clevernucleus.dataattributes.api.attribute.IAttribute;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.ClientUtil;
import com.github.clevernucleus.relicex.impl.item.RelicItem;
import com.google.common.collect.Multimap;

import dev.emi.trinkets.api.SlotAttributes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

@Mixin(value = ItemStack.class, priority = 1001)
abstract class ItemStackMixin {
	
	@Inject(at = @At("RETURN"), method = "getTooltip", cancellable = true)
	private void getTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info) {
		ItemStack stack = (ItemStack)(Object)this;
		Item item = stack.getItem();
		List<Text> tooltip = info.getReturnValue();
		
		if(item instanceof RelicItem) {
			RelicItem relicItem = (RelicItem)item;
			relicItem.amendTooltip(tooltip);
		}
	}
	
	@Inject(method = "addAttributes", at = @At("HEAD"), cancellable = true)
	private void trinketsInject(List<Text> list, Multimap<EntityAttribute, EntityAttributeModifier> map, CallbackInfo info) {
		if(!map.isEmpty()) {
			for(Map.Entry<EntityAttribute, EntityAttributeModifier> entry : map.entries()) {
				EntityAttribute attribute = entry.getKey();
				EntityAttributeModifier modifier = entry.getValue();
				String p = "";
				double g = modifier.getValue();
				
				if(modifier.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_BASE && modifier.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
					IAttribute instance = (IAttribute)attribute;
					g = ClientUtil.displayValue(instance, g);
					
					if(instance.hasProperty(ExAPI.PERCENTAGE_PROPERTY)) {
						p = "%";
					}
				} else {
					g *= 100.0D;
				}
				
				Text text = new TranslatableText(attribute.getTranslationKey());
				
				if(attribute instanceof SlotAttributes.SlotEntityAttribute) {
					text = new TranslatableText("trinkets.tooltip.attributes.slots", text);
				}
				
				if(g > 0.0D) {
					list.add(new TranslatableText("attribute.modifier.plus." + modifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(g) + p, text).formatted(Formatting.BLUE));
				} else if(g < 0.0D) {
					g *= -1.0D;
					list.add(new TranslatableText("attribute.modifier.take." + modifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(g) + p, text).formatted(Formatting.RED));
				}
			}
		}
		
		info.cancel();
	}
}
