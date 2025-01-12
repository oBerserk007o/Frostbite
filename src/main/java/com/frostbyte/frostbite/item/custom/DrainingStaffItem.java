package com.frostbyte.frostbite.item.custom;

import com.frostbyte.frostbite.component.ChargeData;
import com.frostbyte.frostbite.component.ModDataComponentTypes;
import com.frostbyte.frostbite.component.ModeData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class DrainingStaffItem extends Item {
    public int max_charge = 100;
    public String[] modes = {"drain", "blast", "ray", "shield", "heal"};
    public Integer[] costs = {0, 80, 100, 50, 70};
    public boolean used = false;

    public DrainingStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        checkComponentDataForNull(player.getItemInHand(interactionHand));

        int modeIndex = ArrayUtils.indexOf(modes, player.getItemInHand(interactionHand).get(ModDataComponentTypes.MODE.get()).mode());
        int charge = player.getItemInHand(interactionHand).get(ModDataComponentTypes.CHARGE.get()).charge();

        if (used) {
            if (player.isShiftKeyDown()) {
                modeIndex += 1;
                if (modeIndex == modes.length) modeIndex = 0;
                player.getItemInHand(interactionHand).set(ModDataComponentTypes.MODE.get(),
                        new ModeData(modes[modeIndex]));
            } else {
                switch (player.getItemInHand(interactionHand).get(ModDataComponentTypes.MODE.get()).mode()) {
                    case "drain":
                        if (charge < max_charge) reduceCharge(player, interactionHand, -10);;
                        break;
                    case "blast":
                        if (charge >= costs[1]) {
                            blastOption(player);
                            reduceCharge(player, interactionHand, costs[1]);
                        } else {
                            notifyPlayerNotEnoughCharge(player);
                        }
                        break;
                    case "ray":
                        if (charge >= costs[2]) {
                            rayOption(player);
                            reduceCharge(player, interactionHand, costs[2]);
                        } else {
                            notifyPlayerNotEnoughCharge(player);
                        }
                        break;
                    case "shield":
                        if (charge >= costs[3]) {
                            shieldOption(player);
                            reduceCharge(player, interactionHand, costs[3]);
                        } else {
                            notifyPlayerNotEnoughCharge(player);
                        }
                        break;
                    case "heal":
                        if (charge >= costs[4]) {
                            healOption(player);
                            reduceCharge(player, interactionHand, costs[4]);
                        } else {
                            notifyPlayerNotEnoughCharge(player);
                        }
                        break;
                }
            }
        }
        used = !used;
        return InteractionResult.SUCCESS;
    }


    public void blastOption(Player player) {
        player.displayClientMessage(Component.literal("Blast away!"), false);
    }

    public void rayOption(Player player) {
        player.displayClientMessage(Component.literal("Pew pew"), false);
    }

    public void shieldOption(Player player) {
        player.displayClientMessage(Component.literal("Shielding"), false);
    }

    public void healOption(Player player) {
        player.displayClientMessage(Component.literal("Healing"), false);
    }

    public void reduceCharge(Player player, InteractionHand hand, int amount) {
        player.getItemInHand(hand).set(ModDataComponentTypes.CHARGE.get(), new ChargeData(player.getItemInHand(hand).get(ModDataComponentTypes.CHARGE.get()).charge() - amount));
    }

    public void checkComponentDataForNull(ItemStack stack) {
        if (stack.get(ModDataComponentTypes.MODE.get()).mode() == null) {
            stack.set(ModDataComponentTypes.MODE.get(), new ModeData("drain"));
        }
//        if (stack.get(ModDataComponentTypes.CHARGE.get()).charge() == null) {
//            stack.set(ModDataComponentTypes.CHARGE.get(), new ChargeData(0));
//        }
    }

    public void notifyPlayerNotEnoughCharge(Player player) {
        player.playSound(SoundEvents.THORNS_HIT);
        player.displayClientMessage(Component.translatable("tooltip.frostbite.draining_staff.not_enough_charge"), false);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        checkComponentDataForNull(stack);
        tooltipComponents.add(Component.literal(String.format("Charge: %s  Mode: %s",
                stack.get(ModDataComponentTypes.CHARGE.get()).charge(), stack.get(ModDataComponentTypes.MODE.get()).mode())).withStyle(ChatFormatting.GRAY));
    }
}
