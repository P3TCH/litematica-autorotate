package petch.litematica_autorotate;

import fi.dy.masa.malilib.hotkeys.KeyCallbackToggleBooleanConfigWithMessage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class AutoRotateClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoRotateConfig.AUTO_ROTATE.getKeybind().setCallback(
                new KeyCallbackToggleBooleanConfigWithMessage(AutoRotateConfig.AUTO_ROTATE));

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            // Fallback in case a placement never reached useItemOn's RETURN.
            if (mc.player != null) {
                AutoRotate.restore(mc.player);
            } else {
                AutoRotate.reset();
            }
        });
    }
}
