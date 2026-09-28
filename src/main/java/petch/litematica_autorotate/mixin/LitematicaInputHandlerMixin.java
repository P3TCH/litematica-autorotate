package petch.litematica_autorotate.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fi.dy.masa.litematica.event.InputHandler;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import petch.litematica_autorotate.AutoRotateConfig;

// Registers the toggle's hotkey with MaLiLib through Litematica's input handler.
@Mixin(value = InputHandler.class, remap = false)
public abstract class LitematicaInputHandlerMixin {
    @ModifyExpressionValue(method = {"addKeysToMap", "addHotkeys"}, at = @At(value = "FIELD",
            target = "Lfi/dy/masa/litematica/config/Configs$Generic;HOTKEY_LIST:Ljava/util/List;"))
    private List<IHotkey> autorotate$addHotkey(List<IHotkey> hotkeys) {
        return AutoRotateConfig.withHotkeys(hotkeys);
    }
}
