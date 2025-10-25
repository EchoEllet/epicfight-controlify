package dev.echoellet.epicfight_controlify;

import dev.echoellet.epicfight_controlify.api.InputMode;

import dev.echoellet.epicfight_controlify.api.PlayerInputState;
import dev.echoellet.epicfight_controlify.api.action.EpicFightInputActions;
import dev.echoellet.epicfight_controlify.api.controller.ControllerBinding;
import dev.echoellet.epicfight_controlify.api.controller.EpicFightControllerModProvider;
import dev.echoellet.epicfight_controlify.api.controller.IEpicFightControllerMod;
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.bind.ControlifyBindApi;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.entrypoint.InitContext;
import dev.isxander.controlify.api.entrypoint.PreInitContext;
import dev.isxander.controlify.api.event.ControlifyEvents;
import dev.isxander.controlify.api.guide.*;
import dev.isxander.controlify.bindings.BindContext;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.bindings.RadialIcons;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.bindings.input.InputType;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.gui.guide.GuideDomains;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.AbstractButtonComponentProcessor;
import dev.isxander.controlify.utils.render.Blit;
import dev.isxander.controlify.utils.render.CGuiPose;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.gui.screen.SkillEditScreen;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.main.EpicFightMod;

import java.util.*;

// Important for maintainers: Be careful when using Epic Fight classes here,
// as the Epic Fight mod might not be loaded yet. For example, avoid referencing
// EpicFightItems.UCHIGATANA.get() in onControlifyPreInit.
@ApiStatus.Internal
public class EpicFightControlifyEntrypoint implements ControlifyEntrypoint {
    @Override
    public void onControllersDiscovered(ControlifyApi controlify) {
    }

    @Override
    public void onControlifyInit(InitContext context) {
        // It's best to call this method in onControlifyInit,
        // ensuring that Epic Fight can use Controlify input bindings
        // only after they have been registered.
        registerModIntegration();
    }

    @Override
    public void onControlifyPreInit(PreInitContext context) {
        final ControlifyBindApi registrar = ControlifyBindApi.get();
        registerCustomRadialIcons();
        registrar.registerBindContext(COMBAT_MODE_CONTEXT);
        registerInputBindings(registrar);
        registerTargetLockOnSupport();
        registerInGameGuides(context.guideRegistries().inGame());
        registerScreenProcessors();
    }

    private static InputBindingSupplier attack;
    private static InputBindingSupplier mobility;
    private static InputBindingSupplier guard;
    private static InputBindingSupplier dodge;
    private static InputBindingSupplier lockOn;
    private static InputBindingSupplier switchMode;
    private static InputBindingSupplier weaponInnateSkill;
    private static InputBindingSupplier weaponInnateSkillTooltip;
    private static InputBindingSupplier openSkillEditorScreen;
    private static InputBindingSupplier openConfigScreen;
    private static InputBindingSupplier switchVanillaModeDebugging;

    private static final BindContext COMBAT_MODE_CONTEXT = new BindContext(
            EpicFightControlify.rl("epicfight_combat"),
            mc -> {
                final boolean isInGame = mc.screen == null && mc.level != null && mc.player != null;
                return isInGame && ClientEngine.getInstance().isEpicFightMode();
            }
    );
    private static final BindContext IN_GAME_CONTEXT = BindContext.IN_GAME;
    private static final BindContext ANY_SCREEN_CONTEXT = BindContext.ANY_SCREEN;

    private static class ComponentConstants {
        private static final Component KEY_COMBAT = Component.translatable("key.epicfight.combat");
        private static final Component KEY_GUI = Component.translatable("key.epicfight.gui");
        private static final Component KEY_SYSTEM = Component.translatable("key.epicfight.system");

        // Titles

