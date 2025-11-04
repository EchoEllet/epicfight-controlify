package dev.echoellet.epicfight_controlify.util;

import dev.isxander.controlify.bindings.RadialIcons;
import dev.isxander.controlify.utils.render.Blit;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public final class RadialUtils {
    private RadialUtils() {}

    public static void register(@NotNull final ResourceLocation location) {
        // For consistency with the current Controlify radial icons,
        // this code is equivalent to:
        // https://github.com/isXander/Controlify/blob/f5c94c57d5e0d4954e413624a0d7ead937b6e8ab/src/main/java/dev/isxander/controlify/bindings/RadialIcons.java#L106-L112
        RadialIcons.registerIcon(location, (graphics, x, y, tickDelta) -> {
            graphics.pose().pushPose();
            graphics.pose().translate(x, y, 0);
            graphics.pose().scale(0.5f, 0.5f, 1f);
            Blit.blitTex(graphics, location, 0, 0, 0, 0, 32, 32, 32, 32);
            graphics.pose().popPose();
        });
    }
}
