package io.github.xmqqy.xmqqyswathe.registry;

import io.github.xmqqy.xmqqyswathe.XmqqysWathe;
import io.github.xmqqy.xmqqyswathe.component.BombComponents;
import io.github.xmqqy.xmqqyswathe.item.BombItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModItems {
    public static final BombItem BOMB = new BombItem(new Item.Properties()
    .stacksTo(1)
    .component(BombComponents.BOMB_TIME, 20 * 20)   // 400 ticks
    .component(BombComponents.COOLDOWN, 20)         // 20 ticks
    );
    

    // Wathe 模组的装备组资源键
    private static final ResourceKey<CreativeModeTab> EQUIPMENT_GROUP = 
        ResourceKey.create(Registries.CREATIVE_MODE_TAB, 
            ResourceLocation.fromNamespaceAndPath("wathe", "equipment"));

    public static void init() {
        // 1. 注册物品
        Registry.register(BuiltInRegistries.ITEM, XmqqysWathe.id("bomb"), BOMB);

        // 2. 把炸弹放入 Wathe 的装备组
        ItemGroupEvents.modifyEntriesEvent(EQUIPMENT_GROUP).register(entries -> {
        entries.prepend(new ItemStack(ModItems.BOMB));
        });
    }
}