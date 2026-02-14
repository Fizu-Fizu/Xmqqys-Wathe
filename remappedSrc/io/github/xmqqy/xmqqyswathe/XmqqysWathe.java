package io.github.xmqqy.xmqqyswathe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.xmqqy.xmqqyswathe.component.BombComponents;
import io.github.xmqqy.xmqqyswathe.event.ModEvents;
import io.github.xmqqy.xmqqyswathe.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public class XmqqysWathe implements ModInitializer {
	public static final String MOD_ID = "xmqqyswathe";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing XmqqysWathe mod...");
		//初始化
		BombComponents.init();
		// 初始化物品
		ModEvents.init();
		ModItems.init();
		// 初始化角色
		ModRoles.init(); 
    	LOGGER.info("XmqqysWathe mod initialization completed!");
	}	
	public static ResourceLocation id(String key) {
	    return ResourceLocation.fromNamespaceAndPath(MOD_ID, key);   // 直接构造
	}
}