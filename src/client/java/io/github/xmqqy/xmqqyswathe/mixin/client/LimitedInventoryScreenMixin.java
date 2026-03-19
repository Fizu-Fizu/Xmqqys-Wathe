package io.github.xmqqy.xmqqyswathe.mixin.client;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.client.gui.screen.ingame.LimitedHandledScreen;
import dev.doctor4t.wathe.client.gui.screen.ingame.LimitedInventoryScreen;
import dev.doctor4t.wathe.util.ShopEntry;
import io.github.xmqqy.xmqqyswathe.ModRoles;
import io.github.xmqqy.xmqqyswathe.client.BomberClientShop;
import io.github.xmqqy.xmqqyswathe.client.WardenClientShop;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LimitedInventoryScreen.class)
public abstract class LimitedInventoryScreenMixin extends LimitedHandledScreen<InventoryMenu> {

    // 必需的构造函数（仅供编译）
    public LimitedInventoryScreenMixin(InventoryMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Shadow
    @Final
    public LocalPlayer player;

    /**
     * BOMBER 角色使用替换商店列表的方式（原第一个 Mixin）
     */
    @ModifyVariable(method = "init", at = @At(value = "STORE"), name = "entries")
    private List<ShopEntry> replaceShopEntries(List<ShopEntry> entries) {
        GameWorldComponent gameWorld = GameWorldComponent.KEY.get(player.level());
        if (gameWorld.isRole(player, ModRoles.BOMBER)) {
            return BomberClientShop.createEntries(); // BOMBER 专属商店
        }
        return entries; // 其他角色保持原列表
    }

    /**
     * WARDEN 角色使用额外添加商店按钮的方式（原第二个 Mixin，已移除 BOMBER 判断）
     */
    @Inject(method = "init", at = @At("HEAD"))
    private void onInit(CallbackInfo ci) {
        GameWorldComponent gameWorld = GameWorldComponent.KEY.get(player.level());
        if (gameWorld.isRole(player, ModRoles.WARDEN)) {
            addShopEntries(WardenClientShop.createEntries()); // WARDEN 专属商店
        }
    }

    /**
     * 辅助方法：在屏幕指定位置添加商店按钮
     */
    private void addShopEntries(List<ShopEntry> entries) {
        if (entries.isEmpty()) return;
        int apart = 36;
        int x = width / 2 - entries.size() * apart / 2 + 9;
        int y = (height - 32) / 2 - 46;
        for (int i = 0; i < entries.size(); i++) {
            addRenderableWidget(new LimitedInventoryScreen.StoreItemWidget(
                    (LimitedInventoryScreen) (Object) this,
                    x + apart * i,
                    y,
                    entries.get(i),
                    i
            ));
        }
    }
}