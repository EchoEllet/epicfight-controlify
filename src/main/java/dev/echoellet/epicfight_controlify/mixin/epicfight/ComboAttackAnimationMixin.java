package dev.echoellet.epicfight_controlify.mixin.epicfight;

import org.spongepowered.asm.mixin.Mixin;
import yesman.epicfight.api.animation.types.ComboAttackAnimation;

@Mixin(ComboAttackAnimation.class)
public class ComboAttackAnimationMixin {
    // TODO: (Controlify) Might apply the shouldPlayerMove fix https://github.com/Epic-Fight/epicfight/issues/2116
}