        private static final Component WEAPON_INNATE_SKILL_TOOLTIP = Component.translatable("key.epicfight.show_tooltip");
        private static final Component KEY_SWITCH_MODE = Component.translatable("key.epicfight.switch_mode");
        private static final Component KEY_ATTACK = Component.translatable("key.epicfight.attack");
        private static final Component KEY_WEAPON_INNATE_SKILL = Component.translatable("key.epicfight.weapon_innate_skill");
        private static final Component KEY_SKILL_GUI = Component.translatable("key.epicfight.skill_gui");
        private static final Component KEY_DODGE = Component.translatable("key.epicfight.dodge");
        private static final Component KEY_GUARD = Component.translatable("key.epicfight.guard");
        private static final Component KEY_LOCK_ON = Component.translatable("key.epicfight.lock_on");
        private static final Component KEY_MOVER_SKILL = Component.translatable("key.epicfight.mover_skill");
        private static final Component KEY_CONFIG = Component.translatable("key.epicfight.config");
        private static final Component KEY_SWITCH_VANILLA_MODEL_DEBUG = Component.translatable("key.epicfight.switch_vanilla_model_debug");

        // Descriptions

        private static final Component WEAPON_INNATE_SKILL_TOOLTIP_DESCRIPTION = Component.translatable("key.epicfight.show_tooltip.description");
        private static final Component KEY_SWITCH_MODE_DESCRIPTION = Component.translatable("key.epicfight.switch_mode.description");
        private static final Component KEY_ATTACK_DESCRIPTION = Component.translatable("key.epicfight.attack.description");
        private static final Component KEY_WEAPON_INNATE_SKILL_DESCRIPTION = Component.translatable("key.epicfight.weapon_innate_skill.description");
        private static final Component KEY_SKILL_GUI_DESCRIPTION = Component.translatable("key.epicfight.skill_gui.description");
        private static final Component KEY_DODGE_DESCRIPTION = Component.translatable("key.epicfight.dodge.description");
        private static final Component KEY_GUARD_DESCRIPTION = Component.translatable("key.epicfight.guard.description");
        private static final Component KEY_LOCK_ON_DESCRIPTION = Component.translatable("key.epicfight.lock_on.description");
        private static final Component KEY_MOVER_SKILL_DESCRIPTION = Component.translatable("key.epicfight.mover_skill.description");
        private static final Component KEY_CONFIG_DESCRIPTION = Component.translatable("key.epicfight.config.description");
        private static final Component KEY_SWITCH_VANILLA_MODEL_DEBUG_DESCRIPTION = Component.translatable("key.epicfight.switch_vanilla_model_debug.description");
    }

    private enum EpicFightRadialIcons {
        UCHIGATANA(EpicFightControlify.epicFightRl("textures/item/uchigatana_gui.png")),
        SKILL_BOOK(EpicFightControlify.epicFightRl("textures/item/skillbook.png"));

        private final @NotNull ResourceLocation id;

        EpicFightRadialIcons(@NotNull ResourceLocation id) {
            this.id = id;
        }

        public @NotNull ResourceLocation getId() {
            return id;
        }
    }

    private static void registerCustomRadialIcons() {
        for (EpicFightRadialIcons icon : EpicFightRadialIcons.values()) {
            final ResourceLocation location = icon.getId();
            RadialIcons.registerIcon(location, (graphics, x, y, tickDelta) -> {
                var pose = CGuiPose.ofPush(graphics);
                pose.translate(x, y);
                pose.scale(0.5f, 0.5f);
                Blit.tex(graphics, location, 0, 0, 0, 0, 32, 32, 32, 32);
                pose.pop();
            });
        }
    }

    private static void registerInputBindings(ControlifyBindApi registrar) {
        for (EpicFightInputActions action : nonVanillaActions()) {
            registerInputBinding(registrar, action);
        }
    }

    public static Set<EpicFightInputActions> nonVanillaActions() {
        Set<EpicFightInputActions> result = EnumSet.noneOf(EpicFightInputActions.class);
        for (EpicFightInputActions action : EpicFightInputActions.values()) {
            if (!action.isVanilla()) result.add(action);
        }
        return result;
    }

