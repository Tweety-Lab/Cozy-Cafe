package io.github.chakyl.cozycafe.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GeneralUtils {
    public static int getDay(Level level) {
        return (int) (Math.floor((double) level.dayTime() / 24000) + 1);
    }

    public static String formatPrice(int number) {
        return formatPrice(String.valueOf(number), true);
    }

    public static String formatPrice(String number, boolean truncateMillionsBillions) {
        if (truncateMillionsBillions) {
            if (number.length() < 4) return number;
            if (number.length() > 9) return number.charAt(0) + "." + number.charAt(1) + "B";
            if (number.length() > 6) {
                StringBuilder out = new StringBuilder(3);
                for (int i = 0; i < number.length() - 6; i++) {
                    out.append(number.charAt(i));
                }
                if (number.length() == 7) {
                    out.append('.');
                    out.append(number.charAt(1));
                }
                out.append("M");
                return out.toString();
            }
        }
        int start = number.length() % 3;
        StringBuilder out = new StringBuilder(number.length() + (number.length() / 3));
        out.append(number, 0, start);
        for (int i = 0; i < number.length() / 3; i++) {
            if (i != 0 || start != 0) out.append(",");
            out.append(number, i * 3 + start, i * 3 + start + 3);
        }
        return out.toString();
    }

    public static BakedModel getFoodModel(BlockState blockState, ItemStack foodItem) {
        if (foodItem.getItem() instanceof BlockItem blockItem) {
            BlockState foodBlockState = blockItem.getBlock().defaultBlockState();
            return Minecraft.getInstance().getBlockRenderer().getBlockModel(foodBlockState);
        }

        return Minecraft.getInstance().getItemRenderer().getModel(foodItem, null, null, 0);
    }
}
