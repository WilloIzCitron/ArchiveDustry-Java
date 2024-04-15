package bluearchive;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

public class ArchiveDustry extends Mod {

    public ArchiveDustry() {
        Log.info("ArchiveDustry Loaded!.");

        //listen for game load event
        Events.on(ClientLoadEvent.class, event -> {
            tree.loadMusic("research").setLooping(true);
            tree.loadMusic("database").setLooping(true);
            ui.research.shown(() -> {
                tree.loadMusic("research").play();
            });
            ui.research.update(() -> {
                if (state.isMenu() || ui.planet.isShown() || ui.editor.isShown() || state.rules.editor) {
                    Reflect.set(control.sound, "fade", 0f);
                    Reflect.invoke(control.sound, "silence");
                    if (soundControlPlaying() != null) {Reflect.invoke(control.sound, "silence");} //Counteract fade in
                } else {
                    Reflect.set(control.sound, "fade", 0f);
                    Reflect.invoke(control.sound, "silence");
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
                    Reflect.set(control.sound, "fade", 0f);
                    Reflect.invoke(control.sound, "silence");
                    if (soundControlPlaying() != null) {Reflect.invoke(control.sound, "silence");} //Counteract fade in
                } else {
                    Reflect.set(control.sound, "fade", 0f);
                    Reflect.invoke(control.sound, "silence");
                }
            });
            ui.database.hidden(() -> {
                tree.loadMusic("database").stop();
            });
        });


        Events.on(WinEvent.class, winner -> {
            tree.loadMusic("win").play();
            ui.restart.hidden(() -> {
                tree.loadMusic("win").stop();

            });
        });
        Events.on(LoseEvent.class, winner -> {
            tree.loadMusic("lose").play();
            ui.restart.hidden(() -> {
                tree.loadMusic("lose").stop();
            });
        });


        // sector captured = win
        Events.on(SectorCaptureEvent.class, e -> {
            tree.loadMusic("win").play();
        });
    }

    protected static Music soundControlPlaying() {
        if (state.isMenu()) {
            if (ui.planet.isShown()) {
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

