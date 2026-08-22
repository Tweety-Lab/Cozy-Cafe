package io.github.chakyl.cozycafe.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import javax.annotation.Nullable;

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

    // TODO: Find a way to merge renderFood and getFoodModel into just one public method?

    // Nullability here is a bit scuffed
    public static void renderFood(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, @Nullable BlockPos pos, Level level, int light, int overlay) {
        Minecraft minecraft = Minecraft.getInstance();

        if (stack.getItem() instanceof BlockItem blockItem) {
            if (pos == null) {
                return;
            }

            BlockState blockState = blockItem.getBlock().defaultBlockState();
            BakedModel model = minecraft.getBlockRenderer().getBlockModel(blockState);
            RenderType renderType = RenderType.solid();
            VertexConsumer consumer = buffer.getBuffer(renderType);

            poseStack.pushPose();
            poseStack.translate(-0.5D, 0.0D, -0.5D); // Block models have different coordinates, adjust

            minecraft.getBlockRenderer()
                    .getModelRenderer()
                    .tesselateBlock(
                            level,
                            model,
                            blockState,
                            pos,
                            poseStack,
                            consumer,
                            false,
                            RandomSource.create(),
                            42L,
                            overlay,
                            ModelData.EMPTY,
                            renderType
                    );

            poseStack.popPose();

        } else {
            minecraft.getItemRenderer().renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    light,
                    overlay,
                    poseStack,
                    buffer,
                    null,
                    0
            );
        }
    }

    public static BakedModel getFoodModel(ItemStack foodItem){
        if (foodItem.getItem() instanceof BlockItem blockItem) {
            BlockState foodBlockState = blockItem.getBlock().defaultBlockState();
            return Minecraft.getInstance().getBlockRenderer().getBlockModel(foodBlockState);
        }

        return Minecraft.getInstance().getItemRenderer().getModel(foodItem, null, null, 0);
    }
}
