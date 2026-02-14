package io.github.xmqqy.xmqqyswathe.item;

import java.util.UUID;

import io.github.xmqqy.xmqqyswathe.component.BombComponents;
import io.github.xmqqy.xmqqyswathe.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import java.util.Optional;

public class BombItem extends Item {
    public BombItem(Properties properties) {
        super(properties);
    }

    // 炸弹时间
    public static void setBombTime(ItemStack stack, int time) {
        stack.set(BombComponents.BOMB_TIME, time);
    }

    public static int getBombTime(ItemStack stack) {
        return stack.getOrDefault(BombComponents.BOMB_TIME, -1);
    }

    // 冷却
    public static void setCooldown(ItemStack stack, int cooldown) {
        stack.set(BombComponents.COOLDOWN, cooldown);
    }

    public static int getCooldown(ItemStack stack) {
        return stack.getOrDefault(BombComponents.COOLDOWN, 0);
    }

    // 玩家
    public static void setSource(ItemStack stack, UUID source) {
    stack.set(BombComponents.SOURCE, Optional.ofNullable(source).map(UUID::toString));
    }

    public static UUID getSource(ItemStack stack) {
    Optional<String> sourceStr = stack.getOrDefault(BombComponents.SOURCE, Optional.empty());
        if (sourceStr.isPresent()) {
            try {
                return UUID.fromString(sourceStr.get());
            } catch (IllegalArgumentException e) {
            }
        }
        return null;
    }

    // 创建初始炸弹
    public static ItemStack createBomb() {
        ItemStack stack = new ItemStack(ModItems.BOMB);
        setBombTime(stack, 20 * 20); // 400刻
        setCooldown(stack, 20);// 20刻
        return stack;
    }

    @Override
    public Component getName(ItemStack stack) {
        int time = getBombTime(stack);
        if (time >= 0) {
            double seconds = time / 20.0;

            return Component.translatable("item.xmqqyswathe.bomb")
                    .append(Component.literal(" (" + String.format("%.1f", seconds) + "s)"));
        }
        return super.getName(stack);
    }
    
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target,
            InteractionHand hand) {
        // 忽略客户端
        Level level = player.level();
        if (level.isClientSide) {
            return InteractionResult.PASS;
        }
        // 是玩家，不是自己
        if (!(target instanceof ServerPlayer targetPlayer)) return InteractionResult.PASS;
        if (targetPlayer.equals(player)) {
            player.displayClientMessage(Component.translatable("message.xmqqyswathe.bomb.no_self"), true);
            return InteractionResult.FAIL;
        }
        // 冷却
        int cooldown = getCooldown(stack);
        if (cooldown > 0) {
            player.displayClientMessage(Component.translatable("message.xmqqyswathe.bomb.cooldown", cooldown / 20.0), true);
            return InteractionResult.FAIL;
        }
        // 复制组件，并放入目标身上
        ItemStack bombForTarget = stack.copy();
        setCooldown(bombForTarget, 20);
        if (!targetPlayer.getInventory().add(bombForTarget)) {
            player.displayClientMessage(Component.translatable("message.xmqqyswathe.bomb.full_inventory"), true);
            return InteractionResult.FAIL;
        }
        stack.shrink(1);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
        SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0f, 1.0f);
        player.displayClientMessage(Component.translatable("message.xmqqyswathe.bomb.transferred", targetPlayer.getName()), true);
        return InteractionResult.SUCCESS;
    }
}