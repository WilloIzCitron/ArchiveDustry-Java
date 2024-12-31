package bluearchive.ui.dialogs;

import arc.Core;
import arc.scene.actions.Actions;
import arc.scene.ui.*;
import bluearchive.ui.ArchivDBackground;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;


public class ArchivDLive2DSelectionDialog extends Dialog {
    private static TextButton a,b,c;
    public ArchivDLive2DSelectionDialog() {
        super();
        row();
        add(Core.bundle.get("ba-caution")).color(Pal.techBlue).style(Styles.techLabel).fontScale(2f).row();
        image(Tex.whiteui, Pal.techBlue).width(450f).height(3f).pad(4f).center().row();
        add(Core.bundle.get("ba-caution-text")).style(Styles.defaultLabel).row();
        table(t -> {
            a = t.button(Core.bundle.get("ba-l2dInstallLocally"), Icon.download, () -> { this.hide(); Core.settings.put("live2dinstalled", true);}).padLeft(8f).padTop(3f).width(250f).get();
            b = t.button(Core.bundle.get("ba-l2dInstallRepo"), Icon.download, ArchivDBackground::downloadLive2D).padLeft(8f).padTop(3f).width(250f).get();
            c = t.button(Core.bundle.get("cancel"), Icon.left, this::hide).padLeft(8f).padTop(3f).width(250f).get();
        }).center();
        show();
    }

    @Override
    public Dialog show() {
        a.actions(Actions.fadeIn(1f), Actions.visible(true));
        b.actions(Actions.fadeIn(2f), Actions.visible(true));
        c.actions(Actions.fadeIn(3f), Actions.visible(true));
        return super.show();
    }

    @Override
    public void hide() {
        a.clearActions();
        b.clearActions();
        c.clearActions();
        super.hide();
    }
}
