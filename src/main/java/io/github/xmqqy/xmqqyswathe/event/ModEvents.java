package io.github.xmqqy.xmqqyswathe.event;

import java.util.UUID;

import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.game.GameFunctions;
import io.github.xmqqy.xmqqyswathe.item.BombItem;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ModEvents {
    public static void init() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (Player player : world.players()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    updateBombsInInventory(serverPlayer);
                }
            }
        });
    }

    private static void updateBombsInInventory(ServerPlayer player) {
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!(stack.getItem() instanceof BombItem)) continue;

            // 1. 倒计时减 1
            int bombTime = BombItem.getBombTime(stack);
            if (bombTime > 0) {
                BombItem.setBombTime(stack, bombTime - 1);
            }

            // 2. 冷却时间减 1
            int cooldown = BombItem.getCooldown(stack);
            if (cooldown > 0) {
                BombItem.setCooldown(stack, cooldown - 1);
            }

            // 3. 时间 ≤ 0 → 爆炸
            if (bombTime <= 1) {
                inv.setItem(i, ItemStack.EMPTY);

                // 播放爆炸音效
                player.level().playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.GENERIC_EXPLODE,
                    SoundSource.PLAYERS,
                    1.0f, 1.0f
                );
                // 获取击杀者
                UUID sourceUUID = BombItem.getSource(stack);
                ServerPlayer killer = null;
                if (sourceUUID != null) {
                    killer = player.getServer().getPlayerList().getPlayer(sourceUUID);
                }
                if (killer == null) {
                    killer = player;
                }
                
                // 调用 Wathe 的死亡方法
                GameFunctions.killPlayer(
                    player,
                    true,         
                    killer,
                    GameConstants.DeathReasons.GRENADE
                );

                // 发送系统消息
                continue;
            }

            // 4. ActionBar 显示剩余时间/冷却
            if (bombTime > 0) {
                int cd = BombItem.getCooldown(stack);
                double secondstime = bombTime / 20.0;
                double cooldowntime = cd / 20.0;
                Component message;
                if (cd > 0) {
                    message = Component.translatable("message.xmqqyswathe.bomb.status.cooldown",
                            String.format("%.1f", secondstime), String.format("%.1f", cooldowntime));
                } else {
                    message = Component.translatable("message.xmqqyswathe.bomb.status.armed",
                            String.format("%.1f", secondstime));
                }
                player.displayClientMessage(message, true);
            }
        }
    }
}