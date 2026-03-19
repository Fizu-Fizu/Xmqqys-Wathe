package io.github.xmqqy.xmqqyswathe.mixin.role.warden;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import dev.doctor4t.wathe.index.WatheSounds;
import io.github.xmqqy.xmqqyswathe.ModRoles;
import io.github.xmqqy.xmqqyswathe.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerShopComponent.class)
public abstract class WardenShopMixin {

    @Shadow public int balance;
    @Shadow @Final private Player player;
    @Shadow public abstract void sync();

    @Inject(method = "tryBuy", at = @At("HEAD"), cancellable = true)
    void onTryBuy(int index, CallbackInfo ci) {
        GameWorldComponent gameWorld = GameWorldComponent.KEY.get(player.level());
        if (!gameWorld.isRole(player, ModRoles.WARDEN)) return;

        if (index == 0) {
            int price = 150;
            if (balance >= price) {
                balance -= price;
                sync();
                player.getInventory().add(ModItems.HANDCUFF.getDefaultInstance());
                playSound(true);
            } else {
                player.displayClientMessage(Component.literal("余额不足").withStyle(ChatFormatting.DARK_RED), true);
                playSound(false);
            }
            ci.cancel();
        }
    }

    private void playSound(boolean success) {
        if (!(player instanceof ServerPlayer sp)) return;
        var sound = success ? WatheSounds.UI_SHOP_BUY : WatheSounds.UI_SHOP_BUY_FAIL;
        sp.connection.send(new ClientboundSoundPacket(
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound),
                SoundSource.PLAYERS,
                player.getX(), player.getY(), player.getZ(),
                1.0F, 0.9F + player.getRandom().nextFloat() * 0.2F,
                player.getRandom().nextLong()
        ));
    }
}