    /**
     * Registers a non-vanilla input binding with Controlify.
     * <p>
     * Must <strong>only</strong> be called for non-vanilla
     * {@link EpicFightInputActions}. Vanilla actions are already registered
     * and calling this with one will throw {@link IllegalArgumentException}.
     * <p>
     * <strong>Type-safety and exhaustive checking:</strong><br>
     * Returns an {@link InputBindingSupplier} via a <em>switch expression</em>
     * over all enum constants. The returned value is a dummy, used only
     * to satisfy the Java compiler and enforce exhaustive handling. It is
     * <strong>never used</strong> and has no effect on behavior.
     *
     * @param registrar the Controlify API used to register the binding
     * @param action    the non-vanilla input action to register
     * @return a dummy {@link InputBindingSupplier} for type-safety only
     * @throws IllegalArgumentException if called with a vanilla input action
     */
    @SuppressWarnings("UnusedReturnValue") // Read JavaDocs of this method before removing.
    private static @NotNull InputBindingSupplier registerInputBinding(
            @NotNull ControlifyBindApi registrar,
            @NotNull EpicFightInputActions action
    ) {
        final Component combatCategory = ComponentConstants.KEY_COMBAT;
        final Component guiCategory = ComponentConstants.KEY_GUI;
        final Component systemCategory = ComponentConstants.KEY_SYSTEM;


        // Prevents Controlify from auto-registering this vanilla key mapping,
        // since Epic Fight already provides native support for it.
        final KeyMapping keyMappingToDisable = action.keyMapping();

        // Using a switch expression to enforce compile-time exhaustive checking.
        // The returned value is a dummy and does nothing; its only purpose is to
        // satisfy the compiler and ensure all enum constants are handled.
        return switch (action) {
            case VANILLA_ATTACK_DESTROY, USE, SWAP_OFF_HAND, TOGGLE_PERSPECTIVE, DROP, MOVE_FORWARD, MOVE_BACKWARD,
                 MOVE_LEFT, MOVE_RIGHT, SPRINT, SNEAK, JUMP -> throw new IllegalArgumentException(
                    "ControlifyCompat#registerInputBinding() must only be called for non-vanilla actions. " +
                            "This action is vanilla and already registered in Controlify: " + action.name()
            );
            case ATTACK -> attack = registrar.registerBinding(
                    builder -> builder.id(EpicFightControlify.rl("attack"))
                            .category(combatCategory)
                            .allowedContexts(COMBAT_MODE_CONTEXT)
                            .name(ComponentConstants.KEY_ATTACK)
                            .description(ComponentConstants.KEY_ATTACK_DESCRIPTION)
                            .addKeyCorrelation(keyMappingToDisable)
                            .keyEmulation(EpicFightKeyMappings.ATTACK)
            );
            case MOBILITY -> mobility = registrar.registerBinding(
                    builder -> builder.id(EpicFightControlify.rl("mobility"))
                            .category(combatCategory)
                            .allowedContexts(COMBAT_MODE_CONTEXT)
                            .name(ComponentConstants.KEY_MOVER_SKILL)
                            .description(ComponentConstants.KEY_MOVER_SKILL_DESCRIPTION)
                            .addKeyCorrelation(keyMappingToDisable)
            );
            case GUARD -> guard = registrar.registerBinding(
                    builder -> builder.id(EpicFightControlify.rl("guard"))
                            .category(combatCategory)
                            .allowedContexts(COMBAT_MODE_CONTEXT)
                            .name(ComponentConstants.KEY_GUARD)
                            .description(ComponentConstants.KEY_GUARD_DESCRIPTION)
                            .addKeyCorrelation(keyMappingToDisable)
                            .keyEmulation(EpicFightKeyMappings.GUARD)
            );
            case DODGE -> dodge = registrar.registerBinding(
                    builder -> builder.id(EpicFightControlify.rl("dodge"))
                            .category(combatCategory)
                            .allowedContexts(COMBAT_MODE_CONTEXT)
                            .name(ComponentConstants.KEY_DODGE)
                            .description(ComponentConstants.KEY_DODGE_DESCRIPTION)
                            .addKeyCorrelation(keyMappingToDisable)
                            .keyEmulation(EpicFightKeyMappings.DODGE)
            );
            case LOCK_ON -> lockOn = registrar.registerBinding(
                    builder -> builder.id(EpicFightControlify.rl("lock_on"))
                            .category(combatCategory)
                            .allowedContexts(COMBAT_MODE_CONTEXT)
                            .name(ComponentConstants.KEY_LOCK_ON)
                            .description(ComponentConstants.KEY_LOCK_ON_DESCRIPTION)
                            .addKeyCorrelation(keyMappingToDisable)
                            .keyEmulation(EpicFightKeyMappings.LOCK_ON)
            );
            case SWITCH_MODE -> switchMode = registrar.registerBinding(
                    builder -> builder.id(EpicFightControlify.rl("switch_mode"))
                            .category(systemCategory)
                            .allowedContexts(IN_GAME_CONTEXT)
                            .name(ComponentConstants.KEY_SWITCH_MODE)
                            .description(ComponentConstants.KEY_SWITCH_MODE_DESCRIPTION)
                            .addKeyCorrelation(keyMappingToDisable)
                            .radialCandidate(EpicFightRadialIcons.UCHIGATANA.getId())
                            .keyEmulation(EpicFightKeyMappings.SWITCH_MODE)
            );
            case WEAPON_INNATE_SKILL -> weaponInnateSkill = registrar.registerBinding(
                    builder -> builder.id(EpicFightControlify.rl("weapon_innate_skill"))
                            .category(combatCategory)
                            .allowedContexts(COMBAT_MODE_CONTEXT)
                            .name(ComponentConstants.KEY_WEAPON_INNATE_SKILL)
                            .description(ComponentConstants.KEY_WEAPON_INNATE_SKILL_DESCRIPTION)
                            .addKeyCorrelation(keyMappingToDisable)
                            .keyEmulation(EpicFightKeyMappings.WEAPON_INNATE_SKILL)
            );
            case WEAPON_INNATE_SKILL_TOOLTIP -> weaponInnateSkillTooltip = registrar.registerBinding(
                    builder -> builder.id(EpicFightControlify.rl("weapon_innate_skill_tooltip"))
                            .category(guiCategory)
                            .allowedContexts(ANY_SCREEN_CONTEXT)
                            .name(ComponentConstants.WEAPON_INNATE_SKILL_TOOLTIP)
                            .description(ComponentConstants.WEAPON_INNATE_SKILL_TOOLTIP_DESCRIPTION)
                            .addKeyCorrelation(keyMappingToDisable)
                            .keyEmulation(EpicFightKeyMappings.WEAPON_INNATE_SKILL_TOOLTIP)
            );
            case OPEN_SKILL_SCREEN -> openSkillEditorScreen = registrar.registerBinding(
                    builder -> builder.id(EpicFightControlify.rl("open_skill_editor_screen"))
                            .category(guiCategory)
                            .allowedContexts(IN_GAME_CONTEXT)
                            .name(ComponentConstants.KEY_SKILL_GUI)
                            .description(ComponentConstants.KEY_SKILL_GUI_DESCRIPTION)
                            .addKeyCorrelation(keyMappingToDisable)
                            .radialCandidate(EpicFightRadialIcons.SKILL_BOOK.getId())
                            .keyEmulation(EpicFightKeyMappings.SKILL_EDIT)
            );
            case OPEN_CONFIG_SCREEN -> openConfigScreen = registrar.registerBinding(
                    builder -> builder.id(EpicFightControlify.rl("open_config_screen"))
                            .category(guiCategory)
                            .allowedContexts(IN_GAME_CONTEXT)
                            .name(ComponentConstants.KEY_CONFIG)
                            .description(ComponentConstants.KEY_CONFIG_DESCRIPTION)
                            .addKeyCorrelation(keyMappingToDisable)
                            .radialCandidate(RadialIcons.getItem(Items.REDSTONE))
                            .keyEmulation(EpicFightKeyMappings.OPEN_CONFIG_SCREEN)
            );
            case SWITCH_VANILLA_MODEL_DEBUGGING -> switchVanillaModeDebugging = registrar.registerBinding(
                    builder -> builder.id(EpicFightControlify.rl("switch_vanilla_mode_debugging"))
                            .category(systemCategory)
                            .allowedContexts(IN_GAME_CONTEXT)
                            .name(ComponentConstants.KEY_SWITCH_VANILLA_MODEL_DEBUG)
                            .description(ComponentConstants.KEY_SWITCH_VANILLA_MODEL_DEBUG_DESCRIPTION)
                            .addKeyCorrelation(keyMappingToDisable)
                            .keyEmulation(EpicFightKeyMappings.SWITCH_VANILLA_MODEL_DEBUGGING)
            );
        };
    }

