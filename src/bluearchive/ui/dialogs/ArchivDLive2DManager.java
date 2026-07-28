package bluearchive.ui.dialogs;

import arc.Core;
import arc.graphics.Color;
import arc.scene.ui.Dialog;
import arc.scene.ui.layout.*;
import arc.util.Log;
import bluearchive.ArchiveDustry;
import bluearchive.l2d.Live2DBackgroundsExperimental;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.*;
import static bluearchive.l2d.Live2DBackgroundsExperimental.live2ds;
import static mindustry.Vars.dataDirectory;

public class ArchivDLive2DManager extends BaseDialog {
    private Table l2dtabl;
    private boolean L2DLoaded = false;
    //TODO: make another dialog in order to view Live2D(Recollection) Metadata
    Dialog restartDialog = new Dialog(){{
        cont.add(new Table(){{
            cont.image(atlas.find("bluearchive-arona-happy")).size(256,256).center().row();
            new Table(){{
                    cont.add("Arona").left().row();
                    cont.add(bundle.get("ba-restartDialogText")).right().row();
                    cont.button(bundle.get("ba-restartConfirm"), () -> Core.app.exit()).size(250f, 50f);
                }};
        }});
    }};

    public ArchivDLive2DManager(){
        super(bundle.get("ba-l2dManager"));
        addCloseButton();
        onResize(this::rebuild);
        cont.pane(tablbrw -> {
            tablbrw.margin(10f).top();
            l2dtabl = tablbrw;
        }).grow().scrollX(false).top();
        cont.row();
        makeButtonOverlay();
        shown(this::rebuild);
        show();
        hidden(() -> {L2DLoaded = false;});
    }

    private void rebuild(){
        live2ds.each(l -> l.dispose());
        live2ds.clear();
        l2dtabl.clear();
        l2dtabl.add("@loading");

        if(!L2DLoaded){
            dataDirectory.child("live2d").walk(f -> {
                ArchiveDustry.foundL2D++;
                try {
                    Live2DBackgroundsExperimental.load(f);
                    ArchiveDustry.loadedL2D++;
                } catch (Exception e) {
                    Log.err(e);
                    ArchiveDustry.erroredL2D++;
                }
            });
            l2dtabl.clear();
            live2ds.each(l -> l2dtabl.button(con -> {
                con.margin(3f);
                con.row();
                con.add(l.displayName).row();
                con.add(bundle.formatString(bundle.get("ba-l2d.author"), l.author)).color(Color.gray).bottom().row();
            }, Styles.flatBordert, () -> { Core.settings.put("setL2D-new", l.name); restartDialog.show();}).growX().row());
            L2DLoaded = true;
        }
    }
}
