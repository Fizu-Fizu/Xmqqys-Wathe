package io.github.xmqqy.xmqqyswathe;
import org.agmas.harpymodloader.Harpymodloader;

import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.api.WatheRoles;

public class ModRoles {

    public static final Role PELICAN = WatheRoles.registerRole(new Role(
            XmqqysWathe.id("pelican"),
            0x00C800,
            false,
            false,
            Role.MoodType.FAKE,
            -1,
            true));

    public static void init() {
        Harpymodloader.setRoleMaximum(PELICAN, 1);
        
        XmqqysWathe.LOGGER.info("[XmqqysWathe] Custom character registration completed");
    }
}