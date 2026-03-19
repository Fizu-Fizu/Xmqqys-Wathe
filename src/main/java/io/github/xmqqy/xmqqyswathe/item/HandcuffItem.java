package io.github.xmqqy.xmqqyswathe.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HandcuffItem extends Item {
    public HandcuffItem(Properties properties) {
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

        // 消耗一个手铐
        stack.shrink(1);

        // 查找所有符合条件的盔甲架位置（名称以 prison1-6 结尾）
        List<Vec3> prisonPositions = new ArrayList<>();
        for (Entity entity : serverLevel.getAllEntities()) {
            if (entity.getType() == EntityType.ARMOR_STAND) {
                Component customName = entity.getCustomName();
                if (customName != null) {
                    String name = customName.getString();
                    // 匹配 prison1 至 prison6（精确匹配）
                    if (name.equals("prison1") || name.equals("prison2") || name.equals("prison3") ||
                        name.equals("prison4") || name.equals("prison5") || name.equals("prison6")) {
                        prisonPositions.add(entity.position());
                    }
                }
            }
        }

        // 如果没有牢房可用，返回失败（不消耗物品）
        if (prisonPositions.isEmpty()) {
            return InteractionResult.FAIL;
        }

        // 随机选择一个牢房位置
        Random random = new Random();
        Vec3 selectedPos = prisonPositions.get(random.nextInt(prisonPositions.size()));

        // 消耗一个手铐
        stack.shrink(1);

        // 传送目标玩家到该位置（保持玩家原有朝向）
        targetPlayer.teleportTo(serverLevel, selectedPos.x, selectedPos.y, selectedPos.z,
                targetPlayer.getYRot(), targetPlayer.getXRot());
        
        player.displayClientMessage(Component.translatable("message.xmqqyswathe.handcuff.hold", targetPlayer.getName()), true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.xmqqyswathe.handcuff");
    }
}