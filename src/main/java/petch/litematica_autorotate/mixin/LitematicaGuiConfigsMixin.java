package petch.litematica_autorotate.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fi.dy.masa.litematica.gui.GuiConfigs;
import fi.dy.masa.malilib.config.IConfigBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import petch.litematica_autorotate.AutoRotateConfig;

// Shows the toggle in the Generic tab of Litematica's config menu.
@Mixin(value = GuiConfigs.class, remap = false)
public abstract class LitematicaGuiConfigsMixin {
    @ModifyExpressionValue(method = "getConfigs", at = @At(value = "FIELD",
            target = "Lfi/dy/masa/litematica/config/Configs$Generic;OPTIONS:Lcom/google/common/collect/ImmutableList;"))
    private ImmutableList<IConfigBase> autorotate$addOption(ImmutableList<IConfigBase> options) {
        return AutoRotateConfig.withOptions(options);
    }
}
