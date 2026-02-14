package io.github.xmqqy.xmqqyswathe.mixin.role.bomber;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import dev.doctor4t.wathe.index.WatheItems;
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
import net.minecraft.world.item.ItemStack;

@Mixin(PlayerShopComponent.class)
public abstract class BomberShopMixin {

    @Shadow public int balance;
    @Shadow @Final private Player player;
    @Shadow public abstract void sync();

    @Inject(method = "tryBuy", at = @At("HEAD"), cancellable = true)
    void onTryBuy(int index, CallbackInfo ci) {
        GameWorldComponent gameWorld = GameWorldComponent.KEY.get(player.level());
        // 只对轰炸机角色生效
        if (!gameWorld.isRole(player, ModRoles.BOMBER)) return;

        // 根据索引处理购买
        switch (index) {
            case 0: // 炸弹
                buyItem(ModItems.UNLITBOMB.getDefaultInstance(), 75);
                break;
            case 1: // 刀
                buyItem(WatheItems.KNIFE.getDefaultInstance(), 200);
                break;
            case 2: // 手雷（用炸弹代替）
                buyItem(WatheItems.GRENADE.getDefaultInstance(), 250);
                break;
            case 3: // 疯狂模式
                buyPsychoMode(300);
                break;
            case 4: // 毒药
                buyItem(WatheItems.POISON_VIAL.getDefaultInstance(), 100);
                break;
            case 5: // 蝎子
                buyItem(WatheItems.SCORPION.getDefaultInstance(), 50);
                break;
            case 6: // 鞭炮
                buyItem(WatheItems.FIRECRACKER.getDefaultInstance(), 10);
                break;
            case 7: // 开锁器
                buyItem(WatheItems.LOCKPICK.getDefaultInstance(), 50);
                break;
            case 8: // 撬棍
                buyItem(WatheItems.CROWBAR.getDefaultInstance(), 25);
                break;
            case 9: // 裹尸袋
                buyItem(WatheItems.BODY_BAG.getDefaultInstance(), 100);
                break;
            case 10: // 关灯
                buyBlackout(200);
                break;
            case 11: // 笔记（4张）
                buyItem(new ItemStack(WatheItems.NOTE, 4), 10);
                break;
            default:
                player.displayClientMessage(Component.literal("无效的商品").withStyle(ChatFormatting.RED), true);
                ci.cancel();
                return;
        }
        ci.cancel();
    }

    // 购买普通物品
    private void buyItem(ItemStack stack, int price) {
        if (balance < price) {
            failPurchase("余额不足");
            return;
        }
        if (stack.getItem() == ModItems.UNLITBOMB) {
            if (player.getCooldowns().isOnCooldown(ModItems.UNLITBOMB)) {
                failPurchase("冷却中,冷却时间30s");
                return;
            }
        }
        if (!player.getInventory().add(stack.copy())) {
            failPurchase("背包已满");
            return;
        }
        if (stack.getItem() == ModItems.UNLITBOMB) {
        player.getCooldowns().addCooldown(stack.getItem(), 600);
        }
        balance -= price;
        sync();
        playSound(true);
    }

    // 购买疯狂模式
    private void buyPsychoMode(int price) {
        if (balance < price) {
            failPurchase("余额不足");
            return;
        }
        if (player.getCooldowns().isOnCooldown(WatheItems.PSYCHO_MODE)) {
            failPurchase("疯狂模式冷却中");
            return;
        }
        if (!PlayerShopComponent.usePsychoMode(player)) {
            failPurchase("疯狂模式启动失败");
            return;
        }
        player.getCooldowns().addCooldown(WatheItems.PSYCHO_MODE, 2400);
        balance -= price;
        sync();
        playSound(true);
    }

    // 购买关灯
    private void buyBlackout(int price) {
        if (balance < price) {
            failPurchase("余额不足");
            return;
        }
        if (player.getCooldowns().isOnCooldown(WatheItems.BLACKOUT)) {
            failPurchase("关灯冷却中");
            return;
        }
        if (!PlayerShopComponent.useBlackout(player)) {
            failPurchase("关灯失败");
            return;
        }
        player.getCooldowns().addCooldown(WatheItems.BLACKOUT, 2400);
        balance -= price;
        sync();
        playSound(true);
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

    private void failPurchase(String reason) {
        player.displayClientMessage(Component.literal("购买失败: " + reason).withStyle(ChatFormatting.DARK_RED), true);
        playSound(false);
    }
}