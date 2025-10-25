package dev.echoellet.epicfight_controlify.mixin.minecraft;

import dev.echoellet.epicfight_controlify.UpdatedControlEngine;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Minecraft.class)
public class MinecraftMixin {

    @Inject(at = @At("HEAD"), method = "startAttack", cancellable = true)
    private void onStartVanillaAttack(CallbackInfoReturnable<Boolean> cir) {
        if (UpdatedControlEngine.shouldDisableVanillaAttack()) {
            // Prevents the player from performing vanilla attack actions while in Epic Fight mode.
            cir.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "continueAttack", cancellable = true)
    private void onContinueVanillaAttack(boolean leftClick, CallbackInfo ci) {
        if (UpdatedControlEngine.shouldDisableVanillaAttack()) {
            // Prevents the player from breaking blocks such as grass while in Epic Fight mode.
            ci.cancel();
        }
    }
}
