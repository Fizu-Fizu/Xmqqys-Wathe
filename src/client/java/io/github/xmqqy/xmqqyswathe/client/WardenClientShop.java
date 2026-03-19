package io.github.xmqqy.xmqqyswathe.client;

import java.util.ArrayList;
import java.util.List;

import dev.doctor4t.wathe.util.ShopEntry;
import io.github.xmqqy.xmqqyswathe.registry.ModItems;

public class WardenClientShop {
    public static List<ShopEntry> createEntries() {
        List<ShopEntry> entries = new ArrayList<>();
        // 手铐，价格 50
        entries.add(new ShopEntry(ModItems.HANDCUFF.getDefaultInstance(), 150, ShopEntry.Type.TOOL));
        return entries;
    }
}