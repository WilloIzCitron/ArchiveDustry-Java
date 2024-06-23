package bluearchive.ui.dialogs;

import arc.Core;
import arc.graphics.Color;
import arc.scene.Group;
import arc.scene.ui.Dialog;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.*;

public class ArchivDLive2DManager extends BaseDialog {
    Dialog restartDialog = new Dialog(){{
        cont.add(new Table(){{
            cont.image(atlas.find("bluearchive-arona-happy")).size(256,256).row();
            new Table(){{
                    cont.add(bundle.get("ba-restartDialogText")).row();
                    cont.button(bundle.get("ba-restartConfirm"), Styles.flatt, () -> Core.app.exit()).size(250f, 50f);
                }};
        }}).style(Styles.grayi);
    }};
    Table tabl = new Table(){{
        for (int i = 1; i < 7; i++) {
            int finalI = i;
            new Group() {{
                add(bundle.get("ba-l2d"+ finalI +".name")).row();
                add(bundle.get("ba-l2d"+ finalI +".author")).color(Color.gray).row();
                button("Apply", () -> { Core.settings.put("setL2D", finalI); restartDialog.show();}).growX().row();
            }};
        }
    }};

    public ArchivDLive2DManager(){
        super(bundle.get("ba-l2dManager"));
        addCloseButton();
        cont.add(tabl).row();
        show();
    }
}
