package io.github.xmqqy.xmqqyswathe.mixin.role.bomber;

import dev.doctor4t.wathe.cca.PlayerShopComponent;
import dev.doctor4t.wathe.index.WatheItems;
import dev.doctor4t.wathe.util.ShopEntry;
import io.github.xmqqy.xmqqyswathe.registry.ModItems;
import net.minecraft.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BomberShop {
    public static final List<ShopEntry> CUSTOM_ROLE_SHOP = Util.make(new ArrayList<>(), entries -> {
        // 1. 炸弹
        entries.add(new ShopEntry(ModItems.UNLITBOMB.getDefaultInstance(), 75, ShopEntry.Type.WEAPON));

        // 2. 刀
        entries.add(new ShopEntry(WatheItems.KNIFE.getDefaultInstance(), 200, ShopEntry.Type.WEAPON));

        // 3. 手雷
        entries.add(new ShopEntry(WatheItems.GRENADE.getDefaultInstance(), 250, ShopEntry.Type.WEAPON));

        // 4. 疯狂模式
        entries.add(new ShopEntry(WatheItems.PSYCHO_MODE.getDefaultInstance(), 300, ShopEntry.Type.TOOL) {
            @Override
            public boolean onBuy(@NotNull Player player) {
                player.getCooldowns().addCooldown(WatheItems.PSYCHO_MODE, 200);
                return PlayerShopComponent.usePsychoMode(player);
            }
        });

        // 5. 毒药
        entries.add(new ShopEntry(WatheItems.POISON_VIAL.getDefaultInstance(), 100, ShopEntry.Type.POISON));

        // 6. 蝎子
        entries.add(new ShopEntry(WatheItems.SCORPION.getDefaultInstance(), 50, ShopEntry.Type.POISON));

        // 7. 鞭炮
        entries.add(new ShopEntry(WatheItems.FIRECRACKER.getDefaultInstance(), 10, ShopEntry.Type.TOOL));

        // 8. 开锁器
        entries.add(new ShopEntry(WatheItems.LOCKPICK.getDefaultInstance(), 50, ShopEntry.Type.TOOL));

        // 9. 撬棍
        entries.add(new ShopEntry(WatheItems.CROWBAR.getDefaultInstance(), 25, ShopEntry.Type.TOOL));

        // 10. 裹尸袋
        entries.add(new ShopEntry(WatheItems.BODY_BAG.getDefaultInstance(), 100, ShopEntry.Type.TOOL));

        // 11. 关灯
        entries.add(new ShopEntry(WatheItems.BLACKOUT.getDefaultInstance(), 200, ShopEntry.Type.TOOL) {
            @Override
            public boolean onBuy(@NotNull Player player) {
                player.getCooldowns().addCooldown(WatheItems.BLACKOUT, 200);
                return PlayerShopComponent.useBlackout(player);
            }
        });

        // 12. 笔记（4张）
        entries.add(new ShopEntry(new ItemStack(WatheItems.NOTE, 4), 10, ShopEntry.Type.TOOL));
    });
}