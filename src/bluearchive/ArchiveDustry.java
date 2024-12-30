package bluearchive;

import arc.*;
import arc.audio.*;
import arc.util.Log;
import arc.util.Reflect;
import arc.util.Time;
import bluearchive.l2d.Live2DBackgrounds;
import bluearchive.ui.*;
import bluearchive.ui.dialogs.ArchivDFirstTimeDialog;
import bluearchive.ui.overrides.ArchivDLoadingFragment;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.mod.*;
import bluearchive.units.*;

import static mindustry.Vars.*;

public class ArchiveDustry extends Mod {
    public static Music recollectionMusic;
    int foundL2D, loadedL2D, erroredL2D;

    public ArchiveDustry() {

    }
    @Override
    public void init(){
        //Use Pal.accent first... experimental Styles
        //ArchivDStyles.load();
        ArchivDLoadingFragment.init();
        ArchivDSettings.loadSettings();
        if(Core.settings.getBool("ba-addHalo", true)) UnitHalo.init();
        UnitSound.init();
        ArchivDMusic.load();
        if(Core.settings.getBool("enableL2D")) {
            dataDirectory.child("live2d").walk(f -> {
                foundL2D++;
                try {
                    Live2DBackgrounds.load(f);
                    loadedL2D++;
                } catch (Exception e) {
                    Log.err(e);
                    erroredL2D++;
                }
            });
        }
        if (!Core.graphics.isPortrait() && Core.settings.getBool("enableL2D") && Core.settings.has("setL2D-new")) {
//            switch (Core.settings.getInt("setL2D")) {
//                case 1:
//                    ArchivDBackground.buildL2D("kotama");
//                    recollectionMusic = tree.loadMusic("menuaira");
//                    break;
//                case 2:
//                    ArchivDBackground.buildL2D("noa");
//                    recollectionMusic = tree.loadMusic("menurcl");
//                    break;
//                case 3:
//                    ArchivDBackground.buildL2D("mika");
//                    recollectionMusic = tree.loadMusic("moment");
//                    break;
//                case 4:
//                    ArchivDBackground.buildL2D("saori");
//                    recollectionMusic = Musics.fine;
//                    break;
//                case 5:
//                    ArchivDBackground.buildL2D("arisu");
//                    recollectionMusic = tree.loadMusic("somedaySometime");
//                    break;
//                case 6:
//                    ArchivDBackground.buildL2D("reisa");
//                    recollectionMusic = tree.loadMusic("somedaySometime");
//                    break;
//                case 7:
//                    ArchivDBackground.buildL2D("mina");
//                    recollectionMusic = tree.loadMusic("t171");
//                    break;
//                case 8:
//                    ArchivDBackground.buildL2D("nonomi");
//                    recollectionMusic = tree.loadMusic("cat");
//                    break;
            if (!Core.settings.getString("setL2D-new").isEmpty()) {
                ArchivDBackground.buildL2D(Core.settings.getString("setL2D-new"));
            } else {
                Core.settings.put("enableL2D", false); //fallback if setl2d is blank
                Core.settings.put("setSong", 1);
            }
        }
            Events.on(EventType.ClientLoadEvent.class, event -> {
                switch (Core.settings.getInt("setSong")) {
                    case 1:
                        if (Musics.menu != tree.loadMusic("menucm")) Musics.menu = tree.loadMusic("menucm");
                        break;
                    case 2:
                        if (Musics.menu != tree.loadMusic("menure-aoh")) Musics.menu = tree.loadMusic("menure-aoh");
                        break;
                    case 3:
                        if (Musics.menu != recollectionMusic) Musics.menu = recollectionMusic;
                        break;
                }
                Log.infoTag("ArchiveDustry", "Fully Loaded!");
                if (Core.settings.getBool("ba-firstTime")) {
                    new ArchivDFirstTimeDialog();
                }
                tree.loadMusic("research").setLooping(true);
                tree.loadMusic("database").setLooping(true);
                tree.loadMusic("loadout").setLooping(true);
                ui.research.shown(() -> tree.loadMusic("research").play());
                ui.research.update(() -> {
                    if (state.isMenu() || ui.planet.isShown() || ui.editor.isShown() || state.rules.editor) {
                        control.sound.stop();
                        if (soundControlPlaying() != null) {
                            control.sound.stop();
                        } //Counteract fade in
                    }
                });
                ui.research.hidden(() -> tree.loadMusic("research").stop());
                ui.database.shown(() -> tree.loadMusic("database").play());
                ui.database.update(() -> {
                    if (state.isMenu() || ui.planet.isShown() || ui.editor.isShown() || state.rules.editor) {
                        control.sound.stop();
                        if (soundControlPlaying() != null) {
                            control.sound.stop();
                        } //Counteract fade in
                    }
                });
                ui.database.hidden(() -> tree.loadMusic("database").stop());
                ui.schematics.shown(() -> tree.loadMusic("loadout").play());
                ui.schematics.update(() -> {
                    if (state.isMenu() || ui.planet.isShown() || ui.editor.isShown() || state.rules.editor) {
                        control.sound.stop();
                        if (soundControlPlaying() != null) {
                            control.sound.stop();
                        } //Counteract fade in
                    }
                });
                ui.schematics.hidden(() -> tree.loadMusic("loadout").stop());
            });

            Events.on(EventType.WinEvent.class, winner -> {
                Music currentPlay = Reflect.get(control.sound, "current");
                if (currentPlay != null) {
                    currentPlay.stop();
                }
                tree.loadMusic("win").play();
                ui.restart.hidden(() -> tree.loadMusic("win").stop());
            });
            Events.on(EventType.LoseEvent.class, winner -> {
                Music currentPlay = Reflect.get(control.sound, "current");
                if (currentPlay != null) {
                    currentPlay.stop();
                }
                tree.loadMusic("lose").play();
                ui.restart.hidden(() -> tree.loadMusic("lose").stop());
            });


            // sector captured = win
            Events.on(EventType.SectorCaptureEvent.class, e -> {
                Music currentPlay = Reflect.get(control.sound, "current");
                if (currentPlay != null) {
                    currentPlay.stop();
                }
                tree.loadMusic("win").play();
                Time.run(306f, () -> {
                    tree.loadMusic("win").stop();
                    Time.clear();
                });
            });
    }
    public static Music soundControlPlaying() {
        if (state.isMenu()) {
            if (ui.planet.isShown()) {
                //for bleeding edge
                //if(ui.planet.state.planet.launchMusic != null) return ui.planet.state.planet.launchMusic);
                return Musics.launch;
            } else if (ui.editor.isShown()) {
                return Musics.editor;
            } else {
                return Musics.menu;
            }
        } else if (state.rules.editor) {
            return Musics.editor;
        }
        return null;
    }
}

