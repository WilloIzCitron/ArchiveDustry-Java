package bluearchive;

import arc.*;
import arc.audio.*;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.scene.utils.Elem;
import arc.util.*;
import bluearchive.ui.*;
import bluearchive.ui.dialogs.*;
import mindustry.gen.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import bluearchive.units.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.*;

import static mindustry.Vars.*;

public class ArchiveDustry extends Mod {
    public static Music recollectionMusic;
    public ArchiveDustry() {
        //listen for game load event
        Events.on(ClientLoadEvent.class, event -> {
            Log.infoTag("ArchiveDustry", "Fully Loaded!");
            if(Core.settings.getBool("ba-firstTime")) {
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
                    if (soundControlPlaying() != null) {control.sound.stop();} //Counteract fade in
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
                    if (soundControlPlaying() != null) {control.sound.stop();} //Counteract fade in
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
                    if (soundControlPlaying() != null) {control.sound.stop();} //Counteract fade in
                }
            });
            ui.schematics.hidden(() -> {
                tree.loadMusic("loadout").stop();
            });
        });




        Events.on(WinEvent.class, winner -> {
            Music currentPlay = Reflect.get(control.sound, "current");
            if(currentPlay != null) {
                currentPlay.stop();
            }
            tree.loadMusic("win").play();
            ui.restart.hidden(() -> {
                tree.loadMusic("win").stop();

            });
        });
        Events.on(LoseEvent.class, winner -> {
            Music currentPlay = Reflect.get(control.sound, "current");
            if(currentPlay != null) {
                currentPlay.stop();
            }
            tree.loadMusic("lose").play();
            ui.restart.hidden(() -> {
                tree.loadMusic("lose").stop();
            });
        });


        // sector captured = win
        Events.on(SectorCaptureEvent.class, e -> {
            Music currentPlay = Reflect.get(control.sound, "current");
            if(currentPlay != null) {
                currentPlay.stop();
            }
            tree.loadMusic("win").play();
            Time.run(306f, () -> {
                tree.loadMusic("win").stop();
                Time.clear();
            });
        });
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
                    recollectionMusic = ArchivDMusic.sugar;
                    break;
                case 2:
                    ArchivDBackground.buildL2D("noa", 68, 5f);
                    recollectionMusic = ArchivDMusic.aira;
                    break;
                case 3:
                    ArchivDBackground.buildL2D("mika", 98, 5f);
                    recollectionMusic = ArchivDMusic.moment;
                    break;
                case 4:
                    ArchivDBackground.buildL2D("saori", 49, 5f);
                    recollectionMusic = Musics.fine;
                    break;
                case 5:
                    ArchivDBackground.buildL2D("arisu", 68, 5f);
                    recollectionMusic = tree.loadMusic("somedaySometime");
                    break;
            }
        }
        loadSettings();
        switch (Core.settings.getInt("setSong")) {
            case 1:
                Musics.menu = tree.loadMusic("menucm");
                break;
            case 2:
                Musics.menu = tree.loadMusic("menure-aoh");
                break;
            case 3:
                Musics.menu = recollectionMusic;
                break;
        }

    }

    void loadSettings(){
        ui.settings.addCategory("ArchiveDustry", t -> {
            t.pref(new Banner("bluearchive-logo", -1));
            t.pref(new TextSeparator(Core.bundle.get("setting.category.general-setting")));
            t.pref(new Separator(4));
            if (Core.settings.getBool("enableL2D")) {
                t.sliderPref("setL2D", 2, 1, 5, 1, i -> Core.bundle.get("ba-l2d" + (int) i + ".name"));
                t.sliderPref("setSong", 1, 1, 3, 1, i -> {
                    switch (i) {
                        case 1:
                            Musics.menu = tree.loadMusic("menucm");
                            break;
                        case 2:
                            Musics.menu = tree.loadMusic("menure-aoh");
                            break;
                        case 3:
                            if (Core.settings.getBool("enableL2D")) {
                                Musics.menu = recollectionMusic;
                            }
                            break;
                    }
                    return Core.bundle.get("ba-music" + (int) i + ".name");
                });
            } else {
                t.sliderPref("setSong", 1, 1, 2, 1, i -> {
                    switch (i) {
                        case 1:
                            Musics.menu = tree.loadMusic("menucm");
                            break;
                        case 2:
                            Musics.menu = tree.loadMusic("menure-aoh");
                            break;
                    }
                    return Core.bundle.get("ba-music" + (int) i + ".name");
                });
            }
            if(Core.settings.getBool("live2dinstalled", false)) {
                t.checkPref("enableL2D", false);
            }
            t.checkPref("ba-firstTime", true);
            t.checkPref("ba-addHalo", true);
            t.pref(new ButtonSetting("ba-downloadLive2D", Icon.download, ArchivDBackground::downloadLive2D, 32));
            t.pref(new TextSeparator(Core.bundle.get("setting.category.links")));
            t.pref(new Separator(4));
            t.pref(new ButtonSetting("ba-youtube", Icon.play, () -> {
                Core.app.openURI("https://www.youtube.com/channel/UCsrnDYrkovQhCCE8kwKcvKQ");
            }, 32));
            t.pref(new ButtonSetting("ba-github", Icon.github, () -> {
                Core.app.openURI("https://www.github.com/willoizcitron/archivedustry-java");
            }, 32));
            t.pref(new ButtonSetting(Core.bundle.get("credits"), Icon.info, ArchivDCreditsDialog::new, 32));
            t.pref(new TextSeparator(Core.bundle.get("setting.category.mixer")));
            t.pref(new Separator(4));
            int defaultVolume = Core.settings.has("musicvol") ? Core.settings.getInt("musicvol") : (int) Core.settings.getDefault("musicvol");
            t.sliderPref("gameOver", defaultVolume,0, 100,1,i -> {
                tree.loadMusic("win").setVolume(i / 100f);
                tree.loadMusic("lose").setVolume(i / 100f);
                return i + "%";
            });
            t.sliderPref("research", defaultVolume,0, 100,1,i -> {
                tree.loadMusic("research").setVolume(i / 100f);
                return i + "%";
            });
            t.sliderPref("coreDatabase", defaultVolume,0, 100,1,i -> {
                tree.loadMusic("database").setVolume(i / 100f);
                return i + "%";
            });
            t.sliderPref("loadout", defaultVolume,0, 100,1,i -> {
                tree.loadMusic("loadout").setVolume(i / 100f);
                return i + "%";
            });
            t.pref(new Separator(2));
            if (OS.username.startsWith("willoizcitron")){
                t.pref(new Separator(2));
                t.pref(new TextSeparator("< Developer Settings >"));
            };
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

    public static Music soundControlPlaying() {
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