    private static void registerModIntegration() {
        EpicFightControllerModProvider.set(EpicFightMod.MODID, new ControlifyIntegration());
    }

    private static void registerTargetLockOnSupport() {
        ControlifyEvents.LOOK_INPUT_MODIFIER.register(event -> {
            LocalPlayerPatch localPlayerPatch = ClientEngine.getInstance().getPlayerPatch();

            if (localPlayerPatch != null && localPlayerPatch.isTargetLockedOn()) {
                // Fixes a minor issue when locking on an enemy.
                event.lookInput().zero();
            }
        });
    }

    private static void registerInGameGuides(GuideDomainRegistry<InGameCtx> registry) {
//        registry.registerDynamicRule(
//                Rule.builder().binding(guard)
//                        .where(ActionLocation.LEFT)
//                        .then(ComponentConstants.KEY_GUARD)
//                        .when(InGameFacts.ON_GROUND)
//                        .build()
//        );
    }

    private static @NotNull InputBinding getControlifyBinding(@NotNull EpicFightInputActions action) {
        final InputBindingSupplier bindingSupplier = switch (action) {
            case VANILLA_ATTACK_DESTROY -> ControlifyBindings.ATTACK;
            case USE -> ControlifyBindings.USE;
            case SWAP_OFF_HAND -> ControlifyBindings.SWAP_HANDS;
            case DROP -> ControlifyBindings.DROP_INGAME;
            case TOGGLE_PERSPECTIVE -> ControlifyBindings.CHANGE_PERSPECTIVE;
            case ATTACK -> attack;
            case JUMP -> ControlifyBindings.JUMP;
            case MOBILITY -> mobility;
            case GUARD -> guard;
            case DODGE -> dodge;
            case LOCK_ON -> lockOn;
            case SWITCH_MODE -> switchMode;
            case WEAPON_INNATE_SKILL -> weaponInnateSkill;
            case WEAPON_INNATE_SKILL_TOOLTIP -> weaponInnateSkillTooltip;
            case MOVE_FORWARD -> ControlifyBindings.WALK_FORWARD;
            case MOVE_BACKWARD -> ControlifyBindings.WALK_BACKWARD;
            case MOVE_LEFT -> ControlifyBindings.WALK_LEFT;
            case MOVE_RIGHT -> ControlifyBindings.WALK_RIGHT;
            case SPRINT -> ControlifyBindings.SPRINT;
            case SNEAK -> ControlifyBindings.SNEAK;
            case OPEN_SKILL_SCREEN -> openSkillEditorScreen;
            case OPEN_CONFIG_SCREEN -> openConfigScreen;
            case SWITCH_VANILLA_MODEL_DEBUGGING -> switchVanillaModeDebugging;
        };
        final @Nullable InputBinding binding = bindingSupplier.onOrNull(requireControllerEntity());
        return Objects.requireNonNull(binding, "The binding for the action " + action.name() + " is not yet registered.");
    }

