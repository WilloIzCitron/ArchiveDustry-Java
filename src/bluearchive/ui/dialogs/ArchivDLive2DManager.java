package bluearchive.ui.dialogs;

import arc.Core;
import arc.graphics.Color;
import arc.scene.ui.Dialog;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.*;
import arc.util.Time;
import bluearchive.ui.ArchivDStyles;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.*;

public class ArchivDLive2DManager extends BaseDialog {
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
        for (int i = 1; i < 11; i++) {
            int finalI = i;
            cont.button(con -> {
                            con.row();
                            con.image(atlas.find("bluearchive-l2d"+finalI) != atlas.find("error") ? atlas.find("bluearchive-l2d"+finalI) : atlas.find("error")).size(270/2f,213/2f).left().padLeft(2f).row();
                            con.add(bundle.get("ba-l2d"+ finalI +".name")).row();
                            con.add(bundle.get("ba-l2d"+ finalI +".author")).color(Color.gray).bottom().row();
                            }, ArchivDStyles.flatBordert, () -> { Core.settings.put("setL2D", finalI); restartDialog.show();}).size((280/2f)+48f,(235/2f)+64f);
        }
    }};
    ScrollPane scrl = new ScrollPane(tabl);

    public ArchivDLive2DManager(){
        super(bundle.get("ba-l2dManager"));
        addCloseButton();
        cont.add(tabl).row();
        scrl.draw();
        scrl.setWidth(10f);
        scrl.act(Time.delta);
        show();
    }
}
