package io.github.xmqqy.xmqqyswathe.registry;

import io.github.xmqqy.xmqqyswathe.XmqqysWathe;
import io.github.xmqqy.xmqqyswathe.component.BombComponents;
import io.github.xmqqy.xmqqyswathe.item.BombItem;
import io.github.xmqqy.xmqqyswathe.item.UnlitBombItem;
import io.github.xmqqy.xmqqyswathe.item.HandcuffItem;
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
    // Bombs
    public static final BombItem BOMB = new BombItem(new Item.Properties()
    .stacksTo(1)
    .component(BombComponents.BOMB_TIME, 20 * 20)   // 400 ticks
    .component(BombComponents.COOLDOWN, 20)         // 20 ticks
    );

    public static final UnlitBombItem UNLITBOMB = new UnlitBombItem(new Item.Properties()
    .stacksTo(1)
    );

    // handcuff
    public static final HandcuffItem HANDCUFF = new HandcuffItem(new Item.Properties()
    .stacksTo(1)
    );

    // Wathe 模组的装备组资源键
    private static final ResourceKey<CreativeModeTab> EQUIPMENT_GROUP = 
        ResourceKey.create(Registries.CREATIVE_MODE_TAB, 
            ResourceLocation.fromNamespaceAndPath("wathe", "equipment"));

    public static void init() {
        // 1. 注册物品
        Registry.register(BuiltInRegistries.ITEM, XmqqysWathe.id("bomb"), BOMB);
        Registry.register(BuiltInRegistries.ITEM, XmqqysWathe.id("unlitbomb"), UNLITBOMB);
        Registry.register(BuiltInRegistries.ITEM, XmqqysWathe.id("handcuff"), HANDCUFF);

        // 2. 把炸弹放入 Wathe 的装备组
        ItemGroupEvents.modifyEntriesEvent(EQUIPMENT_GROUP).register(entries -> {
        entries.prepend(new ItemStack(ModItems.BOMB));
        });
        ItemGroupEvents.modifyEntriesEvent(EQUIPMENT_GROUP).register(entries -> {
        entries.prepend(new ItemStack(ModItems.UNLITBOMB));
        });
        ItemGroupEvents.modifyEntriesEvent(EQUIPMENT_GROUP).register(entries -> {
        entries.prepend(new ItemStack(ModItems.HANDCUFF));
        });
    }
}