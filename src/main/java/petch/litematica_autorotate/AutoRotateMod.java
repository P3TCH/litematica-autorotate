package petch.litematica_autorotate;

import fi.dy.masa.malilib.hotkeys.KeyCallbackToggleBooleanConfigWithMessage;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = "litematica_autorotate", dist = Dist.CLIENT)
public class AutoRotateMod {
    public AutoRotateMod() {
        AutoRotateConfig.AUTO_ROTATE.getKeybind().setCallback(
                new KeyCallbackToggleBooleanConfigWithMessage(AutoRotateConfig.AUTO_ROTATE));

        NeoForge.EVENT_BUS.addListener(ClientTickEvent.Post.class, event -> {
            // Fallback in case a placement never reached useItemOn's RETURN.
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                AutoRotate.restore(mc.player);
            } else {
                AutoRotate.reset();
            }
        });
    }
}
