package dev.enderdevelopment.vibrantvisuals;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class VibrantVisualsClient implements ClientModInitializer {
    private static final String CATEGORY = "key.categories.vibrant_visuals";
    private static final String TOGGLE_KEY = "key.vibrant_visuals.toggle";
    private static final double VIBRANT_GAMMA = 1.0D;

    private static KeyBinding toggleKey;
    private static boolean enabled;
    private static Double previousGamma;
    private static ParticlesMode previousParticles;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                TOGGLE_KEY,
                GLFW.GLFW_KEY_V,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                setEnabled(client, !enabled);
            }
        });
    }

    private static void setEnabled(MinecraftClient client, boolean value) {
        if (value) {
            previousGamma = client.options.getGamma().getValue();
            previousParticles = client.options.getParticles().getValue();
            client.options.getGamma().setValue(VIBRANT_GAMMA);
            client.options.getParticles().setValue(ParticlesMode.MINIMAL);
            enabled = true;
            showStatus(client, "Vibrant visuals enabled");
            return;
        }

        if (previousGamma != null) {
            client.options.getGamma().setValue(previousGamma);
        }
        if (previousParticles != null) {
            client.options.getParticles().setValue(previousParticles);
        }
        enabled = false;
        showStatus(client, "Vibrant visuals disabled");
    }

    private static void showStatus(MinecraftClient client, String message) {
        if (client.player != null) {
            client.player.sendMessage(Text.literal(message), true);
        }
    }
}
