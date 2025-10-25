package dev.echoellet.epicfight_controlify.mixin.epicfight;

import dev.echoellet.epicfight_controlify.api.handlers.InputManager;
import net.minecraft.client.player.Input;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yesman.epicfight.skill.dodge.KnockdownWakeupSkill;

@Mixin(KnockdownWakeupSkill.class)
public class KnockdownWakeupSkillMixin {
    @Redirect(
            method = "gatherArguments(Lyesman/epicfight/skill/SkillContainer;Lyesman/epicfight/client/events/engine/ControlEngine;Lnet/minecraft/nbt/CompoundTag;)V",
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
            method = "gatherArguments(Lyesman/epicfight/skill/SkillContainer;Lyesman/epicfight/client/events/engine/ControlEngine;Lnet/minecraft/nbt/CompoundTag;)V",
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