    private static @NotNull ControlifyApi getApi() {
        return ControlifyApi.get();
    }

    private static @NotNull ControllerEntity requireControllerEntity() {
        Optional<ControllerEntity> optionalControllerEntity = getApi().getCurrentController();

        if (optionalControllerEntity.isEmpty()) {
            final String message = String.format(
                    "The method IEpicFightControllerMod#getInputState must not be called when the input mode is not %s",
                    InputMode.CONTROLLER.name()
            );
            EpicFightMod.LOGGER.error(message);
            throw new IllegalStateException(message);
        }

        return optionalControllerEntity.get();
    }

    /**
     * Allows Epic Fight to communicate with Controlify APIs without depending on their classes directly.
     *
     */
    private static class ControlifyIntegration implements IEpicFightControllerMod {
        @Override
        public String getModName() {
            return "Controlify";
        }

        @Override
        public @NotNull InputMode getInputMode() {
            return switch (getApi().currentInputMode()) {
                case KEYBOARD_MOUSE -> InputMode.KEYBOARD_MOUSE;
                case CONTROLLER -> InputMode.CONTROLLER;
                case MIXED -> InputMode.MIXED;
            };
        }

        @Override
        public @NotNull ControllerBinding getBinding(EpicFightInputActions action) {
            return new ControllerBindingImpl(getControlifyBinding(action));
        }

