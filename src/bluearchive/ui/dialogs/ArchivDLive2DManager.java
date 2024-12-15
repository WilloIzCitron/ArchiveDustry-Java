package bluearchive.ui.dialogs;

import arc.Core;
import arc.graphics.Color;
import arc.scene.ui.Dialog;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.*;
import arc.util.Time;
import bluearchive.ui.*;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.*;
import static bluearchive.l2d.Live2DBackgrounds.live2ds;

public class ArchivDLive2DManager extends BaseDialog {
    //TODO: make another dialog in order to view Live2D(Recollection) Metadata
    Dialog restartDialog = new Dialog(){{
        cont.add(new Table(){{
            cont.image(atlas.find("bluearchive-arona-happy")).size(256,256).center().row();
            new Table(){{
                    cont.add("Arona").left().row();
                    cont.add(bundle.get("ba-restartDialogText")).right().row();
                    cont.button(bundle.get("ba-restartConfirm"), Styles.flatt, () -> Core.app.exit()).size(250f, 50f);
                }};
        }});
    }};
    Table tabl = new Table(){{
            live2ds.forEach(l -> {
                cont.button(con -> {
                con.row();
                con.add(l.displayName).row();
                con.add(bundle.formatString(bundle.get("ba-l2d.author"), l.author)).color(Color.gray).bottom().row();
            }, Styles.flatBordert, () -> { Core.settings.put("setL2D-new", l.name); restartDialog.show();}).growX().row();
            });
    }};
    ScrollPane scrl = new ScrollPane(tabl, Styles.defaultPane);

    public ArchivDLive2DManager(){
        super(bundle.get("ba-l2dManager"));
        addCloseButton();
        scrl.draw();
        cont.pane(tabl).scrollX(false).row();
        show();
    }
}
