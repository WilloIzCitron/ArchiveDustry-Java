package bluearchive.ui.dialogs;

import arc.Core;
import mindustry.ui.dialogs.BaseDialog;

public class ArchivDFirstTimeDialog extends BaseDialog {
    public ArchivDFirstTimeDialog() {
        super(Core.bundle.get("ba-firstTimeDialog.title"));
        cont.image(Core.atlas.find("bluearchive-mikalove")).pad(20).size(200f, 200f).row();
        cont.add(Core.bundle.get("ba-firstTimeDialog.description")).row();
        cont.button(Core.bundle.get("ba-firstTimeDialog.button"), this::hide).size(200f, 50f).row();
        show();
    }
}
