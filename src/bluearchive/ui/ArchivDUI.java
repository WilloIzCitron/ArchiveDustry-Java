package bluearchive.ui;

import bluearchive.ui.dialogs.*;

public class ArchivDUI {
    
    public static ArchivDCreditsDialog creditsDialog = new ArchivDCreditsDialog();
    public static ArchivDFirstTimeDialog firstTimeDialog = new ArchivDFirstTimeDialog();
    //public static ArchivDMenu menuDialog = new ArchivDMenu(); wip

    public static void init() {
        creditsDialog.hide();
        firstTimeDialog.hide();
    }

    public static void showCredits() {
        creditsDialog.show();
    }
}
