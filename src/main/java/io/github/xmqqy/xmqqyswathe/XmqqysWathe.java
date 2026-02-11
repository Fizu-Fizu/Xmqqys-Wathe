package io.github.xmqqy.xmqqyswathe;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmqqysWathe implements ModInitializer {
	public static final String MOD_ID = "xmqqyswathe";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
    	LOGGER.info("Initializing XmqqysWathe mod...");
    	ModRoles.init(); // 初始化角色
    	LOGGER.info("XmqqysWathe mod initialization completed!");
	}	
	public static ResourceLocation id(String key) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, key);
    }
}