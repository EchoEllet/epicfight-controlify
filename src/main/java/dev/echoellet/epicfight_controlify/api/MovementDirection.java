package dev.echoellet.epicfight_controlify.api;

/**
 * Represents player movement direction based on input.
 * <p>
 * forward, backward, left, right indicate the player’s intended movement:
 * <ul>
 *     <li>1 if that direction key is pressed (forward or left)</li>
 *     <li>-1 if the opposite key is pressed (backward or right)</li>
 *     <li>0 if neither key is pressed</li>
 * </ul>
 * <p>
 * Example uses:
 * <ul>
 *     <li>Dodge skill: calculate dash direction from input</li>
 *     <li>Phantom ascent double jump: move and rotate player based on input direction</li>
 * </ul>
 */
public record MovementDirection(int forward, int backward, int left, int right) {
    public int vertical() {
        return forward + backward;
    }

    public int horizontal() {
        return left + right;
    }

    public static MovementDirection fromBooleans(boolean up, boolean down, boolean left, boolean right) {
        return new MovementDirection(
                up ? 1 : 0,
                down ? -1 : 0,
                left ? 1 : 0,
                right ? -1 : 0
        );
    }

    public static MovementDirection fromInputState(PlayerInputState inputState) {
        return fromBooleans(
                inputState.up(), inputState.down(), inputState.left(), inputState.right()
        );
    }
}
