package io.github.xmqqy.xmqqyswathe.mixin.role.bomber;

import java.nio.file.Paths;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import dev.doctor4t.wathe.index.WatheSounds;
import dev.doctor4t.wathe.util.ShopEntry;
import io.github.xmqqy.xmqqyswathe.ModRoles;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


@Mixin(PlayerShopComponent.class)
public abstract class BomberShopMixin {

    @Shadow
    public int balance;

    @Shadow
    @Final
    private Player player;

    @Shadow
    public abstract void sync();

    @Inject(method = "tryBuy", at = @At("HEAD"), cancellable = true)
    private void onTryBuy(int index, CallbackInfo ci) {
        //GameWorldComponent gameWorld = GameWorldComponent.KEY.get(player.level());

        if (true) {// gameWorld.isRole(player, ModRoles.BOMBER)
            player.getInventory().add(new ItemStack(net.minecraft.world.item.Items.DIAMOND));
            if (index >= 0 && index < BomberShop.CUSTOM_ROLE_SHOP.size()) {
                ShopEntry entry = BomberShop.CUSTOM_ROLE_SHOP.get(index);

                // 检查余额并尝试购买
                if (balance >= entry.price() && entry.onBuy(player)) {
                    balance -= entry.price();
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundSoundPacket(
                                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(WatheSounds.UI_SHOP_BUY),
                                SoundSource.PLAYERS,
                                player.getX(), player.getY(), player.getZ(),
                                1.0F, 0.9F + player.getRandom().nextFloat() * 0.2F,
                                player.getRandom().nextLong()
                        ));
                    }
                } else {
                    player.displayClientMessage(Component.literal("Purchase Failed").withStyle(ChatFormatting.DARK_RED), true);
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundSoundPacket(
                                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(WatheSounds.UI_SHOP_BUY_FAIL),
                                SoundSource.PLAYERS,
                                player.getX(), player.getY(), player.getZ(),
                                1.0F, 0.9F + player.getRandom().nextFloat() * 0.2F,
                                player.getRandom().nextLong()
                        ));
                    }
                }
                sync();
            }
            ci.cancel();
        }
    }
}