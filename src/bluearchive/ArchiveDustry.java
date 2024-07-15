package bluearchive;

import arc.*;
import arc.audio.*;
import bluearchive.events.ArchivDClientLoad;
import bluearchive.ui.*;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.mod.*;
import bluearchive.units.*;

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
                    recollectionMusic = tree.loadMusic("menuaira");
                    break;
                case 2:
                    ArchivDBackground.buildL2D("noa", 68, 5f);
                    recollectionMusic = tree.loadMusic("menurcl");
                    break;
                case 3:
                    ArchivDBackground.buildL2D("mika", 98, 5f);
                    recollectionMusic = tree.loadMusic("moment");
                    break;
                case 4:
                    ArchivDBackground.buildL2D("saori", 49, 5f);
                    recollectionMusic = Musics.fine;
                    break;
                case 5:
                    ArchivDBackground.buildL2D("arisu", 150, 5f);
                    recollectionMusic = tree.loadMusic("somedaySometime");
                    break;
                case 6:
                    ArchivDBackground.buildL2D("reisa", 146, 4f);
                    recollectionMusic = tree.loadMusic("somedaySometime");
                    break;
                case 7:
                    ArchivDBackground.buildL2D("mina", 68, 3f);
                    recollectionMusic = tree.loadMusic("t171");
                    break;
                case 8:
                    ArchivDBackground.buildL2D("nonomi", 63, 5f);
                    recollectionMusic = tree.loadMusic("cat");
                    break;
            }
        }
        ArchivDSettings.loadSettings();
        Events.on(EventType.ClientLoadEvent.class, e -> {
            switch (Core.settings.getInt("setSong")) {
                case 1:
                    if(Musics.menu != tree.loadMusic("menucm")) Musics.menu = tree.loadMusic("menucm");
                    break;
                case 2:
                    if(Musics.menu != tree.loadMusic("menure-aoh")) Musics.menu = tree.loadMusic("menure-aoh");
                    break;
                case 3:
                    if(Musics.menu != recollectionMusic) Musics.menu = recollectionMusic;
                    break;
            }
        });
    }
}

