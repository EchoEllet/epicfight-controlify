package dev.echoellet.epicfight_controlify.mixin.epicfight;

import dev.echoellet.epicfight_controlify.api.action.EpicFightInputActions;
import dev.echoellet.epicfight_controlify.api.handlers.InputManager;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.Input;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yesman.epicfight.skill.mover.PhantomAscentSkill;

@Mixin(PhantomAscentSkill.class)
public class PhantomAscentSkillMixin {
    @Redirect(
            method = "movementInputEvent(Lnet/neoforged/neoforge/client/event/MovementInputUpdateEvent;Lyesman/epicfight/skill/SkillContainer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/KeyMapping;isDown()Z"
            )
    )
    private boolean isJumpPressed(KeyMapping instance) {
        if (InputManager.supportsControllerInput() && InputManager.isActionActive(EpicFightInputActions.JUMP)) {
            return true;
        }
        return instance.isDown();
    }

    @Redirect(
            method = "movementInputEvent",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/player/Input;up:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean onUp(Input instance) {
        return InputManager.getInputState(instance).up();
    }

    @Redirect(
            method = "movementInputEvent",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/player/Input;down:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean onDown(Input instance) {
        return InputManager.getInputState(instance).down();
    }

    @Redirect(
            method = "movementInputEvent",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/player/Input;left:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean onLeft(Input instance) {
        return InputManager.getInputState(instance).left();
    }

    @Redirect(
            method = "movementInputEvent",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/player/Input;right:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean onRight(Input instance) {
        return InputManager.getInputState(instance).right();
    }
}
