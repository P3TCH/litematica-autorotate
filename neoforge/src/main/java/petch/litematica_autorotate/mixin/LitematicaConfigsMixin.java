package petch.litematica_autorotate.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.malilib.config.IConfigBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import petch.litematica_autorotate.AutoRotateConfig;

// Loads/saves the toggle together with Litematica's Generic options in litematica.json.
@Mixin(value = Configs.class, remap = false)
public abstract class LitematicaConfigsMixin {
    @ModifyExpressionValue(method = {"loadFromFile", "saveToFile"}, at = @At(value = "FIELD",
            target = "Lfi/dy/masa/litematica/config/Configs$Generic;OPTIONS:Lcom/google/common/collect/ImmutableList;"))
    private static ImmutableList<IConfigBase> autorotate$addOption(ImmutableList<IConfigBase> options) {
        return AutoRotateConfig.withOptions(options);
    }
}
