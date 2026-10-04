package com.any.mikuplushie.item;

import com.any.mikuplushie.MikuPlushie;
import com.any.mikuplushie.registry.ModBlocks;
import com.any.mikuplushie.util.ModUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class MikuPlushieBlockItem extends BlockItem implements Equipable {

	public MikuPlushieBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		String itemName = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
		tooltip.add(Component.translatable("item." + MikuPlushie.MOD_ID + "." + itemName + ".tooltip"));
		super.appendHoverText(stack, context, tooltip, flag);
	}

	public static void PlayMikuSound(LivingEntity entity){
		ItemStack stack = entity.getItemInHand(entity.getUsedItemHand());

		if (stack.getItem() instanceof MikuPlushieBlockItem){
			String currentPlush = ModUtil.getBlockIdFromItem(stack.getItem());
			ModUtil.playPlushSound(entity.level(), entity.blockPosition(), currentPlush, "dor");
		}
	}

	@Override
	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.HEAD;
	}

	@Override
	public Holder<SoundEvent> getEquipSound() {
		ItemStack stack = this.getDefaultInstance();

		if (stack.getItem() instanceof MikuPlushieBlockItem) {
			if (!stack.is(ModBlocks.KONOHA_PLUSH.get().asItem())){
				String currentPlush = ModUtil.getBlockIdFromItem(stack.getItem());
				return Holder.direct(ModUtil.getPlushSoundEvent(currentPlush, "equip"));
			}
		}
		return Holder.direct(SoundEvents.WOOL_PLACE);
	}
}