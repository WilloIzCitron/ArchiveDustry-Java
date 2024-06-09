package bluearchive.events;

import arc.Core;
import arc.Events;
import arc.audio.Music;
import arc.util.Log;
import arc.util.Reflect;
import arc.util.Time;
import bluearchive.ui.dialogs.ArchivDFirstTimeDialog;
import mindustry.game.EventType;
import mindustry.gen.Musics;

import static mindustry.Vars.*;
import static mindustry.Vars.tree;

public class ArchivDClientLoad {
    public static void load() {
        Events.on(EventType.ClientLoadEvent.class, event -> {
            Log.infoTag("ArchiveDustry", "Fully Loaded!");
            if (Core.settings.getBool("ba-firstTime")) {
                new ArchivDFirstTimeDialog();
            }
            tree.loadMusic("research").setLooping(true);
            tree.loadMusic("database").setLooping(true);
            tree.loadMusic("loadout").setLooping(true);
            ui.research.shown(() -> {
                tree.loadMusic("research").play();
            });
            ui.research.update(() -> {
                if (state.isMenu() || ui.planet.isShown() || ui.editor.isShown() || state.rules.editor) {
                    control.sound.stop();
                    if (soundControlPlaying() != null) {
                        control.sound.stop();
                    } //Counteract fade in
                }
            });
            ui.research.hidden(() -> {
                tree.loadMusic("research").stop();
            });
            ui.database.shown(() -> {
                tree.loadMusic("database").play();
            });
            ui.database.update(() -> {
                if (state.isMenu() || ui.planet.isShown() || ui.editor.isShown() || state.rules.editor) {
                    control.sound.stop();
                    if (soundControlPlaying() != null) {
                        control.sound.stop();
                    } //Counteract fade in
                }
            });
            ui.database.hidden(() -> {
                tree.loadMusic("database").stop();
            });
            ui.schematics.shown(() -> {
                tree.loadMusic("loadout").play();
            });
            ui.schematics.update(() -> {
                if (state.isMenu() || ui.planet.isShown() || ui.editor.isShown() || state.rules.editor) {
                    control.sound.stop();
                    if (soundControlPlaying() != null) {
                        control.sound.stop();
                    } //Counteract fade in
                }
            });
            ui.schematics.hidden(() -> {
                tree.loadMusic("loadout").stop();
            });
        });


        Events.on(EventType.WinEvent.class, winner -> {
            Music currentPlay = Reflect.get(control.sound, "current");
            if (currentPlay != null) {
                currentPlay.stop();
            }
            tree.loadMusic("win").play();
            ui.restart.hidden(() -> {
                tree.loadMusic("win").stop();

            });
        });
        Events.on(EventType.LoseEvent.class, winner -> {
            Music currentPlay = Reflect.get(control.sound, "current");
            if (currentPlay != null) {
                currentPlay.stop();
            }
            tree.loadMusic("lose").play();
            ui.restart.hidden(() -> {
                tree.loadMusic("lose").stop();
            });
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
