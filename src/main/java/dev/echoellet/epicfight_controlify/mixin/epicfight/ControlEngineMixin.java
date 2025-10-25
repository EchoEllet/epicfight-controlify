package dev.echoellet.epicfight_controlify.mixin.epicfight;

import dev.echoellet.epicfight_controlify.api.action.EpicFightInputActions;
import dev.echoellet.epicfight_controlify.api.handlers.InputManager;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.neoevent.playerpatch.SkillCastEvent;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;

@Mixin(ControlEngine.class)
public class ControlEngineMixin {
    @Shadow
    private LocalPlayerPatch playerpatch;

    @Shadow
    private boolean moverPressToggle;

    @Shadow
    private LocalPlayer player;

    @Shadow
    public Options options;

    @Inject(method = "isKeyDown(Lnet/minecraft/client/KeyMapping;)Z", at = @At("HEAD"), cancellable = true)
    private static void onIsKeyDown(KeyMapping key, CallbackInfoReturnable<Boolean> cir) {
        if (InputManager.supportsControllerInput()) {
            final EpicFightInputActions action = EpicFightInputActions.fromKeyMapping(key);
            if (action != null) {
                cir.setReturnValue(InputManager.isActionActive(action));
                cir.cancel();
            }
        }
    }

    @Inject(
            method = "handleEpicFightKeyMappings",
            at = @At("HEAD")
    )
    private void redirectMoverSkillKeyValue(CallbackInfo ci) {
        if (InputManager.supportsControllerInput()) {
            InputManager.triggerOnPress(EpicFightInputActions.MOBILITY, true, () -> {
                if (this.playerpatch.isEpicFightMode() && !this.playerpatch.isHoldingAny()) {
                    if (EpicFightKeyMappings.MOVER_SKILL.getKey().getValue() == this.options.keyJump.getKey().getValue()) {
                        SkillContainer skillContainer = this.playerpatch.getSkill(SkillSlots.MOVER);
                        SkillCastEvent event = new SkillCastEvent(this.playerpatch, skillContainer, null);

                        if (skillContainer.canUse(this.playerpatch, event) && this.player.getVehicle() == null) {
                            if (!this.moverPressToggle) {
                                this.moverPressToggle = true;
                            }
                        }
                    } else {
                        SkillContainer skill = this.playerpatch.getSkill(SkillSlots.MOVER);
                        skill.sendCastRequest(this.playerpatch, ControlEngine.getInstance());
                    }
                }
            });
        }
    }
}
