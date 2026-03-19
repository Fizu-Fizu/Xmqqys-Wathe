package io.github.xmqqy.xmqqyswathe;
import org.agmas.harpymodloader.Harpymodloader;

import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.api.WatheRoles;

public class ModRoles {

    public static final Role WARDEN = WatheRoles.registerRole(new Role(
            XmqqysWathe.id("warden"),
            0x00C800,
            true,
            false,
            Role.MoodType.REAL,
            WatheRoles.CIVILIAN.getMaxSprintTime(),
            false
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
        // warden
        Harpymodloader.setRoleMaximum(WARDEN, 1);

        // bomber
        Harpymodloader.setRoleMaximum(BOMBER, 1);
        
        XmqqysWathe.LOGGER.info("[XmqqysWathe] Custom character registration completed");
    }
}