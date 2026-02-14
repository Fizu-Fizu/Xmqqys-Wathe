package io.github.xmqqy.xmqqyswathe.client;

import dev.doctor4t.wathe.index.WatheItems;
import dev.doctor4t.wathe.util.ShopEntry;
import io.github.xmqqy.xmqqyswathe.registry.ModItems;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BomberClientShop {
    public static List<ShopEntry> createEntries() {
        List<ShopEntry> entries = new ArrayList<>();
        entries.add(new ShopEntry(ModItems.UNLITBOMB.getDefaultInstance(), 75, ShopEntry.Type.WEAPON));
        entries.add(new ShopEntry(WatheItems.KNIFE.getDefaultInstance(), 200, ShopEntry.Type.WEAPON));
        entries.add(new ShopEntry(WatheItems.GRENADE.getDefaultInstance(), 250, ShopEntry.Type.WEAPON));
        entries.add(new ShopEntry(WatheItems.PSYCHO_MODE.getDefaultInstance(), 300, ShopEntry.Type.TOOL));
        entries.add(new ShopEntry(WatheItems.POISON_VIAL.getDefaultInstance(), 100, ShopEntry.Type.POISON));
        entries.add(new ShopEntry(WatheItems.SCORPION.getDefaultInstance(), 50, ShopEntry.Type.POISON));
        entries.add(new ShopEntry(WatheItems.FIRECRACKER.getDefaultInstance(), 10, ShopEntry.Type.TOOL));
        entries.add(new ShopEntry(WatheItems.LOCKPICK.getDefaultInstance(), 50, ShopEntry.Type.TOOL));
        entries.add(new ShopEntry(WatheItems.CROWBAR.getDefaultInstance(), 25, ShopEntry.Type.TOOL));
        entries.add(new ShopEntry(WatheItems.BODY_BAG.getDefaultInstance(), 100, ShopEntry.Type.TOOL));
        entries.add(new ShopEntry(WatheItems.BLACKOUT.getDefaultInstance(), 200, ShopEntry.Type.TOOL));
        entries.add(new ShopEntry(new ItemStack(WatheItems.NOTE, 4), 10, ShopEntry.Type.TOOL));
        return entries;
    }
}