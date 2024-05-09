package bluearchive;

import arc.*;
import arc.audio.*;
import arc.graphics.Color;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.*;
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
            Time.run(306f, () -> {
                tree.loadMusic("win").stop();
                Time.clear();
            });
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
        if(Core.settings.getBool("ba-setSong")){
            Musics.menu = tree.loadMusic("menure-aoh");
        } else {
            Musics.menu = tree.loadMusic("menucm");
        }
    }

    void loadSettings(){
        ui.settings.addCategory("ArchiveDustry", t -> {
            t.pref(new Banner("bluearchive-logo", -1));
            t.pref(new TextSeparator(Core.bundle.get("setting.category.general-setting")));
            t.pref(new Separator(4));
            t.checkPref("ba-firstTime", true);
            t.checkPref("ba-addHalo", true);
            t.checkPref("ba-setSong", false, b -> {
                if (b) {
                    Musics.menu = tree.loadMusic("menure-aoh");
                } else {
                    Musics.menu = tree.loadMusic("menucm");
                }
            });
            t.pref(new TextSeparator(Core.bundle.get("setting.category.links")));
            t.pref(new Separator(4));
            t.pref(new ButtonSetting("ba-youtube", Icon.play, () -> {
                Core.app.openURI("https://www.youtube.com/channel/UCsrnDYrkovQhCCE8kwKcvKQ");
            }, 32));
            t.pref(new ButtonSetting("ba-github", Icon.github, () -> {
                Core.app.openURI("https://www.github.com/willoizcitron/archivedustry-java");
            }, 32));
            t.pref(new ButtonSetting("Credits", Icon.info, this::showCredits, 32));
            t.pref(new TextSeparator(Core.bundle.get("setting.category.mixer")));
            t.pref(new Separator(4));
            t.sliderPref("gameOver", 100,0, 100,1,i -> i + "%");
            t.sliderPref("research", 100,0, 100,1,i -> i + "%");
            t.sliderPref("coreDatabase", 100,0, 100,1,i -> i + "%");
            t.sliderPref("loadout", 100,0, 100,1,i -> i + "%");
            t.pref(new ButtonSetting("ba-mixer-apply", Icon.ok, () -> {
                tree.loadMusic("win").setVolume(Core.settings.getInt("gameOver") / 100f);
                tree.loadMusic("lose").setVolume(Core.settings.getInt("gameOver") / 100f);
                tree.loadMusic("research").setVolume(Core.settings.getInt("research") / 100f);
                tree.loadMusic("database").setVolume(Core.settings.getInt("coreDatabase") / 100f);
                tree.loadMusic("loadout").setVolume(Core.settings.getInt("loadout") / 100f);
            }, 32));
            t.pref(new Separator(2));
            if (OS.username.startsWith("willoizcitron")){
                t.pref(new Separator(2));
                t.pref(new TextSeparator("< Developer Settings >"));
                };
        });
    }

    public void showCredits() {
        BaseDialog credit = new BaseDialog("Credits");
        credit.addCloseButton();
        Table in = new Table();
        ScrollPane pane = new ScrollPane(in);
        credit.cont.pane(new Table() {{
            center();
            image(Tex.clear).height(25).padTop(3f).row();
            image(Core.atlas.find("bluearchive-logo")).row();
            image(Tex.clear).height(25f).padTop(3f).row();
            add("Developed by").row();
            image(Tex.clear).height(5f).padTop(3f).row();
            add("WilloIzCitron").row();
            image(Tex.clear).height(25f).padTop(3f).row();
            image(Tex.clear).height(5f).padTop(3f).row();
            add("Music by").row();
            add("Nor").row();
            add("Mitsukiyo").row();
            add("KARUT").row();
            image(Tex.clear).height(25f).padTop(3f).row();
            add("Music list").row();
            image(Tex.clear).height(5f).padTop(3f).row();
            add("menu.ogg = Constant Moderato & RE Aoharu").row();
            add("launch.ogg = Shooting Stars").row();
            add("game1.ogg = Rolling Beat").row();
            add("game2.ogg = Acceleration").row();
            add("game3.ogg = KIRISAME").row();
            add("game4.ogg = Midnight Trip").row();
            add("game6.ogg = Formless Dream").row();
            add("game7.ogg = Vivid Night").row();
            add("game8.ogg = Crucial Issue").row();
            add("game9.ogg = KARAKURhythm").row();
            add("editor.ogg = Mischievous Step").row();
            add("land.ogg = Aoharu (Intro Sampling)").row();
            add("boss1.ogg = Endless Carnival").row();
            add("boss2.ogg = Out of Control").row();
            add("fine.ogg = Alkaline Tears").row();
            image(Tex.clear).height(10f).padTop(3f).row();
            add("lose.ogg (additional) = Fade Out").row();
            add("win.ogg (additional) = Party Time").row();
            add("research.ogg (additional) = Future Lab").row();
            add("database.ogg (additional) = Future Bossa").row();
            add("loadout.ogg (additional) = MX Adventure").row();
            image(Tex.clear).height(25f).padTop(3f).row();
            add("Legal Notice").row();
            image(Tex.clear).height(5f).padTop(3f).row();
            add("This mod is fanmade! and also obey the Fankit Guidelines.").row();
            add("THIS MOD NOT INTENDED FOR COMMERCIAL USE!").row();
            image(Tex.clear).height(2f).padTop(3f).row();
            image(Tex.clear).height(1f).padTop(3f).row();
            add("Mindustry is developed by Anuke, and was licensed to GNU GPLv3.0").row();
            image(Tex.clear).height(1f).padTop(3f).row();
            add("This mod is MIT Licensed").row();
            image(Tex.clear).height(25f).padTop(25f).row();
            add("Blue Archive is copyrighted to Nexon, Nexon Games and Yostar. All Rights Reserved").row();
            image(Tex.clear).height(1f).padTop(25f).row();
            image(Core.atlas.find("bluearchive-creditpart"));
            image(Tex.clear).height(25f).padTop(25f).row();
            //logo for
        }}).growX();
        credit.show();
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

