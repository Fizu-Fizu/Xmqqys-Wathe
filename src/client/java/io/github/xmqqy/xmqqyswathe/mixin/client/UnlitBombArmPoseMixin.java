package io.github.xmqqy.xmqqyswathe.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.xmqqy.xmqqyswathe.registry.ModItems;
import net.minecraft.client.player.AbstractClientPlayer; 
import net.minecraft.client.renderer.entity.player.PlayerRenderer; 
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerRenderer.class) 
public class UnlitBombArmPoseMixin {

    @WrapOperation(
            method = "getArmPose", 
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;")
    )
    private static ItemStack hideUnlitBomb(AbstractClientPlayer player, InteractionHand hand, Operation<ItemStack> original) {
        ItemStack stack = original.call(player, hand);
        if (stack.is(ModItems.UNLITBOMB)) { 
            return ItemStack.EMPTY;
        }
        return stack;
    }
}