package dev.echoellet.epicfight_controlify.api.controller;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a controller button or analogue stick axis.
 * Provides current and previous state, analogue/digital values, and metadata.
 * <p>
 * Serves a similar role to Minecraft's vanilla {@link net.minecraft.client.KeyMapping},
 * but for controller input instead of keyboard or mouse.
 */
@ApiStatus.Experimental
public interface ControllerBinding {
    /**
     * The ID of the binding (e.g., `epicfight:attack).
     *
     * @return the ID
     */
    ResourceLocation id();

    /**
     * Defines the type of input represented by a {@link ControllerBinding}.
     * <p>
     * Inputs can be either analogue or digital:
     * <ul>
     *      <li>{@link #ANALOGUE} – Inputs with a continuous range of values, such as the movement axes of a stick or triggers (e.g., 0.0 to 1.0).</li>
     *     <li>{@link #DIGITAL} – Inputs that are binary, such as buttons or stick presses (e.g., L3/R3), which are either pressed or released.</li>
     * </ul>
     * <p>
     * Note: A single physical control can produce multiple input types. For example:
     * <ul>
     *     <li>moving a left stick generates analogue signals (X/Y axes)</li>
     *     <li>pressing the stick down generates a separate digital input</li>
     * </ul>
     */
    enum InputType {
        ANALOGUE,
        DIGITAL
    }

    /**
     * Returns the type of this controller binding.
     *
     * @return the {@link InputType} of this binding.
     */
    @NotNull
    InputType getInputType();

    /**
     * Returns whether this binding represents an analogue input.
     * <p>
     *
     * @return true if analogue, false if digital.
     * @see InputType
     */
    default boolean isAnalogueType() {
        return switch (getInputType()) {
            case ANALOGUE -> true;
            case DIGITAL -> false;
        };
    }

    /**
     * Returns whether the digital state is currently active in this tick.
     * <p>
     * This method is only applicable to digital inputs (e.g., buttons, pressing the stick down).
     * </p>
     *
     * @return the current digital state, this tick.
     * @see InputType#DIGITAL
     */
    boolean isDigitalActiveNow();

    /**
     * Returns whether the digital state was active in the previous tick.
     * <p>
     * This method is only applicable to digital inputs (e.g., buttons, pressing the stick down).
     * </p>
     *
     * @return the previous digital state, 1 tick ago.
     * @see InputType#DIGITAL
     */
    boolean wasDigitalActivePreviously();

    /**
     * Returns whether the digital state was just pressed.
     * <p>
     * This method is only applicable to digital inputs (e.g., buttons, pressing the stick down).
     * </p>
     *
     * @return true if the binding is pressed this tick and not pressed the previous tick.
     * @see InputType#DIGITAL
     */
    boolean isDigitalJustPressed();

    /**
     * Returns whether the digital state was just released.
     * <p>
     * This method is only applicable to digital inputs (e.g., buttons, pressing the stick down).
     * </p>
     *
     * @return true if the binding is not pressed this tick and pressed the previous tick.
     * @see InputType#DIGITAL
     */
    boolean isDigitalJustReleased();

    /**
     * Returns the current analogue input value.
     * <p>
     * This method is only applicable to analogue inputs (e.g., left stick, right trigger).
     * For more details, refer to {@link InputType#ANALOGUE}.
     * </p>
     *
     * @return the current analogue value in the range {@code 0.0}–{@code 1.0}, representing this tick's state.
     * @see InputType#ANALOGUE
     */
    float getAnalogueNow();

    /**
     * Simulates a press of this binding.
     * <p>
     * Can be used for GUI interactions or synthetic input from other systems.
     */
    void emulatePress();
}
