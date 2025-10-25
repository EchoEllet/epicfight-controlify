package dev.echoellet.epicfight_controlify.mixin.minecraft;

import dev.echoellet.epicfight_controlify.UpdatedControlEngine;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.events.engine.ControlEngine;

@Mixin(value = ClientCommonPacketListenerImpl.class)
public abstract class ClientCommonPacketListenerImplMixin {

    @Inject(
            method = "send(Lnet/minecraft/network/protocol/Packet;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onBeforeSendPacket(Packet<?> packet, CallbackInfo ci) {
        final boolean isSwapItemWithOffhand = packet instanceof ServerboundPlayerActionPacket actionPacket &&
                actionPacket.getAction() == ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND; 
        if (isSwapItemWithOffhand && UpdatedControlEngine.shouldDisableSwapHandItems()) {
            // Disables the swap offhand items while in action (e.g., attacking in Epic Fight mode).
            ci.cancel();
        }
    }
}