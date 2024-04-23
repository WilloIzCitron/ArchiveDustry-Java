package bluearchive;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.Color;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.*;
import arc.scene.ui.Image;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import bluearchive.units.*;
import mindustry.net.Net;
import mindustry.ui.Links;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.*;

import java.awt.*;

import static mindustry.Vars.*;

public class ArchiveDustry extends Mod {

    public ArchiveDustry() {

        //listen for game load event
        Events.on(ClientLoadEvent.class, event -> {
            Log.info("[AchivD] ArchiveDustry fully loaded!.");
            if(Core.settings.getBool("ba-firstTime")) {
                BaseDialog dialog = new BaseDialog(Core.bundle.get("ba-firstTimeDialog.title"));
                dialog.cont.image(Core.atlas.find("bluearchive-mikalove")).pad(20).size(200f, 200f).row();
                dialog.cont.add(Core.bundle.get("ba-firstTimeDialog.description")).row();
                dialog.cont.button(Core.bundle.get("ba-firstTimeDialog.button"), dialog::hide).size(200f, 50f).row();
                dialog.show();
            }
            tree.loadMusic("research").setLooping(true);
            tree.loadMusic("database").setLooping(true);
            tree.loadMusic("loadout").setLooping(true);
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
            ui.schematics.shown(() -> {
                tree.loadMusic("loadout").play();
            });
            ui.schematics.update(() -> {
                if (state.isMenu() || ui.planet.isShown() || ui.editor.isShown() || state.rules.editor) {
                    Reflect.set(control.sound, "fade", 0f);
                    Reflect.invoke(control.sound, "silence");
                    if (soundControlPlaying() != null) {Reflect.invoke(control.sound, "silence");} //Counteract fade in
                }
            });
            ui.schematics.hidden(() -> {
                tree.loadMusic("loadout").stop();
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
        if(Core.settings.getBool("ba-addHalo")) {
            UnitHalo.init();
        }
        loadSettings();
    }

    void loadSettings(){
        ui.settings.addCategory("ArchiveDustry", t -> {
            t.pref(new Banner("bluearchive-logo", -1));
            t.checkPref("ba-firstTime", true);
            t.checkPref("ba-addHalo", true);
            new Links.LinkEntry("ba-youtube", "https://www.youtube.com/channel/UCsrnDYrkovQhCCE8kwKcvKQ", Icon.play, Color.red);
        });
    }

    /** Not a setting, but rather adds an image to the settings menu. */
    static class Banner extends Setting{
        float width;

        public Banner(String name, float width){
            super(name);
            this.width = width;
        }

        @Override
        public void add(SettingsTable table){
            Image i = new Image(new TextureRegionDrawable(Core.atlas.find(name)), Scaling.fit);
            Cell<Image> ci = table.add(i).padTop(3f);

            if(width > 0){
                ci.width(width);
            }else{
                ci.grow();
            }

            table.row();
        }
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

