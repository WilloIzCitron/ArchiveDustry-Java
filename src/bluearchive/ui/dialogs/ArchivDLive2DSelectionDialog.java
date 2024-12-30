package bluearchive.ui.dialogs;

import arc.Core;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import bluearchive.ui.ArchivDBackground;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;


public class ArchivDLive2DSelectionDialog extends Dialog {
    public ArchivDLive2DSelectionDialog() {
        super();
        row();
        add(Core.bundle.get("ba-caution")).color(Pal.techBlue).style(Styles.techLabel).fontScale(2f).row();
        image(Tex.whiteui, Pal.techBlue).width(450f).height(3f).pad(4f).center().row();
        add(Core.bundle.get("ba-caution-text")).style(Styles.defaultLabel).row();
        TextButton a = button(Core.bundle.get("ba-l2dInstallLocally"), Icon.download, () -> { this.hide(); Core.settings.put("live2dinstalled", true);}).padLeft(8f).growX().padTop(3f).get();
        row();
        TextButton b = button(Core.bundle.get("ba-l2dInstallRepo"), Icon.download, ArchivDBackground::downloadLive2D).padLeft(8f).growX().padTop(3f).get();
        row();
        TextButton c = button(Core.bundle.get("cancel"), Icon.left, this::hide).padLeft(8f).padTop(3f).width(250f).get();
        show();
    }
}
