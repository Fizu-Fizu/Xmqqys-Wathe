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
            true
        ));

    public static final Role BOMBER = WatheRoles.registerRole(new Role(
            XmqqysWathe.id("bomber"),
            0x2F4F4F,
            false,
            true,
            Role.MoodType.FAKE,
            -1,
            true
        ));

        public static void init() {
        // pelican
        Harpymodloader.setRoleMaximum(PELICAN, 0);

        // bomber
        Harpymodloader.setRoleMaximum(BOMBER, 1);
        
        XmqqysWathe.LOGGER.info("[XmqqysWathe] Custom character registration completed");
    }
}