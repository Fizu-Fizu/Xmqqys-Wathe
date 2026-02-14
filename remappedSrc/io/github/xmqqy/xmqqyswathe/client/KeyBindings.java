package io.github.xmqqy.xmqqyswathe.client;

import io.github.xmqqy.xmqqyswathe.network.GrenadeSkillC2SPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class KeyBindings implements ClientModInitializer {
    private static KeyMapping abilityKey;

    @Override
    public void onInitializeClient() {
        abilityKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.noellesroles.ability",   // 翻译键
                GLFW.GLFW_KEY_G,               // 默认 G
                "key.category.xmqqyswathe"     // 分类，可自定义
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (abilityKey.consumeClick()) {
                onAbilityPressed(client);
            }
        });
    }

    private void onAbilityPressed(Minecraft client) {
        if (client.player == null || client.level == null) return;

        // 准星对准的实体必须是玩家
        if (client.hitResult instanceof EntityHitResult hit && hit.getEntity() instanceof Player target) {
            // 发送 C2S 包，包含目标 UUID
            GrenadeSkillC2SPacket.send(target.getUUID());
        } else {
            // 没有对准玩家，提示（可选）
            if (client.player != null) {
                client.player.displayClientMessage(
                        net.minecraft.network.chat.Component.translatable("message.xmqqyswathe.skill.no_target"),
                        true
                );
            }
        }
    }
}