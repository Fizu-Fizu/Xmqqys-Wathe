package io.github.xmqqy.xmqqyswathe.item;

import io.github.xmqqy.xmqqyswathe.XmqqysWathe;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class UnlitBombItem extends Item {
    public UnlitBombItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target,
            net.minecraft.world.InteractionHand hand) {
        // 仅在服务端执行
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return InteractionResult.PASS;
        }
        if (!(target instanceof ServerPlayer targetPlayer)) {
            return InteractionResult.PASS; // 只能对玩家使用
        }

        // 消耗一个未点燃炸弹
        stack.shrink(1);

        // 5秒后给目标一个炸弹
        XmqqysWathe.SCHEDULER.schedule(() -> {
            if (targetPlayer.isAlive() && targetPlayer.level() == serverLevel) {
                ItemStack litBomb = BombItem.createBomb(); // 炸弹
                if (targetPlayer.getInventory().add(litBomb)) {
                    player.level().playSound(null, targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ(), SoundEvents.TNT_PRIMED, SoundSource.PLAYERS, 1.0f, 1.0f);
                } else {
                    targetPlayer.drop(litBomb, false);
                }
            }
        }, 5 * 20);

        return InteractionResult.SUCCESS;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.xmqqyswathe.unlit_bomb");
    }
}