package petch.litematica_autorotate;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import java.util.ArrayList;
import java.util.List;

/**
 * The toggle lives in Litematica's own Generic config tab: the mixins append it to
 * Configs.Generic.OPTIONS / HOTKEY_LIST wherever Litematica reads them, so it is shown
 * in the M + C menu and saved in litematica.json alongside the other options.
 */
public final class AutoRotateConfig {
    public static final ConfigBooleanHotkeyed AUTO_ROTATE =
            new ConfigBooleanHotkeyed("easyPlaceAutoRotate", true, "J").apply("litematica_autorotate.config.generic");

    private AutoRotateConfig() {
    }

    public static ImmutableList<IConfigBase> withOptions(ImmutableList<IConfigBase> options) {
        if (options.contains(AUTO_ROTATE)) {
            return options;
        }

        return ImmutableList.<IConfigBase>builder().addAll(options).add(AUTO_ROTATE).build();
    }

    public static List<IHotkey> withHotkeys(List<IHotkey> hotkeys) {
        if (hotkeys.contains(AUTO_ROTATE)) {
            return hotkeys;
        }

        List<IHotkey> list = new ArrayList<>(hotkeys);
        list.add(AUTO_ROTATE);
        return list;
    }
}
