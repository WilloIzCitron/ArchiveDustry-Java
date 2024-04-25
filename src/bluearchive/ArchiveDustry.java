package bluearchive;

import arc.*;
import arc.audio.*;
import arc.graphics.Color;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextArea;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.*;
import arc.scene.utils.Elem;
import arc.util.*;
import mindustry.gen.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import bluearchive.units.*;
import mindustry.ui.Links;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.*;

import static mindustry.Vars.*;

public class ArchiveDustry extends Mod {

    public ArchiveDustry() {
        //listen for game load event
        Events.on(ClientLoadEvent.class, event -> {
            Log.info("[ArchivD] ArchiveDustry fully loaded!.");
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
        new Links.LinkEntry("ba-youtube", "https://www.youtube.com/channel/UCsrnDYrkovQhCCE8kwKcvKQ", Icon.play, Color.red);
        Log.info("[ArchivD] Link Generated!");
        if(Core.settings.getBool("Heart of Iron 4 Main Menu Soundtrack")){
            Musics.menu = tree.loadMusic("menuhoi4");
        } else {
            Musics.menu = tree.loadMusic("menuba");
        }
    }

    void loadSettings(){
        ui.settings.addCategory("ArchiveDustry", t -> {
            t.pref(new Banner("bluearchive-logo", -1));
            t.pref(new TextSeparator(Core.bundle.get("setting.category.general-setting")));
            t.pref(new Separator(4));
            t.checkPref("ba-firstTime", true);
            t.checkPref("ba-addHalo", true);
            t.pref(new TextSeparator(Core.bundle.get("setting.category.links")));
            t.pref(new Separator(4));
            t.pref(new ButtonSetting("ba-youtube", Icon.play, () -> {
                Core.app.openURI("https://www.youtube.com/channel/UCsrnDYrkovQhCCE8kwKcvKQ");
            }, 32));
            t.pref(new ButtonSetting("ba-github", Icon.github, () -> {
                Core.app.openURI("https://www.github.com/willoizcitron/archivedustry-java");
            }, 32));
            t.pref(new Separator(2));
            if (OS.username.startsWith("willoizcitron")){
                t.pref(new Separator(2));
                t.pref(new TextSeparator("< Developer Settings >"));
                t.checkPref("Heart of Iron 4 Main Menu Soundtrack", false, b -> {
                if(b) {
                    Musics.menu = tree.loadMusic("menuhoi4");
                } else {
                    Musics.menu = tree.loadMusic("menuba");
                }
                });
            }
        });
    }

    /** Not a setting, but rather a button in the settings menu. */
    static class ButtonSetting extends Setting{
        Drawable icon;
        Runnable listener;
        float iconSize;

        public ButtonSetting(String name, Drawable icon, Runnable listener, float iconSize){
            super(name);
            this.icon = icon;
            this.listener = listener;
            this.iconSize = iconSize;
        }

        @Override
        public void add(SettingsTable table){
            ImageButton b = Elem.newImageButton(icon, listener);
            b.resizeImage(iconSize);
            b.label(() -> title).padLeft(6).growX();
            b.left();

            addDesc(table.add(b).left().padTop(3f).get());
            table.row();
        }
    }

static class TextSeparator extends Setting {
        String text;
        public TextSeparator(String text){
            super("");
            this.text = text;
        }
        @Override
        public void add(SettingsTable table){
            TextField b = new TextField(text);
            b.setText(text);
            b.setDisabled(true);
            b.setAlignment(1);
            table.add(b).growX();
            table.row();
        }
}
    static class Separator extends Setting{
        float height;
        public Separator(float height){
            super("");
            this.height = height;
        }

        @Override
        public void add(SettingsTable table){
            table.image(Tex.clear).height(height).padTop(3f);
            table.row();
        }
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

