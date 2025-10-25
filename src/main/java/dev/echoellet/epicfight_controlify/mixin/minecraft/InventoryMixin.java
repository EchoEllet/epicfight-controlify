package dev.echoellet.epicfight_controlify.mixin.minecraft;

import dev.echoellet.epicfight_controlify.UpdatedControlEngine;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(Inventory.class)
public class InventoryMixin {
    @Inject(
            // Note for maintainers: when porting to 1.21.2 or newer, target setSelectedHotbarSlot instead.
            // Some adjustments may be required. Please see: https://github.com/isXander/Controlify/blob/e90c94a9dfe45bc071e4ad01c4db039a0dd2492d/src/main/java/dev/isxander/controlify/ingame/InGameInputHandler.java#L96-L102
            // And test with Controlify to avoid regressions. Remove this comment as well.
            method = "swapPaint",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onCycleHotbarSlot(double direction, CallbackInfo ci) {
        // Called whenever the player changes their selected hotbar item via the mouse wheel or other input systems.
        if (UpdatedControlEngine.isHotbarCyclingDisabled()) {
            // InputEvent.MouseScrollingEvent is already cancelled in ControlEngine.Events#mouseScrollEvent to block hotbar cycling for mouse input.
            // Controller inputs are unaffected, so we also cancel it here to enforce the restriction
            // for all input methods.
            ci.cancel();
        }
    }
}