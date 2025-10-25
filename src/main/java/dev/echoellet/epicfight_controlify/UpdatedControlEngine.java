package dev.echoellet.epicfight_controlify;

import dev.echoellet.epicfight_controlify.mixin.minecraft.InventoryMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import org.jetbrains.annotations.ApiStatus;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;

@EventBusSubscriber(modid = EpicFightControlify.MODID, value = Dist.CLIENT)
public class UpdatedControlEngine {
    /**
     * Determines whether hotbar cycling should be disabled.
     * <p>
     * Used internally in {@link InputEvent.MouseScrollingEvent} and
     * {@link InventoryMixin}. Cancelling the mouse
     * scroll event disables cycling for vanilla mouse input, but other input
     * systems (e.g., controllers) still call {@link Inventory#swapPaint}, so we
     * cancel those calls as well. This ensures universal behavior while
     * maximizing compatibility.
     *
     * @return {@code true} if hotbar item cycling should be disabled; {@code false} otherwise.
     * */
    @ApiStatus.Internal
    public static boolean isHotbarCyclingDisabled() {
        final Minecraft minecraft = Minecraft.getInstance();
        final LocalPlayerPatch localPlayerPatch = ClientEngine.getInstance().getPlayerPatch();
        return minecraft.player != null && localPlayerPatch != null && !localPlayerPatch.getEntityState().canSwitchHoldingItem() && minecraft.screen == null;
    }

    @SubscribeEvent
    public static void mouseScrollEvent(InputEvent.MouseScrollingEvent event) {
        // Disables item switching for the vanilla mouse input
        if (isHotbarCyclingDisabled()) {
            event.setCanceled(true);
        }
    }

    /**
     * Disables the swap offhand items while the player is in action.
     * <p>
     * Previously, we injected into the vanilla {@link Minecraft#handleKeybinds} method
     * and used this workaround to reset the internal counter for the vanilla swap offhand keybind:
     * <pre>
     * {@code
     * // Called before Minecraft#handleKeybinds is called.
     * KeyMapping swapOffhand = Minecraft.getInstance().options.keySwapOffhand;
     * while (swapOffhand.consumeClick()) {}
     * KeyMapping.set(swapOffhand.getKey(), false);
     * }
     * </pre>
     * <p>
     * However, that approach relied on assumptions and did not support other mods, inputs, or systems.
     * The problem is now solved by injecting into {@link ClientPacketListener#send} and
     * canceling the call when the player is in action and trying to swap offhand items.
     *
     * @see ControlEngine#consumeSwapOffhandKeyClicks
     */
    @SuppressWarnings("JavadocReference")
    @ApiStatus.Internal
    public static boolean shouldDisableSwapHandItems() {
        final LocalPlayerPatch playerPatch = ClientEngine.getInstance().getPlayerPatch();
        if (playerPatch == null) {
            return false;
        }
        return playerPatch.getEntityState().inaction() || (!playerPatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).canBePlacedOffhand());
    }

    /**
     * Disables the player's vanilla attacks while in Epic Fight mode.
     * <p>
     * Previously, we injected into the vanilla {@link Minecraft#handleKeybinds} method
     * and used this workaround to reset the internal counter for the vanilla attack keybind:
     * <pre>
     * {@code
     * // Called before Minecraft#handleKeybinds is called.
     * KeyMapping attack = Minecraft.getInstance().options.keyAttack;
     * while (attack.consumeClick()) {}
     * KeyMapping.set(attack.getKey(), false);
     * }
     * </pre>
     * <p>
     * However, that approach relied on assumptions and did not support other mods, inputs, or systems.
     * The problem is now solved by injecting into {@link Minecraft#startAttack} and
     * canceling the call when the player is in Epic Fight mode.
     * This also means the player must be always in vanilla mode to perform vanilla attacks, which is as intended.
     *
     * @see ControlEngine#consumeVanillaAttackKeyClicks
     */
    @SuppressWarnings("JavadocReference")
    @ApiStatus.Internal
    public static boolean shouldDisableVanillaAttack() {
        final LocalPlayerPatch playerPatch = ClientEngine.getInstance().getPlayerPatch();
        if (playerPatch == null) {
            return false;
        }
        return playerPatch.isEpicFightMode() && playerPatch.canPlayAttackAnimation();
    }
}
