package io.github.xmqqy.xmqqyswathe.component;

import com.mojang.serialization.Codec;
import io.github.xmqqy.xmqqyswathe.XmqqysWathe;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import java.util.Optional;

public class BombComponents {
    // 炸弹剩余时间（单位：tick）
    public static final DataComponentType<Integer> BOMB_TIME = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        XmqqysWathe.id("bomb_time"),
        DataComponentType.<Integer>builder()
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .build()
    );

    // 右键冷却时间（单位：tick）
    public static final DataComponentType<Integer> COOLDOWN = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        XmqqysWathe.id("cooldown"),
        DataComponentType.<Integer>builder()
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .build()
    );

    public static final DataComponentType<Optional<String>> SOURCE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            XmqqysWathe.id("source"),
            DataComponentType.<Optional<String>>builder()
                    .persistent(Codec.STRING.optionalFieldOf("source").codec())
                    .networkSynchronized(ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8))
                    .build()
    );

    public static void init() {
        // 
    }
}