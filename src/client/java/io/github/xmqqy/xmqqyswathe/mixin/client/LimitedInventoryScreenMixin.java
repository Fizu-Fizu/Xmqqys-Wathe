package io.github.xmqqy.xmqqyswathe.mixin.client;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.client.gui.screen.ingame.LimitedInventoryScreen;
import dev.doctor4t.wathe.util.ShopEntry;
import io.github.xmqqy.xmqqyswathe.ModRoles;
import io.github.xmqqy.xmqqyswathe.client.BomberClientShop;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(LimitedInventoryScreen.class)
public abstract class LimitedInventoryScreenMixin {

    @Shadow
    @Final
    public LocalPlayer player;

    @ModifyVariable(method = "init", at = @At(value = "STORE"), name = "entries")
    private List<ShopEntry> replaceShopEntries(List<ShopEntry> entries) {
        GameWorldComponent gameWorld = GameWorldComponent.KEY.get(player.level());
        if (gameWorld.isRole(player, ModRoles.BOMBER)) {
            return BomberClientShop.createEntries();
        }
        return entries;
    }
}