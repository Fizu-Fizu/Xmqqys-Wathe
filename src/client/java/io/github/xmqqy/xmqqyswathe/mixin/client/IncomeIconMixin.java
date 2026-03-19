package io.github.xmqqy.xmqqyswathe.mixin.client;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import dev.doctor4t.wathe.client.gui.StoreRenderer;
import io.github.xmqqy.xmqqyswathe.ModRoles;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;

@Mixin(StoreRenderer.class)
public abstract class IncomeIconMixin {

    @Shadow public static float offsetDelta;
    @Shadow public static StoreRenderer.MoneyNumberRenderer view;

    @Inject(method = "renderHud", at = @At("HEAD"))
    private static void onRenderHud(Font font, LocalPlayer player, GuiGraphics graphics, float delta, CallbackInfo ci) {
        if (player.isAlive() && !player.isSpectator()) {
            GameWorldComponent gameWorld = GameWorldComponent.KEY.get(player.level());
            if (gameWorld.isRole(player, ModRoles.WARDEN)) {
                int balance = PlayerShopComponent.KEY.get(player).balance;

                // 检测余额变化
                if (view.getTarget() != (float) balance) {
                    offsetDelta = balance > view.getTarget() ? 0.6F : -0.6F;
                    view.setTarget(balance);
                }

                // 计算颜色
                float r = offsetDelta > 0.0F ? 1.0F - offsetDelta : 1.0F;
                float g = offsetDelta < 0.0F ? 1.0F + offsetDelta : 1.0F;
                float b = 1.0F - Mth.abs(offsetDelta);
                int colour = Mth.color(r, g, b) | 0xFF000000;

                // 渲染在屏幕右上角
                graphics.pose().pushPose();
                graphics.pose().translate(graphics.guiWidth() - 12, 6.0F, 0.0F);
                view.render(font, graphics, 0, 0, colour, delta);
                graphics.pose().popPose();

                // 滚动效果
                offsetDelta = Mth.lerp(delta / 16.0F, offsetDelta, 0.0F);
            }
        }
    }
}