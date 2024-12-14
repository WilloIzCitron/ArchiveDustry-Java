package bluearchive;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.scene.style.TextureRegionDrawable;
import arc.util.Log;
import bluearchive.events.ArchivDClientLoad;
import bluearchive.l2d.Live2DBackgrounds;
import bluearchive.ui.*;
import bluearchive.ui.overrides.ArchivDLoadingFragment;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.mod.*;
import bluearchive.units.*;

import static mindustry.Vars.*;

public class ArchiveDustry extends Mod {
    public static Music recollectionMusic;
    int foundL2D, loadedL2D, erroredL2D;

    public ArchiveDustry() {
        ArchivDClientLoad.load();
    }
    @Override
    public void init(){

        //Use Pal.accent first... experimental Styles
        //ArchivDStyles.load();
        dataDirectory.child("live2d").walk(f -> {
            foundL2D++;
            try {
                Live2DBackgrounds.load(f);
                loadedL2D++;
            } catch(Exception e) {
                Log.err(e);
                erroredL2D++;
            }
        });
        ArchivDMusic.load();
        ArchivDLoadingFragment.init();

        if(Core.settings.getBool("ba-addHalo", true)) {
            UnitHalo.init();
        }
        UnitSound.init();
        if (!Core.graphics.isPortrait() && Core.settings.getBool("enableL2D", true)) {
            switch (Core.settings.getInt("setL2D")) {
                case 1:
                    ArchivDBackground.buildL2D("kotama");
                    recollectionMusic = tree.loadMusic("menuaira");
                    break;
                case 2:
                    ArchivDBackground.buildL2D("noa");
                    recollectionMusic = tree.loadMusic("menurcl");
                    break;
                case 3:
                    ArchivDBackground.buildL2D("mika");
                    recollectionMusic = tree.loadMusic("moment");
                    break;
                case 4:
                    ArchivDBackground.buildL2D("saori");
                    recollectionMusic = Musics.fine;
                    break;
                case 5:
                    ArchivDBackground.buildL2D("arisu");
                    recollectionMusic = tree.loadMusic("somedaySometime");
                    break;
                case 6:
                    ArchivDBackground.buildL2D("reisa");
                    recollectionMusic = tree.loadMusic("somedaySometime");
                    break;
                case 7:
                    ArchivDBackground.buildL2D("mina");
                    recollectionMusic = tree.loadMusic("t171");
                    break;
                case 8:
                    ArchivDBackground.buildL2D("nonomi");
                    recollectionMusic = tree.loadMusic("cat");
                    break;
                case 9:
                    ArchivDBackground.buildL2D("kirinoSwimsuit");
                    break;
                case 10:
                    ArchivDBackground.buildL2D("shirokoTerror");
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

