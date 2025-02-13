package bluearchive;

import arc.*;
import arc.audio.*;
import arc.files.Fi;
import arc.struct.*;
import arc.util.*;
import bluearchive.l2d.Live2DBackgrounds;
import bluearchive.ui.*;
import bluearchive.ui.dialogs.ArchivDFirstTimeDialog;
import bluearchive.ui.overrides.ArchivDLoadingFragment;
import mindustry.core.Version;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.mod.*;
import bluearchive.units.*;
import arc.math.*;

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
        if(!mobile || !headless) Core.graphics.setTitle(Core.settings.getAppName()+" v"+Version.buildString()+" | ArchiveDustry v"+mods.getMod("bluearchive").meta.version+ " | "+RandomMessage());
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

    static String RandomMessage(){
        Fi msg = tree.get("text/messages.txt");
        if(!msg.exists()) return "null";
        Seq<String> strings = Seq.with(msg.readString("UTF-8").split("\n"));
        int stringLength = strings.size;
        return strings.get(Mathf.random(stringLength));
    }
}

