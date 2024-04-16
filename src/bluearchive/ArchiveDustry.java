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
import bluearchive.units.*;
import mindustry.net.Net;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.*;

public class ArchiveDustry extends Mod {

    public ArchiveDustry() {

        //listen for game load event
        Events.on(ClientLoadEvent.class, event -> {
            BaseDialog dialog = new BaseDialog("Thanks for using ArchiveDustry!");
            dialog.cont.image(Core.atlas.find("bluearchive-mikalove")).pad(30).row();
            dialog.cont.add("Hi Sensei! you just installed this mod.\nAnyway it still in development by the creator of ArchiveDustry, [accent]WilloIzCitron[]. \n The creator need to take a long time for making the new content in the future. Thank you!").row();
            dialog.cont.button("Ok Misono Mika.", dialog::hide).size(200f, 50f).row();
            dialog.show();
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

    @Override
    public void init(){
        UnitHalo.init();
        Log.info("[AchivD] ArchiveDustry fully loaded!.");
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

