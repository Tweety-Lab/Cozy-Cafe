package io.github.chakyl.cozycafe.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomerSkinUtils {
    private static final Map<UUID, CustomerSkinInfo> SKIN_CACHE = new HashMap<>();
    private static final CustomerSkinInfo SKIN_DEFAULT = new CustomerSkinInfo(true, DefaultPlayerSkin.getDefaultSkin());

    public static CustomerSkinInfo getCustomerSkinInfo(@Nullable GameProfile profile) {
        if (profile == null) {
            return SKIN_DEFAULT;
        }

        return SKIN_CACHE.computeIfAbsent(profile.getId(), id -> {
            SkinManager skinManager = Minecraft.getInstance().getSkinManager();
            MinecraftProfileTexture skin = skinManager.getInsecureSkinInformation(profile).get(MinecraftProfileTexture.Type.SKIN);

            boolean isSlim = skin != null && "slim".equals(skin.getMetadata("model"));
            ResourceLocation location = skinManager.getInsecureSkinLocation(profile);

            return new CustomerSkinInfo(isSlim, location);
        });
    }
}