        @Override
        public @NotNull PlayerInputState getInputState() {
            ControllerEntity controller = requireControllerEntity();

            InputBinding forwardBind = ControlifyBindings.WALK_FORWARD.on(controller);
            InputBinding backwardBind = ControlifyBindings.WALK_BACKWARD.on(controller);
            InputBinding leftBind = ControlifyBindings.WALK_LEFT.on(controller);
            InputBinding rightBind = ControlifyBindings.WALK_RIGHT.on(controller);
            InputBinding jumpBind = ControlifyBindings.JUMP.on(controller);
            InputBinding sneakBind = ControlifyBindings.SNEAK.on(controller);

            float forwardImpulse = forwardBind.analogueNow() - backwardBind.analogueNow();
            float leftImpulse = leftBind.analogueNow() - rightBind.analogueNow();

            return new PlayerInputState(
                    leftImpulse, forwardImpulse,
                    forwardBind.digitalNow(), backwardBind.digitalNow(),
                    leftBind.digitalNow(), rightBind.digitalNow(),
                    jumpBind.digitalNow(), sneakBind.digitalNow()
            );
        }

        @Override
        public boolean isBoundToSameButton(@NotNull EpicFightInputActions action, @NotNull EpicFightInputActions action2) {
            final Input input1 = getControlifyBinding(action).boundInput();
            final Input input2 = getControlifyBinding(action2).boundInput();
            if (input1.type() == InputType.BUTTON && input2.type() == InputType.BUTTON) {
                return input1.getRelevantInputs().equals(input2.getRelevantInputs());
            }
            return false;
        }
    }

    private record ControllerBindingImpl(@NotNull InputBinding inputBinding) implements ControllerBinding {

        @Override
        public ResourceLocation id() {
            return inputBinding.id();
        }

        @Override
        public @NotNull InputType getInputType() {
            // TODO: (Controlify) Implement or remove getInputType from IEpicFightControllerMod.
            throw new NotImplementedException("The method ControllerBinding#getInputType should not be called, ");
        }

        @Override
        public boolean isDigitalActiveNow() {
            return inputBinding.digitalNow();
        }

        @Override
        public boolean wasDigitalActivePreviously() {
            return inputBinding.digitalPrev();
        }

        @Override
        public boolean isDigitalJustPressed() {
            return inputBinding.justPressed();
        }

        @Override
        public boolean isDigitalJustReleased() {
            return inputBinding.justReleased();
        }

        @Override
        public float getAnalogueNow() {
            return inputBinding.analogueNow();
        }

        @Override
        public void emulatePress() {
            inputBinding.fakePress();
        }
    }

    private static void registerScreenProcessors() {
        ScreenProcessorProvider.registerProvider(
                SkillEditScreen.class,
                SkillEditScreenProcessor::new
        );
        // TODO: (Controlify) SkillEditScreen.SlotButton is private, add use access transformer
//        ComponentProcessorProvider.REGISTRY.register(
//                SkillEditScreen.SlotButton.class,
//                SkillEditScreenProcessor.SlotButtonProcessor::new
//        );
    }

    // TODO: (Controlify) Add support for skill editor screen
    private static class SkillEditScreenProcessor extends ScreenProcessor<SkillEditScreen> {
        public SkillEditScreenProcessor(SkillEditScreen screen) {
            super(screen);
        }

        private static class SlotButtonProcessor extends AbstractButtonComponentProcessor {
            public SlotButtonProcessor(AbstractButton button) {
                super(button);
            }
        }
    }
}