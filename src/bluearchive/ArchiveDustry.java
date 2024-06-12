package bluearchive;

import arc.*;
import arc.audio.*;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.scene.utils.Elem;
import arc.util.*;
import bluearchive.events.ArchivDClientLoad;
import bluearchive.ui.*;
import bluearchive.ui.dialogs.*;
import mindustry.gen.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import bluearchive.units.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.*;

import static mindustry.Vars.*;

public class ArchiveDustry extends Mod {
    public static Music recollectionMusic;
    public ArchiveDustry() {
        ArchivDClientLoad.load();
    }
    @Override
    public void init(){
        ArchivDMusic.load();

        if(Core.settings.getBool("ba-addHalo", true)) {
            UnitHalo.init();
        }
        UnitSound.init();
        if (!Core.graphics.isPortrait() && Core.settings.getBool("enableL2D", true)) {
            switch (Core.settings.getInt("setL2D")) {
                case 1:
                    ArchivDBackground.buildL2D("kotama", 68, 5f);
                    recollectionMusic = ArchivDMusic.sugar;
                    break;
                case 2:
                    ArchivDBackground.buildL2D("noa", 68, 5f);
                    recollectionMusic = ArchivDMusic.aira;
                    break;
                case 3:
                    ArchivDBackground.buildL2D("mika", 98, 5f);
                    recollectionMusic = ArchivDMusic.moment;
                    break;
                case 4:
                    ArchivDBackground.buildL2D("saori", 49, 5f);
                    recollectionMusic = Musics.fine;
                    break;
                case 5:
                    ArchivDBackground.buildL2D("arisu", 58, 5f);
                    recollectionMusic = tree.loadMusic("somedaySometime");
                    break;
                case 6:
                    ArchivDBackground.buildL2D("reisa", 146, 4f);
                    recollectionMusic = tree.loadMusic("somedaySometime");
                    break;
            }
        }
        ArchivDSettings.loadSettings();
        switch (Core.settings.getInt("setSong")) {
            case 1:
                Musics.menu = tree.loadMusic("menucm");
                break;
            case 2:
                Musics.menu = tree.loadMusic("menure-aoh");
                break;
            case 3:
                Musics.menu = recollectionMusic;
                break;
        }

    }
}

